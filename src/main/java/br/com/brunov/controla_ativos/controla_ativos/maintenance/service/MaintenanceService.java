package br.com.brunov.controla_ativos.controla_ativos.maintenance.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import br.com.brunov.controla_ativos.controla_ativos.asset.entities.Asset;
import br.com.brunov.controla_ativos.controla_ativos.asset.entities.AssetStatus;
import br.com.brunov.controla_ativos.controla_ativos.asset.repository.AssetRepository;
import br.com.brunov.controla_ativos.controla_ativos.config.exception.BusinessException;
import br.com.brunov.controla_ativos.controla_ativos.config.exception.ResourceConflictException;
import br.com.brunov.controla_ativos.controla_ativos.config.exception.ResourceNotFoundException;
import br.com.brunov.controla_ativos.controla_ativos.employee.entities.Employee;
import br.com.brunov.controla_ativos.controla_ativos.employee.repository.EmployeeRepository;
import br.com.brunov.controla_ativos.controla_ativos.maintenance.dto.MaintenanceRequest;
import br.com.brunov.controla_ativos.controla_ativos.maintenance.dto.MaintenanceResponse;
import br.com.brunov.controla_ativos.controla_ativos.maintenance.dto.MaintenanceUpdateRequest;
import br.com.brunov.controla_ativos.controla_ativos.maintenance.entities.Maintenance;
import br.com.brunov.controla_ativos.controla_ativos.maintenance.entities.MaintenanceStatus;
import br.com.brunov.controla_ativos.controla_ativos.maintenance.repository.MaintenanceRepository;
import jakarta.validation.Valid;

@Service
@Validated
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final AssetRepository assetRepository;
    private final EmployeeRepository employeeRepository;

    public MaintenanceService(
            MaintenanceRepository maintenanceRepository,
            AssetRepository assetRepository,
            EmployeeRepository employeeRepository) {

        this.maintenanceRepository = maintenanceRepository;
        this.assetRepository = assetRepository;
        this.employeeRepository = employeeRepository;
    }

    // Cadastrar manutenção
    @Transactional
    public MaintenanceResponse create(
            @Valid MaintenanceRequest request) {

        Asset asset = findAsset(request.assetId());

        if (asset.getStatus() == AssetStatus.MAINTENANCE
                || asset.getStatus() == AssetStatus.DISCARDED) {
            throw new BusinessException(
                    "O ativo não pode receber uma nova manutenção.");
        }

        Employee technician = null;

        if (request.technicianId() != null) {
            technician = findEmployee(request.technicianId());
        }

        Maintenance maintenance = Maintenance.builder()
                .asset(asset)
                .type(request.type())
                .title(request.title())
                .description(request.description())
                .scheduledDate(request.scheduledDate())
                .technician(technician)
                .cost(request.cost())
                .notes(request.notes())
                .status(MaintenanceStatus.SCHEDULED)
                .build();

        Maintenance saved = maintenanceRepository.save(maintenance);

        return MaintenanceResponse.fromEntity(saved);
    }

    // Listar todas as manutenções
    @Transactional(readOnly = true)
    public List<MaintenanceResponse> findAll() {
        return maintenanceRepository.findAll()
                .stream()
                .map(MaintenanceResponse::fromEntity)
                .toList();
    }

    // Buscar manutenção por ID
    @Transactional(readOnly = true)
    public MaintenanceResponse findById(Long id) {
        return MaintenanceResponse.fromEntity(findMaintenance(id));
    }

    // Consultar histórico de um ativo
    @Transactional(readOnly = true)
    public List<MaintenanceResponse> findByAssetId(Long assetId) {

        findAsset(assetId);

        return maintenanceRepository.findByAssetId(assetId)
                .stream()
                .map(MaintenanceResponse::fromEntity)
                .toList();
    }

    // Consultar manutenções por status
    @Transactional(readOnly = true)
    public List<MaintenanceResponse> findByStatus(
            MaintenanceStatus status) {

        return maintenanceRepository.findByStatus(status)
                .stream()
                .map(MaintenanceResponse::fromEntity)
                .toList();
    }

    // Atualização parcial
    @Transactional
    public MaintenanceResponse update(
            Long id,
            @Valid MaintenanceUpdateRequest request) {

        Maintenance maintenance = findMaintenance(id);

        if (maintenance.getStatus() == MaintenanceStatus.COMPLETED
                || maintenance.getStatus() == MaintenanceStatus.CANCELLED) {
            throw new BusinessException(
                    "Não é permitido alterar uma manutenção concluída ou cancelada.");
        }

        if (request.getType() != null) {
            if (maintenance.getStatus() != MaintenanceStatus.SCHEDULED) {
                throw new BusinessException(
                        "O tipo só pode ser alterado antes do início da manutenção.");
            }

            maintenance.setType(request.getType());
        }

        if (request.getTitle() != null) {
            if (request.getTitle().isBlank()) {
                throw new BusinessException(
                        "O título não pode ficar vazio.");
            }

            maintenance.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            maintenance.setDescription(request.getDescription());
        }

        if (request.getScheduledDate() != null) {
            if (maintenance.getStatus() != MaintenanceStatus.SCHEDULED) {
                throw new BusinessException(
                        "A data agendada só pode ser alterada antes do início.");
            }

            maintenance.setScheduledDate(request.getScheduledDate());
        }

        if (request.getTechnicianId() != null) {
            maintenance.setTechnician(
                    findEmployee(request.getTechnicianId()));
        }

        if (request.getCost() != null) {
            maintenance.setCost(request.getCost());
        }

        if (request.getNotes() != null) {
            maintenance.setNotes(request.getNotes());
        }

        Maintenance updated = maintenanceRepository.save(maintenance);

        return MaintenanceResponse.fromEntity(updated);
    }

    // Iniciar manutenção
    @Transactional
    public MaintenanceResponse start(Long id) {

        Maintenance maintenance = findMaintenance(id);

        if (maintenance.getStatus() != MaintenanceStatus.SCHEDULED) {
            throw new ResourceConflictException(
                    "Somente manutenções agendadas podem ser iniciadas.");
        }

        Asset asset = maintenance.getAsset();

        if (asset.getStatus() != AssetStatus.AVAILABLE) {
            throw new BusinessException(
                    "O ativo precisa estar disponível para iniciar a manutenção.");
        }

        boolean anotherMaintenanceInProgress =
                maintenanceRepository.existsByAssetIdAndStatus(
                        asset.getId(),
                        MaintenanceStatus.IN_PROGRESS);

        if (anotherMaintenanceInProgress) {
            throw new ResourceConflictException(
                    "Já existe uma manutenção em andamento para este ativo.");
        }

        maintenance.setStatus(MaintenanceStatus.IN_PROGRESS);
        maintenance.setStartedAt(LocalDateTime.now());

        asset.setStatus(AssetStatus.MAINTENANCE);

        assetRepository.save(asset);

        Maintenance updated = maintenanceRepository.save(maintenance);

        return MaintenanceResponse.fromEntity(updated);
    }

    // Concluir manutenção
    @Transactional
    public MaintenanceResponse complete(Long id) {

        Maintenance maintenance = findMaintenance(id);

        if (maintenance.getStatus() != MaintenanceStatus.IN_PROGRESS) {
            throw new BusinessException(
                    "Somente manutenções em andamento podem ser concluídas.");
        }

        Asset asset = maintenance.getAsset();

        maintenance.setStatus(MaintenanceStatus.COMPLETED);
        maintenance.setCompletedAt(LocalDateTime.now());

        asset.setStatus(AssetStatus.AVAILABLE);

        assetRepository.save(asset);

        Maintenance updated = maintenanceRepository.save(maintenance);

        return MaintenanceResponse.fromEntity(updated);
    }

    // Cancelar manutenção agendada
    @Transactional
    public MaintenanceResponse cancel(Long id) {

        Maintenance maintenance = findMaintenance(id);

        if (maintenance.getStatus() != MaintenanceStatus.SCHEDULED) {
            throw new BusinessException(
                    "Somente manutenções agendadas podem ser canceladas.");
        }

        maintenance.setStatus(MaintenanceStatus.CANCELLED);

        Maintenance updated = maintenanceRepository.save(maintenance);

        return MaintenanceResponse.fromEntity(updated);
    }

    // Métodos auxiliares

    private Maintenance findMaintenance(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Manutenção não encontrada: " + id));
    }

    private Asset findAsset(Long id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ativo não encontrado: " + id));
    }

    private Employee findEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Funcionário não encontrado: " + id));
    }
}


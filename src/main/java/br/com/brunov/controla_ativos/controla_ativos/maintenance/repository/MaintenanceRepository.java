package br.com.brunov.controla_ativos.controla_ativos.maintenance.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.brunov.controla_ativos.controla_ativos.maintenance.entities.Maintenance;
import br.com.brunov.controla_ativos.controla_ativos.maintenance.entities.MaintenanceStatus;
import br.com.brunov.controla_ativos.controla_ativos.maintenance.entities.MaintenanceType;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long>{
     // Busca todas as manutenções de um ativo
    List<Maintenance> findByAssetId(Long assetId);

    // Busca manutenções de um ativo por status
    List<Maintenance> findByAssetIdAndStatus(
            Long assetId,
            MaintenanceStatus status
    );

    // Busca manutenções por status
    List<Maintenance> findByStatus(MaintenanceStatus status);

    // Busca manutenções por tipo
    List<Maintenance> findByType( MaintenanceType type);

    // Busca manutenções agendadas para uma data
    List<Maintenance> findByScheduledDate(LocalDate scheduledDate);

    // Busca manutenções dentro de um período agendado
    List<Maintenance> findByScheduledDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );

    // Verifica se um ativo possui manutenção em determinado status
    boolean existsByAssetIdAndStatus(
            Long assetId,
            MaintenanceStatus status
    );

}

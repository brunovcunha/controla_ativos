package br.com.brunov.controla_ativos.controla_ativos.maintenance.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.brunov.controla_ativos.controla_ativos.maintenance.dto.MaintenanceRequest;
import br.com.brunov.controla_ativos.controla_ativos.maintenance.dto.MaintenanceResponse;
import br.com.brunov.controla_ativos.controla_ativos.maintenance.dto.MaintenanceUpdateRequest;
import br.com.brunov.controla_ativos.controla_ativos.maintenance.entities.MaintenanceStatus;
import br.com.brunov.controla_ativos.controla_ativos.maintenance.service.MaintenanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag (
    name = "Manutenção",
    description = "Gerenciamento de manutenções de ativos"
)

@RestController
@RequestMapping("/api/maintenances")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    public MaintenanceController(
            MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    // Cadastrar manutenção
    @PostMapping
    public ResponseEntity<MaintenanceResponse> create(
            @Valid @RequestBody MaintenanceRequest request) {

        MaintenanceResponse response =
                maintenanceService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Listar todas as manutenções
    @GetMapping
    public ResponseEntity<List<MaintenanceResponse>> findAll() {

        return ResponseEntity.ok(
                maintenanceService.findAll());
    }

    // Consultar manutenção por ID
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceResponse> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                maintenanceService.findById(id));
    }

    // Consultar histórico de um ativo
    @GetMapping("/asset/{assetId}")
    public ResponseEntity<List<MaintenanceResponse>> findByAssetId(
            @PathVariable Long assetId) {

        return ResponseEntity.ok(
                maintenanceService.findByAssetId(assetId));
    }

    // Consultar manutenções por status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<MaintenanceResponse>> findByStatus(
            @PathVariable MaintenanceStatus status) {

        return ResponseEntity.ok(
                maintenanceService.findByStatus(status));
    }

    // Atualização parcial
    @PatchMapping("/{id}")
    public ResponseEntity<MaintenanceResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody MaintenanceUpdateRequest request) {

        return ResponseEntity.ok(
                maintenanceService.update(id, request));
    }

    // Iniciar manutenção
    @PatchMapping("/{id}/start")
    public ResponseEntity<MaintenanceResponse> start(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                maintenanceService.start(id));
    }

    // Concluir manutenção
    @PatchMapping("/{id}/complete")
    public ResponseEntity<MaintenanceResponse> complete(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                maintenanceService.complete(id));
    }

    // Cancelar manutenção
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<MaintenanceResponse> cancel(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                maintenanceService.cancel(id));
    }
}


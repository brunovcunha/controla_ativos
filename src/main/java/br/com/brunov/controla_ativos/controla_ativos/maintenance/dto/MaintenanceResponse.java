package br.com.brunov.controla_ativos.controla_ativos.maintenance.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.brunov.controla_ativos.controla_ativos.maintenance.entities.Maintenance;
import br.com.brunov.controla_ativos.controla_ativos.maintenance.entities.MaintenanceStatus;
import br.com.brunov.controla_ativos.controla_ativos.maintenance.entities.MaintenanceType;

public record MaintenanceResponse(
        Long id,
        Long assetId,
        String assetTag,
        String assetName,
        MaintenanceType type,
        String title,
        String description,
        MaintenanceStatus status,
        LocalDate scheduledDate,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        Long technicianId,
        String technicianName,
        BigDecimal cost,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static MaintenanceResponse fromEntity(Maintenance maintenance) {
        return new MaintenanceResponse(
                maintenance.getId(),
                maintenance.getAsset().getId(),
                maintenance.getAsset().getAssetTag(),
                maintenance.getAsset().getName(),
                maintenance.getType(),
                maintenance.getTitle(),
                maintenance.getDescription(),
                maintenance.getStatus(),
                maintenance.getScheduledDate(),
                maintenance.getStartedAt(),
                maintenance.getCompletedAt(),
                maintenance.getTechnician() != null
                        ? maintenance.getTechnician().getId()
                        : null,
                maintenance.getTechnician() != null
                        ? maintenance.getTechnician().getName()
                        : null,
                maintenance.getCost(),
                maintenance.getNotes(),
                maintenance.getCreatedAt(),
                maintenance.getUpdatedAt()
        );
    }
}


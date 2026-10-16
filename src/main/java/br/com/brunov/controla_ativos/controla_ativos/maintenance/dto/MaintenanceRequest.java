package br.com.brunov.controla_ativos.controla_ativos.maintenance.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.brunov.controla_ativos.controla_ativos.maintenance.entities.MaintenanceType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MaintenanceRequest(

        @NotNull(message = "O ID do ativo é obrigatório")
        Long assetId,

        @NotNull(message = "O tipo de manutenção é obrigatório")
        MaintenanceType type,

        @NotBlank(message = "O título é obrigatório")
        @Size(max = 150, message = "O título deve ter no máximo 150 caracteres")
        String title,

        String description,

        LocalDate scheduledDate,

        Long technicianId,

        @DecimalMin(value = "0.0", message = "O custo não pode ser negativo")
        BigDecimal cost,

        String notes

) {}


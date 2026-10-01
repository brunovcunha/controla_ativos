package br.com.brunov.controla_ativos.controla_ativos.loan.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record LoanUpdateRequest(

    @NotNull(message = "O ativo é obrigatório.")
    Long assetId,

    @NotNull(message = "O colaborador é obrigatório.")
    Long employeeId,

    @NotNull(message = "A data inicial é obrigatória.")
    @Future(message = "A data inicial deve ser futura.")
    LocalDateTime scheduledStart,

    @NotNull(message = "A data final é obrigatória.")
    @Future(message = "A data final deve ser futura.")
    LocalDateTime scheduledEnd,

    @Size(max = 255)
    String purpose,

    @Size(max = 500)
    String notes
) {
}
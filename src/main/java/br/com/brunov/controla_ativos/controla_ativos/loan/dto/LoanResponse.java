package br.com.brunov.controla_ativos.controla_ativos.loan.dto;

import java.time.LocalDateTime;

import br.com.brunov.controla_ativos.controla_ativos.loan.entities.Loan;
import br.com.brunov.controla_ativos.controla_ativos.loan.entities.LoanStatus;

public record LoanResponse(

    Long id,

    Long assetId,

    String assetTag,

    String assetName,

    Long employeeId,

    LocalDateTime scheduledStart,

    LocalDateTime scheduledEnd,

    LocalDateTime pickupAt,

    LocalDateTime returnedAt,

    LoanStatus status,

    String purpose,

    String notes,

    LocalDateTime createdAt,

    LocalDateTime updatedAt

) {

    public static LoanResponse fromEntity(Loan loan) {

        return new LoanResponse(

            loan.getId(),

            loan.getAsset().getId(),

            loan.getAsset().getAssetTag(),

            loan.getAsset().getName(),

            loan.getEmployee().getId(),

            loan.getScheduledStart(),

            loan.getScheduledEnd(),

            loan.getPickupAt(),

            loan.getReturnedAt(),

            loan.getStatus(),

            loan.getPurpose(),

            loan.getNotes(),

            loan.getCreatedAt(),

            loan.getUpdatedAt()
        );
    }
}
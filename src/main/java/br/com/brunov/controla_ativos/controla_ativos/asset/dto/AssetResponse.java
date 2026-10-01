package br.com.brunov.controla_ativos.controla_ativos.asset.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.brunov.controla_ativos.controla_ativos.asset.entities.Asset;
import br.com.brunov.controla_ativos.controla_ativos.asset.entities.AssetStatus;
import br.com.brunov.controla_ativos.controla_ativos.asset.entities.AssetType;

public record AssetResponse(

        Long id,
        String assetTag,
        String name,
        AssetType type,
        String brand,
        String model,
        String serialNumber,
        AssetStatus status,
        LocalDate purchaseDate,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static AssetResponse fromEntity(Asset asset) {

        return new AssetResponse(
                asset.getId(),
                asset.getAssetTag(),
                asset.getName(),
                asset.getType(),
                asset.getBrand(),
                asset.getModel(),
                asset.getSerialNumber(),
                asset.getStatus(),
                asset.getPurchaseDate(),
                asset.getNotes(),
                asset.getCreatedAt(),
                asset.getUpdatedAt());
    }
}

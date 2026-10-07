package br.com.brunov.controla_ativos.controla_ativos.asset.dto;

import java.time.LocalDate;

import br.com.brunov.controla_ativos.controla_ativos.asset.entities.AssetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AssetRequest(
    @NotBlank(message = "O patrimônio é obrigatório.")
    @Size (max = 50)
    String assetTag,

    @NotBlank(message = "O nome do ativo é obrigatório.")
    @Size(max = 150)
    String name,

    @NotNull (message = "O tipo do ativo é obrigatório.")
    AssetType type,

    @Size(max = 100)
    String brand,

    @Size(max = 100)
    String model,

    @Size(max = 100)
    String serialNumber,

    LocalDate purchaseDate,

    @Size(max = 500)
    String notes,

    @NotBlank(message = "A localização é obrigatória.")
    Long locationId
) {
    
}

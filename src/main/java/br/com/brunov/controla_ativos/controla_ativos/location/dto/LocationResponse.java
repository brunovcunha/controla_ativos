package br.com.brunov.controla_ativos.controla_ativos.location.dto;

import java.time.LocalDateTime;

import br.com.brunov.controla_ativos.controla_ativos.location.entities.Location;

public record LocationResponse(

    Long id,
    String code,
    String name,
    String description,
    Boolean active,
    LocalDateTime createdAt,
    LocalDateTime updatedAt

) {

    public static LocationResponse fromEntity(Location location) {

        return new LocationResponse(
            location.getId(),
            location.getCode(),
            location.getName(),
            location.getDescription(),
            location.getActive(),
            location.getCreatedAt(),
            location.getUpdatedAt()
        );
    }
}
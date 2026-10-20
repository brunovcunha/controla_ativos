package br.com.brunov.controla_ativos.controla_ativos.location.dto;

import java.time.LocalDateTime;

import br.com.brunov.controla_ativos.controla_ativos.location.entities.Location;

public record LocationResponse(

    Long id,
    String name,
    String address,
    String room,
    Boolean active,
    LocalDateTime createdAt,
    LocalDateTime updatedAt

) {

    public static LocationResponse fromEntity(Location location) {

        return new LocationResponse(
            location.getId(),
            location.getName(),
            location.getRoom(),
            location.getAddress(),
            location.getActive(),
            location.getCreatedAt(),
            location.getUpdatedAt()
        );
    }
}
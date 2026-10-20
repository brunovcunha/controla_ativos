package br.com.brunov.controla_ativos.controla_ativos.location.dto;

import jakarta.validation.constraints.Size;

public record LocationUpdateRequest(
    @Size(max = 100)
    String name,

    @Size(max = 255)
    String address,

    @Size(max = 255)
    String room,

    Boolean active

) {}
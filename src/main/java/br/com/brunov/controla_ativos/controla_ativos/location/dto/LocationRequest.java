package br.com.brunov.controla_ativos.controla_ativos.location.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LocationRequest(

    @NotBlank(message = "O código da localização é obrigatório.")
    @Size(max = 20)
    String code,

    @NotBlank(message = "O nome da localização é obrigatório.")
    @Size(max = 100)
    String name,

    @Size(max = 255)
    String description

) {}
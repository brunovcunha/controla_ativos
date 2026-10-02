package br.com.brunov.controla_ativos.controla_ativos.department.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DepartmentUpdateRequest(

    @NotBlank(message = "O código do departamento é obrigatório.")
    @Size(max = 20)
    String code,

    @NotBlank(message = "O nome do departamento é obrigatório.")
    @Size(max = 100)
    String name,

    @Size(max = 255)
    String description,

    @NotNull(message = "O status do departamento é obrigatório.")
    Boolean active
) {
}
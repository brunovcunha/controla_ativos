package br.com.brunov.controla_ativos.controla_ativos.employee.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmployeeRequest(

    @NotBlank(message = "A matrícula é obrigatória.")
    @Size(max = 20)
    String registration,

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 150)
    String name,

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Informe um e-mail válido.")
    @Size(max = 150)
    String email,

    @Size(max = 20)
    String phone,

    @Size(max = 100)
    String jobTitle,

    @Size(max = 500)
    String notes
) {
}
package br.com.brunov.controla_ativos.controla_ativos.employee.dto;


import br.com.brunov.controla_ativos.controla_ativos.employee.entities.EmployeeStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EmployeeUpdateRequest(

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

    @NotNull(message = "O status é obrigatório.")
    EmployeeStatus status,

    @Size(max = 500)
    String notes
) {
}

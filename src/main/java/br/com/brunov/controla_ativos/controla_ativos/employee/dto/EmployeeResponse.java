package br.com.brunov.controla_ativos.controla_ativos.employee.dto;

import java.time.LocalDateTime;

import br.com.brunov.controla_ativos.controla_ativos.employee.entities.Employee;
import br.com.brunov.controla_ativos.controla_ativos.employee.entities.EmployeeStatus;

public record EmployeeResponse(

    Long id,

    String registration,

    String name,

    String email,

    String phone,

    String jobTitle,

    EmployeeStatus status,

    String notes,

    LocalDateTime createdAt,

    LocalDateTime updatedAt,

    Long departmentId,

    String departmentCode,

    String departmentName,

    Boolean departmentStatus

) {

    public static EmployeeResponse fromEntity(Employee employee) {

        return new EmployeeResponse(

            employee.getId(),

            employee.getRegistration(),

            employee.getName(),

            employee.getEmail(),

            employee.getPhone(),

            employee.getJobTitle(),

            employee.getStatus(),

            employee.getNotes(),

            employee.getCreatedAt(),

            employee.getUpdatedAt(),
            
            employee.getDepartment().getId(),

            employee.getDepartment().getCode(),

            employee.getDepartment().getName(),

            employee.getDepartment().getActive()
        );
    }
}

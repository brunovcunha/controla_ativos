package br.com.brunov.controla_ativos.controla_ativos.department.dto;

import java.time.LocalDateTime;

import br.com.brunov.controla_ativos.controla_ativos.department.entities.Department;

public record DepartmentResponse(

    Long id,

    String code,

    String name,

    String description,

    Boolean active,

    LocalDateTime createdAt,

    LocalDateTime updatedAt

) {

    public static DepartmentResponse fromEntity(
        Department department
    ) {

        return new DepartmentResponse(

            department.getId(),

            department.getCode(),

            department.getName(),

            department.getDescription(),

            department.getActive(),

            department.getCreatedAt(),

            department.getUpdatedAt()
        );
    }
}

package br.com.brunov.controla_ativos.controla_ativos.department.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.brunov.controla_ativos.controla_ativos.department.dto.DepartmentRequest;
import br.com.brunov.controla_ativos.controla_ativos.department.dto.DepartmentResponse;
import br.com.brunov.controla_ativos.controla_ativos.department.dto.DepartmentUpdateRequest;
import br.com.brunov.controla_ativos.controla_ativos.department.entities.Department;
import br.com.brunov.controla_ativos.controla_ativos.department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;


    @Transactional
    public DepartmentResponse create(
        DepartmentRequest request
    ) {

        if (departmentRepository
                .existsByCode(request.code())) {

            throw new IllegalArgumentException(
                "Já existe um departamento com o código: "
                    + request.code()
            );
        }

        Department department = Department.builder()
            .code(request.code())
            .name(request.name())
            .description(request.description())
            .active(true)
            .build();

        Department savedDepartment =
            departmentRepository.save(department);

        return DepartmentResponse.fromEntity(
            savedDepartment
        );
    }


    @Transactional(readOnly = true)
    public List<DepartmentResponse> findAll() {

        return departmentRepository.findAll()
            .stream()
            .map(DepartmentResponse::fromEntity)
            .toList();
    }


    @Transactional(readOnly = true)
    public DepartmentResponse findById(Long id) {

        Department department =
            findDepartment(id);

        return DepartmentResponse.fromEntity(
            department
        );
    }


    @Transactional
    public DepartmentResponse update(
        Long id,
        DepartmentUpdateRequest request
    ) {

        Department department =
            findDepartment(id);

        if (!department.getCode()
                .equalsIgnoreCase(request.code())
                && departmentRepository
                    .existsByCode(request.code())) {

            throw new IllegalArgumentException(
                "Já existe um departamento com o código: "
                    + request.code()
            );
        }

        department.setCode(
            request.code()
        );

        department.setName(
            request.name()
        );

        department.setDescription(
            request.description()
        );

        department.setActive(
            request.active()
        );

        Department updatedDepartment =
            departmentRepository.save(department);

        return DepartmentResponse.fromEntity(
            updatedDepartment
        );
    }


    @Transactional
    public void delete(Long id) {

        Department department =
            findDepartment(id);

        /*
         * Não excluímos fisicamente o departamento.
         *
         * Isso preserva o histórico dos funcionários
         * que pertenciam ao departamento.
         */
        department.setActive(false);

        departmentRepository.save(department);
    }


    private Department findDepartment(Long id) {

        return departmentRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException(
                    "Departamento não encontrado: " + id
                )
            );
    }
}
package br.com.brunov.controla_ativos.controla_ativos.employee.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.brunov.controla_ativos.controla_ativos.department.dto.DepartmentResponse;
import br.com.brunov.controla_ativos.controla_ativos.department.entities.Department;
import br.com.brunov.controla_ativos.controla_ativos.department.repository.DepartmentRepository;
import br.com.brunov.controla_ativos.controla_ativos.employee.dto.EmployeeRequest;
import br.com.brunov.controla_ativos.controla_ativos.employee.dto.EmployeeResponse;
import br.com.brunov.controla_ativos.controla_ativos.employee.dto.EmployeeUpdateRequest;
import br.com.brunov.controla_ativos.controla_ativos.employee.entities.Employee;
import br.com.brunov.controla_ativos.controla_ativos.employee.entities.EmployeeStatus;
import br.com.brunov.controla_ativos.controla_ativos.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public EmployeeResponse create(EmployeeRequest request) {

        if (employeeRepository
                .existsByRegistration(request.registration())) {

            throw new IllegalArgumentException(
                    "Já existe um funcionário com a matrícula: "
                            + request.registration());
        }

        if (employeeRepository
                .existsByEmail(request.email())) {

            throw new IllegalArgumentException(
                    "Já existe um funcionário com o e-mail: "
                            + request.email());
        }

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new RuntimeException(
                        "Departamento não encontrado: " + request.departmentId()));

        Employee employee = Employee.builder()
                .registration(request.registration())
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .jobTitle(request.jobTitle())
                .notes(request.notes())
                .status(EmployeeStatus.ACTIVE)
                .department(department)
                .build();

        Employee savedEmployee = employeeRepository.save(employee);

        return EmployeeResponse.fromEntity(savedEmployee);
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponse> findAll() {

        return employeeRepository.findAll()
                .stream()
                .map(EmployeeResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmployeeResponse findById(Long id) {

        Employee employee = findEmployee(id);

        return EmployeeResponse.fromEntity(employee);
    }

    @Transactional
    public EmployeeResponse update(
            Long id,
            EmployeeUpdateRequest request) {

        Employee employee = findEmployee(id);

        if (!employee.getRegistration()
                .equals(request.registration())
                && employeeRepository
                        .existsByRegistration(request.registration())) {

            throw new IllegalArgumentException(
                    "Já existe um funcionário com a matrícula: "
                            + request.registration());
        }

        if (!employee.getEmail()
                .equalsIgnoreCase(request.email())
                && employeeRepository
                        .existsByEmail(request.email())) {

            throw new IllegalArgumentException(
                    "Já existe um funcionário com o e-mail: "
                            + request.email());
        }

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new RuntimeException(
                        "Departamento não encontrado: " + request.departmentId()));

        employee.setRegistration(
                request.registration());

        employee.setName(
                request.name());

        employee.setEmail(
                request.email());

        employee.setPhone(
                request.phone());

        employee.setJobTitle(
                request.jobTitle());

        employee.setStatus(
                request.status());

        employee.setNotes(
                request.notes());

        employee.setDepartment(department);

        Employee updatedEmployee = employeeRepository.save(employee);

        return EmployeeResponse.fromEntity(updatedEmployee);
    }

    @Transactional
    public void delete(Long id) {

        Employee employee = findEmployee(id);

        /*
         * Não vamos excluir fisicamente o funcionário.
         *
         * Isso preserva o histórico de empréstimos.
         */
        employee.setStatus(EmployeeStatus.INACTIVE);

        employeeRepository.save(employee);
    }

    private Employee findEmployee(Long id) {

        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Funcionário não encontrado: " + id));
    }
}

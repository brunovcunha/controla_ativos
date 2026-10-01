package br.com.brunov.controla_ativos.controla_ativos.employee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.brunov.controla_ativos.controla_ativos.employee.entities.Employee;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    Optional<Employee> findByRegistration(String registration);

    boolean existsByRegistration(String registration);

    boolean existsByEmail(String email);
}

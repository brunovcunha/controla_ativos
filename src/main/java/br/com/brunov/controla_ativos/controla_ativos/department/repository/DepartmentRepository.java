package br.com.brunov.controla_ativos.controla_ativos.department.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.brunov.controla_ativos.controla_ativos.department.entities.Department;

public interface DepartmentRepository
        extends JpaRepository<Department, Long> {

    Optional<Department> findByCode(String code);

    boolean existsByCode(String code);
}
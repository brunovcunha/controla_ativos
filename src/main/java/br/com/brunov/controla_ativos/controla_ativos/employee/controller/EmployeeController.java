package br.com.brunov.controla_ativos.controla_ativos.employee.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.brunov.controla_ativos.controla_ativos.employee.dto.EmployeeRequest;
import br.com.brunov.controla_ativos.controla_ativos.employee.dto.EmployeeResponse;
import br.com.brunov.controla_ativos.controla_ativos.employee.dto.EmployeeUpdateRequest;
import br.com.brunov.controla_ativos.controla_ativos.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;


    @PostMapping
    public ResponseEntity<EmployeeResponse> create(
        @RequestBody @Valid EmployeeRequest request
    ) {

        EmployeeResponse response =
            employeeService.create(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }


    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> findAll() {

        return ResponseEntity.ok(
            employeeService.findAll()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> findById(
        @PathVariable Long id
    ) {

        return ResponseEntity.ok(
            employeeService.findById(id)
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> update(
        @PathVariable Long id,
        @RequestBody @Valid EmployeeUpdateRequest request
    ) {

        return ResponseEntity.ok(
            employeeService.update(id, request)
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable Long id
    ) {

        employeeService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
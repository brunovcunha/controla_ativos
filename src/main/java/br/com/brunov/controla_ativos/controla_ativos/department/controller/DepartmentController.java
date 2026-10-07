package br.com.brunov.controla_ativos.controla_ativos.department.controller;

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

import br.com.brunov.controla_ativos.controla_ativos.department.dto.DepartmentRequest;
import br.com.brunov.controla_ativos.controla_ativos.department.dto.DepartmentResponse;
import br.com.brunov.controla_ativos.controla_ativos.department.dto.DepartmentUpdateRequest;
import br.com.brunov.controla_ativos.controla_ativos.department.service.DepartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag (
    name = "Departamentos",
    description = "Gerenciamento dos departamentos da empresa"
)

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;


    @PostMapping
    public ResponseEntity<DepartmentResponse> create(
        @RequestBody @Valid DepartmentRequest request
    ) {

        DepartmentResponse response =
            departmentService.create(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }


    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> findAll() {

        return ResponseEntity.ok(
            departmentService.findAll()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> findById(
        @PathVariable Long id
    ) {

        return ResponseEntity.ok(
            departmentService.findById(id)
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponse> update(
        @PathVariable Long id,
        @RequestBody @Valid DepartmentUpdateRequest request
    ) {

        return ResponseEntity.ok(
            departmentService.update(id, request)
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable Long id
    ) {

        departmentService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
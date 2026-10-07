package br.com.brunov.controla_ativos.controla_ativos.location.controller;


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

import br.com.brunov.controla_ativos.controla_ativos.location.dto.LocationRequest;
import br.com.brunov.controla_ativos.controla_ativos.location.dto.LocationResponse;
import br.com.brunov.controla_ativos.controla_ativos.location.dto.LocationUpdateRequest;
import br.com.brunov.controla_ativos.controla_ativos.location.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<LocationResponse> create(
            @Valid @RequestBody LocationRequest request) {

        LocationResponse response =
                locationService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<LocationResponse>> findAll() {

        return ResponseEntity.ok(
                locationService.findAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationResponse> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                locationService.findById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody LocationUpdateRequest request) {

        return ResponseEntity.ok(
                locationService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        locationService.delete(id);

        return ResponseEntity.noContent().build();
    }
}

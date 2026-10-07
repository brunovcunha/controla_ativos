package br.com.brunov.controla_ativos.controla_ativos.asset.controller;

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

import br.com.brunov.controla_ativos.controla_ativos.asset.dto.AssetRequest;
import br.com.brunov.controla_ativos.controla_ativos.asset.dto.AssetResponse;
import br.com.brunov.controla_ativos.controla_ativos.asset.dto.AssetUpdateRequest;
import br.com.brunov.controla_ativos.controla_ativos.asset.service.AssetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "Ativos",
    description = "Gerenciamento dos ativos da empresa"
)

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor 
public class AssetController {
    private final AssetService assetService;

    @PostMapping
    public ResponseEntity<AssetResponse> create(
            @RequestBody @Valid AssetRequest request) {

        AssetResponse response =
            assetService.create(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @GetMapping
    public ResponseEntity<List<AssetResponse>> findAll() {

        return ResponseEntity.ok(
            assetService.findAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetResponse> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
            assetService.findById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid AssetUpdateRequest request) {

        return ResponseEntity.ok(
            assetService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        assetService.delete(id);

        return ResponseEntity.noContent().build();
    }
}

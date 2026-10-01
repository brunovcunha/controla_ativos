package br.com.brunov.controla_ativos.controla_ativos.loan.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.brunov.controla_ativos.controla_ativos.loan.dto.LoanRequest;
import br.com.brunov.controla_ativos.controla_ativos.loan.dto.LoanResponse;
import br.com.brunov.controla_ativos.controla_ativos.loan.dto.LoanUpdateRequest;
import br.com.brunov.controla_ativos.controla_ativos.loan.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<LoanResponse> create(
        @RequestBody @Valid LoanRequest request
    ) {

        LoanResponse response =
            loanService.create(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @GetMapping
    public ResponseEntity<List<LoanResponse>> findAll() {

        return ResponseEntity.ok(
            loanService.findAll()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<LoanResponse> findById(
        @PathVariable Long id
    ) {

        return ResponseEntity.ok(
            loanService.findById(id)
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<LoanResponse> update(
        @PathVariable Long id,
        @RequestBody @Valid LoanUpdateRequest request
    ) {

        return ResponseEntity.ok(
            loanService.update(id, request)
        );
    }


    @PostMapping("/{id}/pickup")
    public ResponseEntity<LoanResponse> pickup(
        @PathVariable Long id
    ) {

        return ResponseEntity.ok(
            loanService.pickup(id)
        );
    }


    @PostMapping("/{id}/return")
    public ResponseEntity<LoanResponse> returnAsset(
        @PathVariable Long id
    ) {

        return ResponseEntity.ok(
            loanService.returnAsset(id)
        );
    }


    @PostMapping("/{id}/cancel")
    public ResponseEntity<LoanResponse> cancel(
        @PathVariable Long id
    ) {

        return ResponseEntity.ok(
            loanService.cancel(id)
        );
    }
}

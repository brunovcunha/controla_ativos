package br.com.brunov.controla_ativos.controla_ativos.loan.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.brunov.controla_ativos.controla_ativos.asset.entities.Asset;
import br.com.brunov.controla_ativos.controla_ativos.asset.entities.AssetStatus;
import br.com.brunov.controla_ativos.controla_ativos.asset.repository.AssetRepository;
import br.com.brunov.controla_ativos.controla_ativos.employee.entities.Employee;
import br.com.brunov.controla_ativos.controla_ativos.employee.repository.EmployeeRepository;
import br.com.brunov.controla_ativos.controla_ativos.loan.dto.LoanRequest;
import br.com.brunov.controla_ativos.controla_ativos.loan.dto.LoanResponse;
import br.com.brunov.controla_ativos.controla_ativos.loan.dto.LoanUpdateRequest;
import br.com.brunov.controla_ativos.controla_ativos.loan.entities.Loan;
import br.com.brunov.controla_ativos.controla_ativos.loan.entities.LoanStatus;
import br.com.brunov.controla_ativos.controla_ativos.loan.repository.LoanRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final AssetRepository assetRepository;
    private final EmployeeRepository employeeRepository;


    @Transactional
    public LoanResponse create(LoanRequest request) {

        validatePeriod(
            request.scheduledStart(),
            request.scheduledEnd()
        );

        Asset asset = assetRepository.findById(request.assetId())
            .orElseThrow(() ->
                new RuntimeException(
                    "Ativo não encontrado: " + request.assetId()
                )
            );

        validateAssetAvailability(asset);

        validateOverlap(
            asset.getId(),
            request.scheduledStart(),
            request.scheduledEnd(),
            null
        );

        Employee employee = employeeRepository.findById(request.employeeId())
    .orElseThrow(() ->
        new RuntimeException(
            "Funcionário não encontrado: " + request.employeeId()
        )
    );

        Loan loan = Loan.builder()
            .asset(asset)
            .employee(employee)
            .scheduledStart(request.scheduledStart())
            .scheduledEnd(request.scheduledEnd())
            .purpose(request.purpose())
            .notes(request.notes())
            .status(LoanStatus.SCHEDULED)
            .build();

        Loan savedLoan = loanRepository.save(loan);

        return LoanResponse.fromEntity(savedLoan);
    }


    @Transactional(readOnly = true)
    public List<LoanResponse> findAll() {

        return loanRepository.findAll()
            .stream()
            .map(LoanResponse::fromEntity)
            .toList();
    }


    @Transactional(readOnly = true)
    public LoanResponse findById(Long id) {

        Loan loan = findLoan(id);

        return LoanResponse.fromEntity(loan);
    }


    @Transactional
    public LoanResponse update(
        Long id,
        LoanUpdateRequest request
    ) {

        Loan loan = findLoan(id);

        if (loan.getStatus() != LoanStatus.SCHEDULED) {
            throw new IllegalStateException(
                "Somente empréstimos agendados podem ser alterados."
            );
        }

        validatePeriod(
            request.scheduledStart(),
            request.scheduledEnd()
        );

        Asset asset = assetRepository.findById(request.assetId())
            .orElseThrow(() ->
                new RuntimeException(
                    "Ativo não encontrado: " + request.assetId()
                )
            );

        validateAssetAvailability(asset);

        validateOverlap(
            asset.getId(),
            request.scheduledStart(),
            request.scheduledEnd(),
            id
        );

        Employee employee = employeeRepository.findById(request.employeeId())
    .orElseThrow(() ->
        new RuntimeException(
            "Funcionário não encontrado: " + request.employeeId()
        )
    );

        loan.setAsset(asset);
        loan.setEmployee(employee);
        loan.setScheduledStart(request.scheduledStart());
        loan.setScheduledEnd(request.scheduledEnd());
        loan.setPurpose(request.purpose());
        loan.setNotes(request.notes());

        Loan updatedLoan = loanRepository.save(loan);

        return LoanResponse.fromEntity(updatedLoan);
    }


    @Transactional
    public LoanResponse pickup(Long id) {

        Loan loan = findLoan(id);

        if (loan.getStatus() != LoanStatus.SCHEDULED &&
            loan.getStatus() != LoanStatus.PENDING_PICKUP) {

            throw new IllegalStateException(
                "O empréstimo não está disponível para retirada."
            );
        }

        Asset asset = loan.getAsset();

        if (asset.getStatus() != AssetStatus.AVAILABLE) {
            throw new IllegalStateException(
                "O ativo não está disponível para retirada."
            );
        }

        loan.setPickupAt(LocalDateTime.now());
        loan.setStatus(LoanStatus.IN_USE);

        asset.setStatus(AssetStatus.IN_USE);

        return LoanResponse.fromEntity(loan);
    }


    @Transactional
    public LoanResponse returnAsset(Long id) {

        Loan loan = findLoan(id);

        if (loan.getStatus() != LoanStatus.IN_USE) {
            throw new IllegalStateException(
                "Somente empréstimos em uso podem ser devolvidos."
            );
        }

        loan.setReturnedAt(LocalDateTime.now());
        loan.setStatus(LoanStatus.RETURNED);

        Asset asset = loan.getAsset();

        asset.setStatus(AssetStatus.AVAILABLE);

        return LoanResponse.fromEntity(loan);
    }


    @Transactional
    public LoanResponse cancel(Long id) {

        Loan loan = findLoan(id);

        if (loan.getStatus() != LoanStatus.SCHEDULED &&
            loan.getStatus() != LoanStatus.PENDING_PICKUP) {

            throw new IllegalStateException(
                "Este empréstimo não pode mais ser cancelado."
            );
        }

        loan.setStatus(LoanStatus.CANCELLED);

        return LoanResponse.fromEntity(loan);
    }


    private Loan findLoan(Long id) {

        return loanRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException(
                    "Empréstimo não encontrado: " + id
                )
            );
    }


    private void validatePeriod(
        LocalDateTime start,
        LocalDateTime end
    ) {

        if (!start.isBefore(end)) {

            throw new IllegalArgumentException(
                "A data inicial deve ser anterior à data final."
            );
        }
    }


    private void validateAssetAvailability(Asset asset) {

        if (asset.getStatus() == AssetStatus.MAINTENANCE ||
            asset.getStatus() == AssetStatus.DISCARDED) {

            throw new IllegalStateException(
                "O ativo não está disponível para empréstimo."
            );
        }
    }


    private void validateOverlap(
        Long assetId,
        LocalDateTime start,
        LocalDateTime end,
        Long loanId
    ) {

        List<LoanStatus> statuses = List.of(
            LoanStatus.SCHEDULED,
            LoanStatus.PENDING_PICKUP,
            LoanStatus.IN_USE
        );

        List<Loan> overlappingLoans =
            loanRepository.findOverlappingLoans(
                assetId,
                start,
                end,
                statuses
            );

        boolean conflict = overlappingLoans
            .stream()
            .anyMatch(loan ->
                loanId == null ||
                !loan.getId().equals(loanId)
            );

        if (conflict) {

            throw new IllegalStateException(
                "O ativo já possui um empréstimo "
                + "agendado para este período."
            );
        }
    }
}
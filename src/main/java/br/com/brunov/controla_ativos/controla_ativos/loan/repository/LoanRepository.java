package br.com.brunov.controla_ativos.controla_ativos.loan.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.brunov.controla_ativos.controla_ativos.loan.entities.Loan;
import br.com.brunov.controla_ativos.controla_ativos.loan.entities.LoanStatus;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("""
        SELECT l
        FROM Loan l
        WHERE l.asset.id = :assetId
          AND l.status IN :statuses
          AND l.scheduledStart < :scheduledEnd
          AND l.scheduledEnd > :scheduledStart
    """)
    List<Loan> findOverlappingLoans(
        @Param("assetId") Long assetId,
        @Param("scheduledStart") LocalDateTime scheduledStart,
        @Param("scheduledEnd") LocalDateTime scheduledEnd,
        @Param("statuses") List<LoanStatus> statuses
    );
}

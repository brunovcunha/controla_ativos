package br.com.brunov.controla_ativos.controla_ativos.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.brunov.controla_ativos.controla_ativos.location.entities.Location;

import java.util.Optional;

public interface LocationRepository
        extends JpaRepository<Location, Long> {

    Optional<Location> findByCode(String code);

    boolean existsByCode(String code);
}

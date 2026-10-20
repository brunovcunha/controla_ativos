package br.com.brunov.controla_ativos.controla_ativos.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.brunov.controla_ativos.controla_ativos.location.entities.Location;

import java.util.List;

public interface LocationRepository
        extends JpaRepository<Location, Long> {

    List<Location> findByName(String name);
}

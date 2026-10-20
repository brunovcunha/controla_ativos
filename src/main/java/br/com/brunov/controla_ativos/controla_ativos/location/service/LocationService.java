package br.com.brunov.controla_ativos.controla_ativos.location.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.brunov.controla_ativos.controla_ativos.config.exception.ResourceConflictException;
import br.com.brunov.controla_ativos.controla_ativos.config.exception.ResourceNotFoundException;
import br.com.brunov.controla_ativos.controla_ativos.location.dto.LocationRequest;
import br.com.brunov.controla_ativos.controla_ativos.location.dto.LocationResponse;
import br.com.brunov.controla_ativos.controla_ativos.location.dto.LocationUpdateRequest;
import br.com.brunov.controla_ativos.controla_ativos.location.entities.Location;
import br.com.brunov.controla_ativos.controla_ativos.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationService {

        private final LocationRepository locationRepository;

        @Transactional
        public LocationResponse create(LocationRequest request) {

                if (!locationRepository.findByName(request.name()).isEmpty()) {
                        throw new ResourceConflictException(
                                        "Já existe uma localização com o código: "
                                                        + request.name());
                }


                Location location = Location.builder()
                                .name(request.name())
                                .address(request.address())
                                .room(request.room())
                                .active(true)
                                .build();

                Location savedLocation = locationRepository.save(location);

                return LocationResponse.fromEntity(savedLocation);
        }

        @Transactional(readOnly = true)
        public List<LocationResponse> findAll() {

                return locationRepository.findAll()
                                .stream()
                                .map(LocationResponse::fromEntity)
                                .toList();
        }

        @Transactional(readOnly = true)
        public LocationResponse findById(Long id) {

                Location location = findLocation(id);

                return LocationResponse.fromEntity(location);
        }

        @Transactional
        public LocationResponse update(
                        Long id,
                        LocationUpdateRequest request) {

                Location location = findLocation(id);

                if (request.name() != null) {
                        location.setName(request.name());
                }

                if (request.address() != null) {
                        location.setAddress(request.address());
                }

                if (request.active() != null) {
                        location.setActive(request.active());
                }

                if (request.room() != null) {
                        location.setRoom(request.room());
                }

                Location updatedLocation = locationRepository.save(location);

                return LocationResponse.fromEntity(updatedLocation);
        }

        @Transactional
        public void delete(Long id) {

                Location location = findLocation(id);

                /*
                 * Não excluímos fisicamente o departamento.
                 *
                 * Isso preserva o histórico dos funcionários
                 * que pertenciam ao departamento.
                 */
                location.setActive(false);

                locationRepository.save(location);
        }

        public  Location findLocation(Long id) {

                return locationRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Localização não encontrada: " + id));
        }

}
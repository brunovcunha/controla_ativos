package br.com.brunov.controla_ativos.controla_ativos.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.brunov.controla_ativos.controla_ativos.config.exception.ResourceConflictException;
import br.com.brunov.controla_ativos.controla_ativos.config.exception.ResourceNotFoundException;
import br.com.brunov.controla_ativos.controla_ativos.location.dto.LocationRequest;
import br.com.brunov.controla_ativos.controla_ativos.location.dto.LocationResponse;
import br.com.brunov.controla_ativos.controla_ativos.location.dto.LocationUpdateRequest;
import br.com.brunov.controla_ativos.controla_ativos.location.entities.Location;
import br.com.brunov.controla_ativos.controla_ativos.location.repository.LocationRepository;
import br.com.brunov.controla_ativos.controla_ativos.location.service.LocationService;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    private Location location;

    @BeforeEach
    void setUp() {
        location = Location.builder()
                .id(1L)
                .name("Sala de TI")
                .address("Av. Brasil, 1500")
                .room("3º andar - TI")
                .active(true)
                .build();
    }

    @Test
    void shouldCreateLocationSuccessfully() {
        // Arrange
        LocationRequest request = new LocationRequest(
                "Estoque",
                "Av. Brasil, 1500",
                "Subsolo");

        when(locationRepository.findByName("Estoque")).thenReturn(List.of(location));

        when(locationRepository.save(any(Location.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        LocationResponse response = locationService.create(request);

        // Assert
        assertNotNull(response);

        ArgumentCaptor<Location> captor =
                ArgumentCaptor.forClass(Location.class);

        verify(locationRepository).save(captor.capture());

        Location savedLocation = captor.getValue();

        assertEquals("Estoque", savedLocation.getName());
        assertEquals("Av. Brasil, 1500", savedLocation.getAddress());
        assertEquals("Subsolo", savedLocation.getRoom());
        assertTrue(savedLocation.getActive());

        verify(locationRepository).findByName("Estoque");
    }

    @Test
    void shouldThrowConflictWhenCreatingDuplicateName() {
        // Arrange
        LocationRequest request = new LocationRequest(
                "Sala de TI",
                "Outro endereço",
                "Sala 10");

        when(locationRepository.findByName("Sala de TI"))
                .thenReturn(List.of(location));

        // Act & Assert
        assertThrows(
                ResourceConflictException.class,
                () -> locationService.create(request));

        verify(locationRepository, never()).save(any(Location.class));
    }

    @Test
    void shouldReturnAllLocations() {
        // Arrange
        Location secondLocation = Location.builder()
                .id(2L)
                .name("Estoque")
                .address("Rua Central, 200")
                .room("Subsolo")
                .active(true)
                .build();

        when(locationRepository.findAll())
                .thenReturn(List.of(location, secondLocation));

        // Act
        List<LocationResponse> responses = locationService.findAll();

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());

        verify(locationRepository).findAll();
    }

    @Test
    void shouldFindLocationById() {
        // Arrange
        when(locationRepository.findById(1L))
                .thenReturn(Optional.of(location));

        // Act
        LocationResponse response = locationService.findById(1L);

        // Assert
        assertNotNull(response);

        verify(locationRepository).findById(1L);
    }

    @Test
    void shouldThrowNotFoundWhenFindingLocationById() {
        // Arrange
        when(locationRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> locationService.findById(99L));

        assertEquals(
                "Localização não encontrada: 99",
                exception.getMessage());

        verify(locationRepository, never()).save(any(Location.class));
    }

    @Test
    void shouldUpdateLocationPartially() {
        // Arrange
        LocationUpdateRequest request = new LocationUpdateRequest(
                "Sala de TI atualizada",
                null,
                null,
                null);

        when(locationRepository.findById(1L))
                .thenReturn(Optional.of(location));

        when(locationRepository.save(any(Location.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        LocationResponse response = locationService.update(1L, request);

        // Assert
        assertNotNull(response);

        ArgumentCaptor<Location> captor =
                ArgumentCaptor.forClass(Location.class);

        verify(locationRepository).save(captor.capture());

        Location updatedLocation = captor.getValue();

        assertEquals("Sala de TI atualizada", updatedLocation.getName());
        assertEquals("Av. Brasil, 1500", updatedLocation.getAddress());
        assertEquals("3º andar - TI", updatedLocation.getRoom());
        assertTrue(updatedLocation.getActive());
    }

    @Test
    void shouldUpdateLocationAddressAndRoom() {
        // Arrange
        LocationUpdateRequest request = new LocationUpdateRequest(
                null,
                "Rua Nova, 500",
                "2º andar",
                null);

        when(locationRepository.findById(1L))
                .thenReturn(Optional.of(location));

        when(locationRepository.save(any(Location.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        LocationResponse response = locationService.update(1L, request);

        // Assert
        assertNotNull(response);
        assertEquals("Rua Nova, 500", response.address());
        assertEquals("2º andar", response.room());
        assertEquals("Sala de TI", response.name());
    }

    @Test
    void shouldUpdateLocationActiveStatus() {
        // Arrange
        LocationUpdateRequest request = new LocationUpdateRequest(
                null,
                null,
                null,
                false);

        when(locationRepository.findById(1L))
                .thenReturn(Optional.of(location));

        when(locationRepository.save(any(Location.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        LocationResponse response = locationService.update(1L, request);

        // Assert
        assertNotNull(response);
        assertFalse(response.active());
    }

    @Test
    void shouldDeleteLocationLogically() {
        // Arrange
        when(locationRepository.findById(1L))
                .thenReturn(Optional.of(location));

        when(locationRepository.save(any(Location.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        locationService.delete(1L);

        // Assert
        ArgumentCaptor<Location> captor =
                ArgumentCaptor.forClass(Location.class);

        verify(locationRepository).save(captor.capture());

        Location deletedLocation = captor.getValue();

        assertFalse(deletedLocation.getActive());
        assertEquals(1L, deletedLocation.getId());

        // A exclusão lógica não remove o registro do banco.
        verify(locationRepository, never()).deleteById(anyLong());
        verify(locationRepository, never()).delete(any(Location.class));
    }

    @Test
    void shouldThrowNotFoundWhenDeletingNonexistentLocation() {
        // Arrange
        when(locationRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                ResourceNotFoundException.class,
                () -> locationService.delete(99L));

        verify(locationRepository, never()).save(any(Location.class));
    }
}
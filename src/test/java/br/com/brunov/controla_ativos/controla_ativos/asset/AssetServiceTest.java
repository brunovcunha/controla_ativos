package br.com.brunov.controla_ativos.controla_ativos.asset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.brunov.controla_ativos.controla_ativos.asset.dto.AssetRequest;
import br.com.brunov.controla_ativos.controla_ativos.asset.dto.AssetUpdateRequest;
import br.com.brunov.controla_ativos.controla_ativos.asset.entities.Asset;
import br.com.brunov.controla_ativos.controla_ativos.asset.entities.AssetStatus;
import br.com.brunov.controla_ativos.controla_ativos.asset.entities.AssetType;
import br.com.brunov.controla_ativos.controla_ativos.asset.repository.AssetRepository;
import br.com.brunov.controla_ativos.controla_ativos.asset.service.AssetService;
import br.com.brunov.controla_ativos.controla_ativos.config.exception.ResourceConflictException;
import br.com.brunov.controla_ativos.controla_ativos.config.exception.ResourceNotFoundException;
import br.com.brunov.controla_ativos.controla_ativos.location.entities.Location;
import br.com.brunov.controla_ativos.controla_ativos.location.service.LocationService;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private LocationService locationService;

    @InjectMocks
    private AssetService assetService;

    private Asset asset;
    private Location location;

    @BeforeEach
    void setUp() {
        location = mock(Location.class);

        asset = Asset.builder()
                .id(1L)
                .assetTag("PAT001")
                .name("Notebook Dell")
                .status(AssetStatus.AVAILABLE)
                .location(location)
                .build();
    }

    // CREATE

    @Test
    void shouldCreateAssetSuccessfullyWithoutLocation() {
        AssetRequest request = new AssetRequest(
                "PAT002",
                "Monitor Dell",
                AssetType.MONITOR,
                "Dell",
                "P2422H",
                "SER123",
                LocalDate.of(2025, 1, 15),
                "Monitor para escritório",
                null,
                500.0);

        when(assetRepository.existsByAssetTag("PAT002"))
                .thenReturn(false);

        when(assetRepository.save(any(Asset.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = assetService.create(request);

        assertNotNull(response);

        verify(assetRepository).existsByAssetTag("PAT002");
        verify(assetRepository).save(any(Asset.class));
        verifyNoInteractions(locationService);
    }

    @Test
    void shouldCreateAssetSuccessfullyWithLocation() {
        AssetRequest request = new AssetRequest(
                "PAT003",
                "Notebook Lenovo",
                AssetType.NOTEBOOK,
                "Lenovo",
                "ThinkPad",
                "SER456",
                LocalDate.of(2025, 2, 10),
                "Notebook para reuniões",
                10L,
                3000.00);

        when(assetRepository.existsByAssetTag("PAT003"))
                .thenReturn(false);

        when(locationService.findLocation(10L))
                .thenReturn(location);

        when(assetRepository.save(any(Asset.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = assetService.create(request);

        assertNotNull(response);

        verify(locationService).findLocation(10L);
        verify(assetRepository).save(any(Asset.class));
    }

    @Test
    void shouldThrowConflictWhenCreatingAssetWithDuplicateTag() {
        AssetRequest request = new AssetRequest(
                "PAT001",
                "Notebook Dell",
                null,
                "Dell",
                "Latitude",
                "SER789",
                null,
                null,
                null,
                3000.00);

        when(assetRepository.existsByAssetTag("PAT001"))
                .thenReturn(true);

        assertThrows(
                ResourceConflictException.class,
                () -> assetService.create(request));

        verify(assetRepository, never()).save(any(Asset.class));
        verifyNoInteractions(locationService);
    }

    // FIND ALL

    @Test
    void shouldReturnAllAssets() {
        Asset secondAsset = Asset.builder()
                .id(2L)
                .assetTag("PAT002")
                .name("Monitor Dell")
                .status(AssetStatus.AVAILABLE)
                .build();

        when(assetRepository.findAll())
                .thenReturn(List.of(asset, secondAsset));

        var response = assetService.findAll();

        assertEquals(2, response.size());
        verify(assetRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoAssetsExist() {
        when(assetRepository.findAll())
                .thenReturn(List.of());

        var response = assetService.findAll();

        assertTrue(response.isEmpty());
        verify(assetRepository).findAll();
    }

    // FIND BY ID

    @Test
    void shouldFindAssetById() {
        when(assetRepository.findById(1L))
                .thenReturn(Optional.of(asset));

        var response = assetService.findById(1L);

        assertNotNull(response);
        verify(assetRepository).findById(1L);
    }

    @Test
    void shouldThrowNotFoundWhenFindingNonexistentAsset() {
        when(assetRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> assetService.findById(99L));
    }

    // UPDATE

    @Test
    void shouldUpdateAssetPartially() {
        AssetUpdateRequest request = new AssetUpdateRequest(
                null, // assetTag
                "Notebook Dell Atualizado", // name
                null, // type
                null, // brand
                null, // model
                null, // serialNumber
                null, // status
                null, // purchaseDate
                "Equipamento atualizado", // notes
                null, // locationId
                null
        );

        when(assetRepository.findById(1L))
                .thenReturn(Optional.of(asset));

        when(assetRepository.save(any(Asset.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = assetService.update(1L, request);

        assertNotNull(response);
        assertEquals("Notebook Dell Atualizado", asset.getName());
        assertEquals("Equipamento atualizado", asset.getNotes());

        verify(assetRepository).save(asset);
        verifyNoInteractions(locationService);
    }

    @Test
    void shouldUpdateAssetTag() {
        AssetUpdateRequest request = new AssetUpdateRequest(
                "PAT010", null, null, null, null,
                null, null, null, null, null, null);

        when(assetRepository.findById(1L))
                .thenReturn(Optional.of(asset));

        when(assetRepository.save(any(Asset.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assetService.update(1L, request);

        assertEquals("PAT010", asset.getAssetTag());
        verify(assetRepository).save(asset);
    }

    @Test
    void shouldUpdateAssetLocation() {
        Location newLocation = mock(Location.class);

        AssetUpdateRequest request = new AssetUpdateRequest(
                null, null, null, null, null,
                null, null, null, null, 20L, null);

        when(assetRepository.findById(1L))
                .thenReturn(Optional.of(asset));

        when(locationService.findLocation(20L))
                .thenReturn(newLocation);

        when(assetRepository.save(any(Asset.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assetService.update(1L, request);

        assertSame(newLocation, asset.getLocation());
        verify(locationService).findLocation(20L);
        verify(assetRepository).save(asset);
    }

    @Test
    void shouldThrowNotFoundWhenUpdatingNonexistentAsset() {
        AssetUpdateRequest request = new AssetUpdateRequest(
                null, null, null, null, null,
                null, null, null, null, null, null);

        when(assetRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> assetService.update(99L, request));

        verify(assetRepository, never()).save(any(Asset.class));
        verifyNoInteractions(locationService);
    }

    @Test
    void shouldPropagateNotFoundWhenUpdatingToNonexistentLocation() {
        AssetUpdateRequest request = new AssetUpdateRequest(
                null, null, null, null, null,
                null, null, null, null, 99L, null);

        when(assetRepository.findById(1L))
                .thenReturn(Optional.of(asset));

        when(locationService.findLocation(99L))
                .thenThrow(new ResourceNotFoundException(
                        "Localização não encontrada: 99"));

        assertThrows(
                ResourceNotFoundException.class,
                () -> assetService.update(1L, request));

        verify(assetRepository, never()).save(any(Asset.class));
    }

    // DELETE

    @Test
    void shouldDeleteAssetSuccessfully() {
        when(assetRepository.existsById(1L))
                .thenReturn(true);

        assetService.delete(1L);

        verify(assetRepository).existsById(1L);
        verify(assetRepository).deleteById(1L);
    }

    @Test
    void shouldThrowNotFoundWhenDeletingNonexistentAsset() {
        when(assetRepository.existsById(99L))
                .thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> assetService.delete(99L));

        verify(assetRepository, never()).deleteById(anyLong());
    }
}

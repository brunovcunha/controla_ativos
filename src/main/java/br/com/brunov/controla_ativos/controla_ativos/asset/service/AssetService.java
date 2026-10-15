package br.com.brunov.controla_ativos.controla_ativos.asset.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.brunov.controla_ativos.controla_ativos.asset.dto.AssetRequest;
import br.com.brunov.controla_ativos.controla_ativos.asset.dto.AssetResponse;
import br.com.brunov.controla_ativos.controla_ativos.asset.dto.AssetUpdateRequest;
import br.com.brunov.controla_ativos.controla_ativos.asset.entities.Asset;
import br.com.brunov.controla_ativos.controla_ativos.asset.entities.AssetStatus;
import br.com.brunov.controla_ativos.controla_ativos.asset.repository.AssetRepository;
import br.com.brunov.controla_ativos.controla_ativos.location.entities.Location;
import br.com.brunov.controla_ativos.controla_ativos.location.repository.LocationRepository;
import br.com.brunov.controla_ativos.controla_ativos.location.service.LocationService;

@Service
public class AssetService {
    private final AssetRepository assetRepository;
    private final LocationService locationService;

    public AssetService(AssetRepository assetRepository, LocationService locationService) {
        this.assetRepository = assetRepository;
        this.locationService = locationService;
    }

    @Transactional
    public AssetResponse create(AssetRequest request) {

        if (assetRepository.existsByAssetTag(request.assetTag())) {
            throw new IllegalArgumentException(
                    "Já existe um ativo cadastrado com o patrimônio: "
                            + request.assetTag());
        }

        Asset asset = Asset.builder()
                .assetTag(request.assetTag())
                .name(request.name())
                .type(request.type())
                .brand(request.brand())
                .model(request.model())
                .serialNumber(request.serialNumber())
                .purchaseDate(request.purchaseDate())
                .notes(request.notes())
                .status(AssetStatus.AVAILABLE)
                .location(request.locationId() == null
                        ? null
                        : locationService.findLocation(request.locationId()))
                .build();

        Asset savedAsset = assetRepository.save(asset);

        return AssetResponse.fromEntity(savedAsset);
    }

    @Transactional(readOnly = true)
    public List<AssetResponse> findAll() {

        return assetRepository.findAll()
                .stream()
                .map(AssetResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public AssetResponse findById(Long id) {

        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Ativo não encontrado: " + id));

        return AssetResponse.fromEntity(asset);
    }

    @Transactional
    public AssetResponse update(
            Long id,
            AssetUpdateRequest request) {

        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Ativo não encontrado: " + id));

        if (request.assetTag() != null) {
            asset.setAssetTag(request.assetTag());
        }

        if (request.name() != null) {
            asset.setName(request.name());
        }

        if (request.type() != null) {
            asset.setType(request.type());
        }

        if (request.brand() != null) {
            asset.setBrand(request.brand());
        }

        if (request.model() != null) {
            asset.setModel(request.model());
        }

        if (request.serialNumber() != null) {
            asset.setSerialNumber(request.serialNumber());
        }

        if (request.status() != null) {
            asset.setStatus(request.status());
        }

        if (request.purchaseDate() != null) {
            asset.setPurchaseDate(request.purchaseDate());
        }

        if (request.notes() != null) {
            asset.setNotes(request.notes());
        }

        if (request.locationId() != null) {
            Location location = locationService.findLocation(
                    request.locationId());

            asset.setLocation(location);
        }

        Asset updatedAsset = assetRepository.save(asset);

        return AssetResponse.fromEntity(updatedAsset);
    }

    @Transactional
    public void delete(Long id) {

        if (!assetRepository.existsById(id)) {
            throw new RuntimeException(
                    "Ativo não encontrado: " + id);
        }

        assetRepository.deleteById(id);
    }
}

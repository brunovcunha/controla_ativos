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

@Service 
public class AssetService {

    private final AssetRepository assetRepository;
    private final LocationRepository locationRepository;

    public AssetService(AssetRepository assetRepository, LocationRepository locationRepository) {
        this.assetRepository = assetRepository;
        this.locationRepository = locationRepository;
    }

    @Transactional
    public AssetResponse create(AssetRequest request) {

        if (assetRepository.existsByAssetTag(request.assetTag())) {
            throw new IllegalArgumentException(
                "Já existe um ativo cadastrado com o patrimônio: "
                    + request.assetTag()
            );
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

    @Transactional (readOnly = true)
    public AssetResponse findById(Long id) {

        Asset asset = assetRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException(
                    "Ativo não encontrado: " + id
                )
            );

        return AssetResponse.fromEntity(asset);
    }

    @Transactional
    public AssetResponse update(
            Long id,
            AssetUpdateRequest request) {

        Asset asset = assetRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException(
                    "Ativo não encontrado: " + id
                )
            );

        Location location = locationRepository.findById(request.locationId()).orElseThrow(() -> 
                new RuntimeException("Localização do ativo não encontrada!")
    );

        asset.setAssetTag(request.assetTag());
        asset.setName(request.name());
        asset.setType(request.type());
        asset.setBrand(request.brand());
        asset.setModel(request.model());
        asset.setSerialNumber(request.serialNumber());
        asset.setStatus(request.status());
        asset.setPurchaseDate(request.purchaseDate());
        asset.setNotes(request.notes());
        asset.setLocation(location);

        Asset updatedAsset = assetRepository.save(asset);

        return AssetResponse.fromEntity(updatedAsset);
    }

    @Transactional
    public void delete(Long id) {

        if (!assetRepository.existsById(id)) {
            throw new RuntimeException(
                "Ativo não encontrado: " + id
            );
        }

        assetRepository.deleteById(id);
    }
}

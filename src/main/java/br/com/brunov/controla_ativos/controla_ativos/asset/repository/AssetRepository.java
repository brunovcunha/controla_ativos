package br.com.brunov.controla_ativos.controla_ativos.asset.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.brunov.controla_ativos.controla_ativos.asset.entities.Asset;
import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    Optional<Asset> findByAssetTag(String assetTag);

    boolean existsByAssetTag(String assetTag);
}

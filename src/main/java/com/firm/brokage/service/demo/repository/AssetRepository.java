package com.firm.brokage.service.demo.repository;
import com.firm.brokage.service.demo.entities.Asset;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
  Asset findByCustomerIdAndAssetName(Long customerId, String assetName);

  List<Asset> findByCustomerId(Long customerId);
}

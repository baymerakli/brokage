package com.firm.brokage.service.demo.repository;
import com.firm.brokage.service.demo.entities.Asset;
import com.firm.brokage.service.demo.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
  Asset findByCustomerIdAndAssetName(Long customerId, String assetName);

  List<Asset> findByCustomerId(Long customerId);
}

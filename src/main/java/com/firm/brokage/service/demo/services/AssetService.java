package com.firm.brokage.service.demo.services;

import static com.firm.brokage.service.demo.common.APICommonUtilConstant.BASE_ASSET;

import com.firm.brokage.service.demo.entities.Asset;
import com.firm.brokage.service.demo.entities.Customer;
import com.firm.brokage.service.demo.entities.Order;
import com.firm.brokage.service.demo.enumaration.OrderSide;
import com.firm.brokage.service.demo.exceptions.CustomerNotFoundException;
import com.firm.brokage.service.demo.exceptions.InsufficientFundsException;
import com.firm.brokage.service.demo.repository.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class AssetService {

  @Autowired
  private AssetRepository assetRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  public Customer getCustomerById(Long customerId) {
    Optional<Customer> customer = customerRepository.findById(customerId);
    if(customer.isEmpty()) {
      throw new CustomerNotFoundException();
    }
    return customer.get();
  }

  public void updateAssetWithOrderMatch(Order order) {
    Asset baseAsset = assetRepository.findByCustomerIdAndAssetName(order.getCustomer().getId(), BASE_ASSET);
    if (baseAsset == null) {
      throw new InsufficientFundsException("Customer does not have a "+BASE_ASSET+" asset.");
    }

    BigDecimal totalTransactionAmount = order.getPrice().multiply(order.getSize());

    if (OrderSide.SELL.equals(order.getOrderSide())) {
      Asset assetToSell = assetRepository.findByCustomerIdAndAssetName(order.getCustomer().getId(), order.getAssetName());
      if (assetToSell == null || assetToSell.getUsableSize().compareTo(order.getSize()) < 0) {
        throw new InsufficientFundsException("Not enough asset quantity to sell.");
      }

      assetToSell.setSize(assetToSell.getSize().subtract(order.getSize()));
      assetToSell.setUsableSize(assetToSell.getUsableSize().subtract(order.getSize()));
      assetToSell.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
      assetRepository.save(assetToSell);

      baseAsset.setSize(baseAsset.getSize().add(totalTransactionAmount));
      baseAsset.setUsableSize(baseAsset.getUsableSize().add(totalTransactionAmount));
    } else if (OrderSide.BUY.equals(order.getOrderSide())) {
      if (baseAsset.getUsableSize().compareTo(totalTransactionAmount) < 0) {
        throw new InsufficientFundsException("Insufficient "+BASE_ASSET+" balance for purchase.");
      }

      baseAsset.setSize(baseAsset.getSize().subtract(totalTransactionAmount));
      baseAsset.setUsableSize(baseAsset.getUsableSize().subtract(totalTransactionAmount));

      Asset boughtAsset = assetRepository.findByCustomerIdAndAssetName(order.getCustomer().getId(), order.getAssetName());
      if (boughtAsset == null) {
        boughtAsset = new Asset();
        boughtAsset.setCustomer(order.getCustomer());
        boughtAsset.setAssetName(order.getAssetName());
        boughtAsset.setSize(order.getSize());
        boughtAsset.setUsableSize(order.getSize());
        boughtAsset.setCreatedAt(new Timestamp(System.currentTimeMillis()));
      } else {
        boughtAsset.setSize(boughtAsset.getSize().add(order.getSize()));
        boughtAsset.setUsableSize(boughtAsset.getUsableSize().add(order.getSize()));
        boughtAsset.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
      }
      assetRepository.save(boughtAsset);
    }

    // Save the updated "TRY" asset
    baseAsset.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    assetRepository.save(baseAsset);
  }
}

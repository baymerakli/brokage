package com.firm.brokage.service.demo.services;

import com.firm.brokage.service.demo.entities.Asset;
import com.firm.brokage.service.demo.entities.Customer;
import com.firm.brokage.service.demo.entities.Transaction;
import com.firm.brokage.service.demo.enumaration.TransactionType;
import com.firm.brokage.service.demo.exceptions.CustomerNotFoundException;
import com.firm.brokage.service.demo.exceptions.InsufficientFundsException;
import com.firm.brokage.service.demo.repository.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.firm.brokage.service.demo.common.APICommonUtilConstant.BASE_ASSET;

@Log4j2
@Service
public class CustomerService {

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

  public void depositMoney(Long customerId, BigDecimal amount) {
    Optional<Customer> customer = customerRepository.findById(customerId);
    if(customer.isEmpty()) {
      throw new CustomerNotFoundException();
    }

    Asset baseAsset = assetRepository.findByCustomerIdAndAssetName(customerId, BASE_ASSET);
    if (baseAsset == null) {
      baseAsset = new Asset();
      baseAsset.setCustomer(customer.get());
      baseAsset.setAssetName(BASE_ASSET);
      baseAsset.setSize(BigDecimal.ZERO);
      baseAsset.setUsableSize(BigDecimal.ZERO);
      baseAsset.setCreatedAt(new Timestamp(System.currentTimeMillis()));
    }
    baseAsset.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    baseAsset.setSize(baseAsset.getSize().add(amount));
    baseAsset.setUsableSize(baseAsset.getUsableSize().add(amount));
    assetRepository.save(baseAsset);

    createTransaction(customer.get(), TransactionType.DEPOSIT, amount);
  }

  public void withdrawMoney(Long customerId, BigDecimal amount) {
    Optional<Customer> customer = customerRepository.findById(customerId);
    if(customer.isEmpty()) {
      throw new CustomerNotFoundException();
    }

    Asset baseAsset = assetRepository.findByCustomerIdAndAssetName(customerId, "TRY");
    if (baseAsset != null && baseAsset.getUsableSize().compareTo(amount) >= 0) {
      baseAsset.setSize(baseAsset.getSize().subtract(amount));
      baseAsset.setUsableSize(baseAsset.getUsableSize().subtract(amount));
      baseAsset.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
      assetRepository.save(baseAsset);

      createTransaction(customer.get(), TransactionType.WITHDRAW, amount);
    } else {
      throw new InsufficientFundsException();
    }
  }

  private void createTransaction(Customer customer, TransactionType type, BigDecimal amount) {
    Transaction transaction = new Transaction();
    transaction.setCustomer(customer);
    transaction.setTransactionType(type);
    transaction.setAmount(amount);
    transaction.setCreatedAt(new Timestamp(System.currentTimeMillis()));
    transactionRepository.save(transaction);
  }

  public List<Asset> listAssetsForCustomer(Long customerId) {
    return assetRepository.findByCustomerId(customerId);
  }
}

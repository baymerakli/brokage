package com.firm.brokage.service.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.firm.brokage.service.demo.entities.Asset;
import com.firm.brokage.service.demo.entities.Customer;
import com.firm.brokage.service.demo.entities.Transaction;
import com.firm.brokage.service.demo.exceptions.CustomerNotFoundException;
import com.firm.brokage.service.demo.repository.AssetRepository;
import com.firm.brokage.service.demo.repository.CustomerRepository;
import com.firm.brokage.service.demo.repository.TransactionRepository;
import com.firm.brokage.service.demo.services.CustomerService;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CustomerServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private Asset asset;

    @BeforeEach
    public void setup() {
        customer = new Customer();
        customer.setId(1L);
        asset = new Asset();
        asset.setCustomer(customer);
        asset.setAssetName("TRY");
        asset.setSize(BigDecimal.valueOf(1000));
        asset.setUsableSize(BigDecimal.valueOf(1000));
        asset.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        asset.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    }
    @Test
    public void depositMoney_Success() {
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customer));
        when(assetRepository.findByCustomerIdAndAssetName(any(Long.class), any(String.class))).thenReturn(asset);

        customerService.depositMoney(1L, BigDecimal.valueOf(500));

        verify(assetRepository, times(1)).save(any(Asset.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void depositMoney_CustomerNotFound() {
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.depositMoney(1L, BigDecimal.valueOf(500)));
    }

    @Test
    public void withdrawMoney_Success() {
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customer));
        when(assetRepository.findByCustomerIdAndAssetName(any(Long.class), any(String.class))).thenReturn(asset);

        customerService.withdrawMoney(1L, BigDecimal.valueOf(500));

        verify(assetRepository, times(1)).save(any(Asset.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
    @Test
    public void listAssetsForCustomer_Success() {
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customer));
        when(assetRepository.findByCustomerId(any(Long.class))).thenReturn(Collections.singletonList(asset));

        var result = customerService.listAssetsForCustomer(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(asset, result.get(0));
    }
}

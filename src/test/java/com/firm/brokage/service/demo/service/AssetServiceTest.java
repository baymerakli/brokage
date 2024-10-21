package com.firm.brokage.service.demo.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.firm.brokage.service.demo.entities.Asset;
import com.firm.brokage.service.demo.entities.Customer;
import com.firm.brokage.service.demo.entities.Order;
import com.firm.brokage.service.demo.enumaration.OrderSide;
import com.firm.brokage.service.demo.exceptions.InsufficientFundsException;
import com.firm.brokage.service.demo.repository.AssetRepository;
import com.firm.brokage.service.demo.repository.CustomerRepository;
import com.firm.brokage.service.demo.repository.TransactionRepository;
import com.firm.brokage.service.demo.services.AssetService;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.firm.brokage.service.demo.util.BrokageTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AssetService assetService;

    private Customer customer;

    @BeforeEach
    public void setup() {
        customer = new Customer();
        customer.setId(1L);
    }

    @Test
    @DisplayName("Successfully update assets for SELL order")
    public void updateAssetWithOrderMatch_Sell_Success() {
        Customer customer = new Customer(); // Set customer details as needed
        Asset baseAsset = BrokageTestUtil.createBaseAsset(customer);
        Asset assetToSell = BrokageTestUtil.createSellAsset(customer);
        Order sellOrder = BrokageTestUtil.createSellOrder(customer);

        when(assetRepository.findByCustomerIdAndAssetName(customer.getId(), "TRY")).thenReturn(baseAsset);
        when(assetRepository.findByCustomerIdAndAssetName(customer.getId(), "USD")).thenReturn(assetToSell);

        assetService.updateAssetWithOrderMatch(sellOrder);

        assertEquals(new BigDecimal("3000"), baseAsset.getSize());
        assertEquals(new BigDecimal("2500"), baseAsset.getUsableSize());
        assertEquals(new BigDecimal("10"), assetToSell.getSize());
        assertEquals(new BigDecimal("5"), assetToSell.getUsableSize());
    }


    @Test
    @DisplayName("Successfully update assets for BUY order")
    public void updateAssetWithOrderMatch_Buy_Success() {
        Order order = BrokageTestUtil.createSellOrder(customer);
        Asset baseAsset = BrokageTestUtil.createBaseAsset(customer);

        order.setOrderSide(OrderSide.BUY);
        when(assetRepository.findByCustomerIdAndAssetName(customer.getId(), "TRY")).thenReturn(baseAsset);
        when(assetRepository.findByCustomerIdAndAssetName(customer.getId(), order.getAssetName())).thenReturn(null);

        assetService.updateAssetWithOrderMatch(order);

        assertEquals(new BigDecimal("1000"), baseAsset.getSize());
        assertEquals(new BigDecimal("500"), baseAsset.getUsableSize());
    }

    @Test
    @DisplayName("Fail to update assets for BUY order due to insufficient funds")
    public void updateAssetWithOrderMatch_Buy_InsufficientFunds() {
        Order order = BrokageTestUtil.createSellOrder(customer);
        Asset baseAsset = BrokageTestUtil.createBaseAsset(customer);

        order.setOrderSide(OrderSide.BUY);
        order.setSize(new BigDecimal("20"));
        when(assetRepository.findByCustomerIdAndAssetName(customer.getId(), "TRY")).thenReturn(baseAsset);

        Exception exception = assertThrows(InsufficientFundsException.class, () -> assetService.updateAssetWithOrderMatch(order));
        assertTrue(exception.getMessage().contains("Insufficient TRY balance for purchase"));
    }

    @Test
    @DisplayName("Fail to update assets for SELL order due to insufficient asset quantity")
    public void updateAssetWithOrderMatch_Sell_InsufficientQuantity() {
        Order order = BrokageTestUtil.createSellOrder(customer);
        Asset assetToSell = BrokageTestUtil.createSellAsset(customer);

        order.setSize(new BigDecimal("20"));
        when(assetRepository.findByCustomerIdAndAssetName(customer.getId(), "USD")).thenReturn(assetToSell);

        Exception exception = assertThrows(InsufficientFundsException.class, () -> assetService.updateAssetWithOrderMatch(order));
        assertTrue(exception.getMessage().contains("Customer does not have a TRY asset."));
    }
}

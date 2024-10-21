package com.firm.brokage.service.demo.util;

import com.firm.brokage.service.demo.api.OrderRequest;
import com.firm.brokage.service.demo.entities.Asset;
import com.firm.brokage.service.demo.entities.Customer;
import com.firm.brokage.service.demo.entities.Order;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.firm.brokage.service.demo.enumaration.OrderSide;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class BrokageTestUtil {
  public static Order createOrder(Long id, String assetName, BigDecimal size){
    Order order = new Order();
    order.setId(id);
    order.setAssetName(assetName);
    order.setSize(size);
    return order;
  }

  public static Order createSellOrder(Customer customer){
    Order order = new Order();
    order.setCustomer(customer);
    order.setAssetName("USD");
    order.setOrderSide(OrderSide.SELL);
    order.setSize(new BigDecimal("10"));
    order.setPrice(new BigDecimal("100"));
    return order;
  }

  public static OrderRequest createOrderRequest(Long customerId, String assetName, OrderRequest.SideEnum side, Integer size, BigDecimal price) {
    OrderRequest orderRequest = new OrderRequest();
    orderRequest.setCustomerId(customerId);
    orderRequest.setAssetName(assetName);
    orderRequest.setSide(side);
    orderRequest.setSize(size);
    orderRequest.setPrice(price);
    return orderRequest;
  }

  public static void generateBasicValidRequestDetails(MockHttpServletRequestBuilder builder) {
    builder.contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON);
  }

  public static Asset createBaseAsset(Customer customer){
    Asset baseAsset = new Asset();
    baseAsset.setCustomer(customer);
    baseAsset.setAssetName("TRY");
    baseAsset.setSize(new BigDecimal("2000"));
    baseAsset.setUsableSize(new BigDecimal("1500"));
    baseAsset.setCreatedAt(new Timestamp(System.currentTimeMillis()));
    baseAsset.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    return baseAsset;
  }

  public static Asset createSellAsset(Customer customer){
    Asset assetToSell = new Asset();
    assetToSell.setCustomer(customer);
    assetToSell.setAssetName("USD");
    assetToSell.setSize(new BigDecimal("20"));
    assetToSell.setUsableSize(new BigDecimal("15"));
    assetToSell.setCreatedAt(new Timestamp(System.currentTimeMillis()));
    assetToSell.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    return assetToSell;
  }
}

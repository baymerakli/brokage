package com.firm.brokage.service.demo.util;

import com.firm.brokage.service.demo.api.OrderRequest;
import com.firm.brokage.service.demo.entities.Order;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class BrokageTestUtil {
  public static Order createOrder(Long id, String assetName, BigDecimal size){
    Order order = new Order();
    order.setId(id);
    order.setAssetName(assetName);
    order.setSize(size);
    return order;
  }

  public static OrderRequest createOrderRequest(String assetName){
    OrderRequest orderRequest = new OrderRequest();
    orderRequest.setCustomerId(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE));
    orderRequest.setAssetName(assetName);
    return orderRequest;
  }

  public static void generateBasicValidRequestDetails(MockHttpServletRequestBuilder builder) {
    builder.contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON);
  }
}

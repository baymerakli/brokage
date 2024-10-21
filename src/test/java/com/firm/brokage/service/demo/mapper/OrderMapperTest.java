package com.firm.brokage.service.demo.mapper;

import com.firm.brokage.service.demo.api.OrderRequest;
import com.firm.brokage.service.demo.api.OrderResponse;
import com.firm.brokage.service.demo.entities.Order;
import com.firm.brokage.service.demo.mappers.OrderMapper;
import com.firm.brokage.service.demo.mappers.OrderMapperImpl;
import com.firm.brokage.service.demo.util.BrokageTestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {OrderMapperImpl.class})
public class OrderMapperTest {

  @Autowired
  OrderMapper mapper;

  @Test
  @DisplayName("Test that Order is correctly mapped to OrderResponse")
  public void orderToOrderResponse_success() {
    Order order = BrokageTestUtil.createOrder(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE),"USDTRY", BigDecimal.ONE);

    OrderResponse orderResponse = mapper
        .orderToOrderResponse(order);

    assertEquals(orderResponse.getOrderId(), order.getId());
    assertEquals(orderResponse.getAssetName(), order.getAssetName());
  }
  @Test
  @DisplayName("Test that OrderRequest is correctly mapped to Order")
  public void createOrderRequestToOrder_success() {
    OrderRequest orderRequest = BrokageTestUtil.createOrderRequest("USDTRY");

    Order order = mapper
        .createOrderRequestToOrder(orderRequest);

    assertEquals(order.getAssetName(), orderRequest.getAssetName());
  }
}

package com.firm.brokage.service.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firm.brokage.service.demo.api.OrderRequest;
import com.firm.brokage.service.demo.common.APIPathConstant;
import com.firm.brokage.service.demo.entities.Order;
import com.firm.brokage.service.demo.services.OrderService;
import com.firm.brokage.service.demo.util.BrokageTestUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CreateOrdersControllerTest {

  private ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private MockMvc mvc;

  @MockBean
  OrderService orderService;

  private List<Order> orders;

  @BeforeEach
  public void setup() {
    orders = new ArrayList<>();
    orders.add(BrokageTestUtil.createOrder(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE), "USD", BigDecimal.ONE));
    orders.add(BrokageTestUtil.createOrder(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE), "USD", BigDecimal.ONE));
  }

  @AfterEach
  public void tearDown() {
    orders = null;
  }

  @Test
  @DisplayName("Create Order - HTTP 200")
  public void createOrder_http_200() throws Exception {
    OrderRequest orderRequest = BrokageTestUtil.createOrderRequest(1L, "USD", OrderRequest.SideEnum.BUY, 10, BigDecimal.valueOf(100));
    Order order = BrokageTestUtil.createOrder(1L, "USD", BigDecimal.TEN);

    doReturn(order).when(orderService).createOrder(any(OrderRequest.class));

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(APIPathConstant.ORDERS_BASE_URL)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(orderRequest))
            .with(user("admin").password("password").roles("ADMIN"));

    mvc.perform(builder)
            .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Create Order - Invalid Data - HTTP 400")
  public void createOrder_invalidData_http_400() throws Exception {
    OrderRequest invalidOrderRequest = new OrderRequest();

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(APIPathConstant.ORDERS_BASE_URL)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(invalidOrderRequest))
            .with(user("admin").password("password").roles("ADMIN"));

    mvc.perform(builder)
            .andExpect(status().isBadRequest());
  }

}

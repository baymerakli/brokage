package com.firm.brokage.service.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firm.brokage.service.demo.common.APIPathConstant;
import com.firm.brokage.service.demo.entities.Order;
import com.firm.brokage.service.demo.services.OrderService;
import com.firm.brokage.service.demo.util.BrokageTestUtil;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GetOrdersControllerTest {

  private ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private MockMvc mvc;

  @MockBean
  OrderService orderService;

  private List<Order> orders;

  @BeforeEach
  public void setup() {
    orders = new ArrayList<>();
    orders.add(BrokageTestUtil.createOrder(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE), "USDTRY", BigDecimal.ONE));
    orders.add(BrokageTestUtil.createOrder(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE), "USDTRY", BigDecimal.ONE));
  }

  @AfterEach
  public void tearDown() {
    orders = null;
  }

  @Test
  @DisplayName("Test that GET /orders/ returns 200")
  public void getAllOrders_http_200() throws Exception {
    doReturn(orders).when(orderService).getOrders(any(Long.class), any(), any());

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(APIPathConstant.ORDERS_BASE_URL)
            .with(user("admin").password("password").roles("ADMIN"));

    BrokageTestUtil.generateBasicValidRequestDetails(builder);

    mvc.perform(builder)
            .andExpect(status().isOk()); // Using isOk for 200 status code
  }

  @Test
  @DisplayName("Test that GET /orders/ returns 200")
  public void getOrdersForACustomer_http_200() throws Exception {
    doReturn(orders).when(orderService).getOrders(any(Long.class), any(), any());

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(APIPathConstant.ORDERS_BASE_URL)
            .param("customerId", "1")
            .param("startDate", "2023-01-01")
            .param("endDate", "2023-01-31")
            .with(user("admin").password("password").roles("ADMIN"));

    BrokageTestUtil.generateBasicValidRequestDetails(builder);

    mvc.perform(builder)
            .andExpect(status().isOk());
  }
}

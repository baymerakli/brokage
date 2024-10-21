package com.firm.brokage.service.demo.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.firm.brokage.service.demo.common.APIPathConstant;
import com.firm.brokage.service.demo.entities.Order;
import com.firm.brokage.service.demo.exceptions.OrderNotFoundException;
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
public class CancelOrdersControllerTest {

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
  @DisplayName("Cancel Order - HTTP 204")
  public void cancelOrder_http_204() throws Exception {
    Long orderId = 1L;
    doNothing().when(orderService).cancelOrder(orderId);

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete(APIPathConstant.DELETE_ORDER_URL.replace("{" + APIPathConstant.ORDER_REF_ID + "}", orderId.toString()))
            .with(user("admin").password("password").roles("ADMIN"));

    mvc.perform(builder)
            .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("Cancel Order - Order Not Found - HTTP 404")
  public void cancelOrder_orderNotFound_http_404() throws Exception {
    Long orderId = 2L;
    doThrow(new OrderNotFoundException()).when(orderService).cancelOrder(orderId);

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete(APIPathConstant.DELETE_ORDER_URL.replace("{" + APIPathConstant.ORDER_REF_ID + "}", orderId.toString()))
            .with(user("admin").password("password").roles("ADMIN"));

    mvc.perform(builder)
            .andExpect(status().isNotFound());
  }
}

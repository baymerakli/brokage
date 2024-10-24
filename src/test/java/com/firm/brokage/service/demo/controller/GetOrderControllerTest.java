package com.firm.brokage.service.demo.controller;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.firm.brokage.service.demo.common.APIPathConstant;
import com.firm.brokage.service.demo.entities.Order;
import com.firm.brokage.service.demo.exceptions.OrderNotFoundException;
import com.firm.brokage.service.demo.services.OrderService;
import com.firm.brokage.service.demo.util.BrokageTestUtil;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GetOrderControllerTest {
  @Autowired
  private MockMvc mvc;

  @MockBean
  OrderService orderService;

  private Order order;

  @BeforeEach
  public void setup() {
    order = BrokageTestUtil.createOrder(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE), "USD", BigDecimal.ONE);
  }

  @AfterEach
  public void tearDown() {
    order = null;
  }

  @Test
  @DisplayName("Test that GET /orders/{id} returns 200")
  public void getOrder_http_200() throws Exception {
    doReturn(order).when(orderService).getOrder(order.getId());
    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(APIPathConstant.GET_ORDER_URL.replace("{" + APIPathConstant.ORDER_REF_ID + "}", order.getId().toString()))
            .with(user("admin").password("password").roles("ADMIN"));

    mvc.perform(builder)
            .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Test that GET /orders/{id} returns 404 for invalid order ID")
  public void getOrder_http_404() throws Exception {
    Long randomId = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
    doThrow(new OrderNotFoundException()).when(orderService).getOrder(randomId);
    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(APIPathConstant.GET_ORDER_URL.replace("{" + APIPathConstant.ORDER_REF_ID + "}", randomId.toString()))
            .with(user("admin").password("password").roles("ADMIN"));

    mvc.perform(builder)
            .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Test that GET /orders/{id} returns 403 for unauthorized access")
  @WithMockUser(username = "user", roles = {"USER"})
  public void getOrder_http_403() throws Exception {
    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(APIPathConstant.GET_ORDER_URL.replace("{" + APIPathConstant.ORDER_REF_ID + "}", order.getId().toString()));

    mvc.perform(builder)
            .andExpect(status().isForbidden());
  }
}

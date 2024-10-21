package com.firm.brokage.service.demo.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.firm.brokage.service.demo.common.APIPathConstant;
import com.firm.brokage.service.demo.entities.Order;
import com.firm.brokage.service.demo.exceptions.OrderCannotBeMatchedException;
import com.firm.brokage.service.demo.exceptions.OrderNotFoundException;
import com.firm.brokage.service.demo.services.OrderService;
import java.util.List;
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
public class MatchOrdersControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  OrderService orderService;

  private List<Order> orders;
  @Test
  @DisplayName("Successfully match an order")
  public void matchOrder_Success() throws Exception {
    Long orderId = 1L;
    doNothing().when(orderService).matchOrder(orderId);

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(APIPathConstant.MATCH_ORDER_URL.replace("{" + APIPathConstant.ORDER_REF_ID + "}", orderId.toString()))
            .with(user("admin").password("password").roles("ADMIN"));

    mvc.perform(builder)
            .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("Attempt to match a non-existing order")
  public void matchOrder_OrderNotFound() throws Exception {
    Long orderId = 2L;
    doThrow(new OrderNotFoundException()).when(orderService).matchOrder(orderId);

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(APIPathConstant.MATCH_ORDER_URL.replace("{" + APIPathConstant.ORDER_REF_ID + "}", orderId.toString()))
            .with(user("admin").password("password").roles("ADMIN"));

    mvc.perform(builder)
            .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Attempt to match an order that cannot be matched")
  public void matchOrder_CannotBeMatched() throws Exception {
    Long orderId = 3L;
    doThrow(new OrderCannotBeMatchedException()).when(orderService).matchOrder(orderId);

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(APIPathConstant.MATCH_ORDER_URL.replace("{" + APIPathConstant.ORDER_REF_ID + "}", orderId.toString()))
            .with(user("admin").password("password").roles("ADMIN"));

    mvc.perform(builder)
            .andExpect(status().isBadRequest());
  }
}

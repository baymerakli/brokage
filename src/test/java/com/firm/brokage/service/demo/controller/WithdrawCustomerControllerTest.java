package com.firm.brokage.service.demo.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firm.brokage.service.demo.api.WithdrawRequest;
import com.firm.brokage.service.demo.common.APIPathConstant;
import com.firm.brokage.service.demo.exceptions.InsufficientFundsException;
import com.firm.brokage.service.demo.services.CustomerService;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class WithdrawCustomerControllerTest {

  @MockBean
  private CustomerService customerService;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("Withdraw Money - HTTP 200")
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void withdrawMoney_Success() throws Exception {
    Long customerId = 1L;
    WithdrawRequest request = new WithdrawRequest();
    request.setAmount(new BigDecimal("100.00"));

    doNothing().when(customerService).withdrawMoney(customerId, request.getAmount());

    mockMvc.perform(post(APIPathConstant.CUSTOMER_WITHDRAW_URL.replace("{" + APIPathConstant.CUSTOMER_REF_ID + "}", customerId.toString()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  @DisplayName("Withdraw Money - HTTP 200")
  public void withdrawMoney_InsufficientFunds() throws Exception {
    Long customerId = 1L;
    WithdrawRequest request = new WithdrawRequest();
    request.setAmount(new BigDecimal("100.00"));

    doThrow(new InsufficientFundsException("Insufficient funds for withdrawal."))
        .when(customerService)
        .withdrawMoney(eq(customerId), any(BigDecimal.class));

    mockMvc.perform(post(APIPathConstant.CUSTOMER_WITHDRAW_URL.replace("{" + APIPathConstant.CUSTOMER_REF_ID + "}", customerId.toString()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
  }

}

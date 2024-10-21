package com.firm.brokage.service.demo.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firm.brokage.service.demo.api.AssetResponse;
import com.firm.brokage.service.demo.common.APIPathConstant;
import com.firm.brokage.service.demo.entities.Asset;
import com.firm.brokage.service.demo.services.CustomerService;
import java.util.List;
import java.util.stream.Collectors;
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
public class ListAssetsCustomerControllerTest {

  @MockBean
  private CustomerService customerService;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("List Assets - HTTP 200")
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void listAssets_Success() throws Exception {
    Long customerId = 1L;
    List<Asset> assets = List.of(new Asset()); // Simplified for example
    List<AssetResponse> responses = assets.stream()
            .map(asset -> new AssetResponse(/* parameters */))
            .collect(Collectors.toList());

    when(customerService.listAssetsForCustomer(customerId)).thenReturn(assets);

    mockMvc.perform(get(APIPathConstant.ASSETS_LIST_URL)
                    .param(APIPathConstant.CUSTOMER_REF_ID, customerId.toString())
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
  }


}

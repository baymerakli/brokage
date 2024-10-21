package com.firm.brokage.service.demo.controllers;

import com.firm.brokage.service.demo.api.*;
import com.firm.brokage.service.demo.entities.Asset;
import com.firm.brokage.service.demo.mappers.AssetMapper;
import com.firm.brokage.service.demo.mappers.OrderMapper;
import com.firm.brokage.service.demo.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.firm.brokage.service.demo.common.APICommonUtilConstant.DEFAULT_CONTENT_TYPE;
import static com.firm.brokage.service.demo.common.APIPathConstant.*;

@RestController
public class CustomerController {

  @Autowired
  CustomerService customerService;

  @Autowired
  AssetMapper mapper;

  @PostMapping(value = CUSTOMER_WITHDRAW_URL, produces = {DEFAULT_CONTENT_TYPE})
  public ResponseEntity<?> withdrawMoney(@PathVariable(CUSTOMER_REF_ID) Long customerId,
                                         @Valid @RequestBody WithdrawRequest withdrawRequest) {
    customerService.withdrawMoney(customerId, withdrawRequest.getAmount());
    return ResponseEntity.ok().build();
  }

  @PostMapping(value = "CUSTOMER_DEPOSIT_URL", produces = {DEFAULT_CONTENT_TYPE})
  public ResponseEntity<?> depositMoney(@PathVariable(CUSTOMER_REF_ID) Long customerId,
                                        @Valid @RequestBody DepositRequest depositRequest) {
    customerService.depositMoney(customerId, depositRequest.getAmount());
    return ResponseEntity.ok().build();
  }

  @GetMapping(value = ASSETS_LIST_URL, produces = {DEFAULT_CONTENT_TYPE})
  public ResponseEntity<List<AssetResponse>> listAssets(@RequestParam(CUSTOMER_REF_ID) Long customerId) {
    List<Asset> assets = customerService.listAssetsForCustomer(customerId);
    List<AssetResponse> assetResponses = assets.stream()
            .map(asset -> mapper.assetToAssetResponse(asset))
            .collect(Collectors.toList());
    return ResponseEntity.ok(assetResponses);
  }

}

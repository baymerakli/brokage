package com.firm.brokage.service.demo.controllers.admin;

import static com.firm.brokage.service.demo.common.APICommonUtilConstant.DEFAULT_CONTENT_TYPE;
import static com.firm.brokage.service.demo.common.APIPathConstant.*;

import com.firm.brokage.service.demo.api.OrderRequest;
import com.firm.brokage.service.demo.api.OrderResponse;
import com.firm.brokage.service.demo.mappers.OrderMapper;
import com.firm.brokage.service.demo.services.OrderService;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

  @Autowired
  OrderService orderService;

  @Autowired
  OrderMapper mapper;

  @GetMapping(value = GET_ORDER_URL, produces = {DEFAULT_CONTENT_TYPE})
  public OrderResponse getOrder(@PathVariable(ORDER_REF_ID) Long orderId) {
    return mapper.orderToOrderResponse(orderService.getOrder(orderId));
  }

  @GetMapping(value = ORDERS_BASE_URL, produces = {DEFAULT_CONTENT_TYPE})
  public List<OrderResponse> getOrders(@RequestParam(required = false) Long customerId,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
    return orderService.findOrdersByCriteria(customerId, startDate, endDate).stream()
            .map(mapper::orderToOrderResponse)
            .collect(Collectors.toList());
  }

  @PostMapping(value = ORDERS_BASE_URL, produces = {DEFAULT_CONTENT_TYPE})
  public OrderResponse createOrder(@Valid @RequestBody OrderRequest orderRequest) {
    return mapper.orderToOrderResponse(orderService.createOrder(orderRequest));
  }

  @PostMapping(value = MATCH_ORDER_URL, produces = {DEFAULT_CONTENT_TYPE})
  public ResponseEntity<Void> matchOrder(@PathVariable(ORDER_REF_ID) Long orderId) {
    orderService.matchOrder(orderId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping(value = DELETE_ORDER_URL, produces = {DEFAULT_CONTENT_TYPE})
  public ResponseEntity<Void> deleteOrder(@PathVariable(ORDER_REF_ID) Long orderId) {
    orderService.cancelOrder(orderId);
    return ResponseEntity.noContent().build();
  }
}

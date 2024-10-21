package com.firm.brokage.service.demo.services;

import com.firm.brokage.service.demo.api.OrderRequest;
import com.firm.brokage.service.demo.entities.Customer;
import com.firm.brokage.service.demo.entities.Order;
import com.firm.brokage.service.demo.enumaration.OrderStatus;
import com.firm.brokage.service.demo.exceptions.OrderCannotBeDeletedException;
import com.firm.brokage.service.demo.exceptions.OrderCannotBeMatchedException;
import com.firm.brokage.service.demo.exceptions.OrderNotFoundException;
import com.firm.brokage.service.demo.mappers.OrderMapper;
import com.firm.brokage.service.demo.repository.OrderRepository;
import com.firm.brokage.service.demo.repository.OrderSpecifications;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class OrderService {

  @Autowired
  OrderRepository orderRepository;

  @Autowired
  CustomerService customerService;

  @Autowired
  AssetService assetService;

  @Autowired
  OrderMapper mapper;

  public Order getOrder(Long orderId) {
    return orderRepository.findById(orderId)
            .orElseThrow(OrderNotFoundException::new);
  }

  public List<Order> getOrders(Long customerId, LocalDate startDate, LocalDate endDate) {
    return orderRepository.findOrdersByCriteria(customerId, startDate, endDate);
  }

  public Order createOrder(OrderRequest orderRequest){
    Customer customer = customerService.getCustomerById(orderRequest.getCustomerId());

    Order order = mapper.createOrderRequestToOrder(orderRequest); // Ensure your mapper is compatible with Long ID
    order.setCustomer(customer);
    return orderRepository.save(order);
  }

  public void cancelOrder(Long orderId){ // Change UUID to Long
    Optional<Order> order = orderRepository.findById(orderId);
    if(order.isEmpty()){
      throw new OrderNotFoundException();
    }

    if(!order.get().getStatus().equals(OrderStatus.PENDING)){
      throw new OrderCannotBeDeletedException();
    }

    order.get().setStatus(OrderStatus.CANCELED);
    orderRepository.save(order.get());
  }

  @Transactional
  public void matchOrder(Long orderId) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(OrderNotFoundException::new); // Simplified exception throwing

    if (!OrderStatus.PENDING.equals(order.getStatus())) {
      throw new OrderCannotBeMatchedException();
    }

    assetService.updateAssetWithOrderMatch(order);

    order.setStatus(OrderStatus.MATCHED);
    orderRepository.save(order);
  }

  public List<Order> findOrdersByCriteria(Long customerId, LocalDate startDate, LocalDate endDate) {
    Specification<Order> spec = OrderSpecifications.withDynamicQuery(customerId, startDate, endDate);
    return orderRepository.findAll(spec);
  }
}


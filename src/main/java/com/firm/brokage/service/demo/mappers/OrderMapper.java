package com.firm.brokage.service.demo.mappers;

import com.firm.brokage.service.demo.api.OrderRequest;
import com.firm.brokage.service.demo.api.OrderResponse;
import com.firm.brokage.service.demo.entities.Order;
import com.firm.brokage.service.demo.enumaration.OrderSide;
import org.mapstruct.*;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "createDate", source = "createdAt")
    public abstract OrderResponse orderToOrderResponse(Order order);

    @Mapping(target = "orderSide", source = "side")
    @Mapping(constant = "PENDING", target = "status")
    public abstract Order createOrderRequestToOrder(OrderRequest orderRequest);

    default OffsetDateTime map(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime();
    }

    @AfterMapping
    default void setCreatedAt(@MappingTarget Order order) {
        order.setCreatedAt(new Timestamp(System.currentTimeMillis()));
    }

    default OrderSide map(OrderRequest.SideEnum sideEnum) {
        if (sideEnum == null) {
            return null;
        }
        switch (sideEnum) {
            case BUY:
                return OrderSide.BUY;
            case SELL:
                return OrderSide.SELL;
            default:
                throw new IllegalArgumentException("Unknown enum value: " + sideEnum);
        }
    }
}


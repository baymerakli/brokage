package com.firm.brokage.service.demo.entities;

import com.firm.brokage.service.demo.enumaration.OrderSide;
import com.firm.brokage.service.demo.enumaration.OrderStatus;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "asset_name")
    private String assetName;

    @Column(name = "size")
    private BigDecimal size;

    @Column(name = "price")
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_side")
    private OrderSide orderSide;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;
}

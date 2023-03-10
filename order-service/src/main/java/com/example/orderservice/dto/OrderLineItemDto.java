package com.example.orderservice.dto;

import com.example.orderservice.model.OrderLineItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineItemDto {
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;

    private List<OrderLineItem> orderLineItems;
}

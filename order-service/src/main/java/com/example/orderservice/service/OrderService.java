package com.example.orderservice.service;


import com.example.orderservice.config.WebClientConfig;
import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.OrderLineItemDto;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderLineItem;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();

        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setItems(orderLineItems);

        List<String> skuCodes = order.getItems().stream().map(OrderLineItem::getSkuCode).toList();

        List<Integer> quantities = order.getItems().stream().map(OrderLineItem::getQuantity).toList();

        InventoryResponse[] inventoryResponses = webClientBuilder.build()
                .get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes)
                                .queryParam("quantity",quantities)
                                .build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean stockCheck = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::getIsInStock);

        if (!stockCheck)
            throw new IllegalArgumentException("Product is not in stock...");

        orderRepository.save(order);
    }

    private OrderLineItem mapToDto(OrderLineItemDto orderLineItemDto) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setPrice(orderLineItemDto.getPrice());
        orderLineItem.setSkuCode(orderLineItemDto.getSkuCode());
        orderLineItem.setQuantity(orderLineItemDto.getQuantity());
        orderLineItem.setOrderLineItems(orderLineItem.getOrderLineItems());

        return orderLineItem;

    }
}

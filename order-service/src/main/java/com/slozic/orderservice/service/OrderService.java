package com.slozic.orderservice.service;

import com.slozic.orderservice.dto.InventoryResponse;
import com.slozic.orderservice.dto.OrderLineItemsDto;
import com.slozic.orderservice.dto.OrderRequest;
import com.slozic.orderservice.event.OrderPlacedEvent;
import com.slozic.orderservice.model.Order;
import com.slozic.orderservice.model.OrderLineItems;
import com.slozic.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {
    @Value("${inventory.service.url:default}")
    private String API_INVENTORY;
    @Value("${inventory.service.port:default}")
    private String INVENTORY_PORT;
    private final OrderRepository orderRepository;
    private final WebClient webClient;
    private final ApplicationEventPublisher applicationEventPublisher;

    public String placeOrder(final OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        InventoryResponse[] inventoryResponseArray = webClient.get()
                .uri(API_INVENTORY,
                        uriBuilder -> uriBuilder.port(INVENTORY_PORT).
                                queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                .allMatch(InventoryResponse::isInStock);

        if (allProductsInStock && inventoryResponseArray.length > 0) {
            orderRepository.save(order);
            // publish Order Placed Event
            applicationEventPublisher.publishEvent(new OrderPlacedEvent(this, order.getOrderNumber()));
            return "Order Placed";
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }

    public Optional<Order> fetchOrder(Long id) {
        return orderRepository.findById(id);
    }
}

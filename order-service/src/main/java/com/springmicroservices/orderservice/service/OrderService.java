package com.springmicroservices.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.springmicroservices.orderservice.dto.InventoryResponse;
import com.springmicroservices.orderservice.dto.OrderLineItemsDto;
import com.springmicroservices.orderservice.dto.OrderRequest;
import com.springmicroservices.orderservice.events.OrderPlacedEvent;
import com.springmicroservices.orderservice.model.Order;
import com.springmicroservices.orderservice.model.OrderLineItems;
import com.springmicroservices.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    @Autowired
    private  WebClient.Builder webclientBuilder;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkatemplate;
    public boolean placeOrder(OrderRequest orderRequest)   {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);
        
        List<String> skucode = orderRequest.getOrderLineItemsDtoList().stream().map(orderlineitem -> orderlineitem.getSkuCode()).toList();
        //call the inventory service to check if the product is in stock
        InventoryResponse[] inventoryResponsArray = webclientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skucode).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allinstock  = Arrays.stream(inventoryResponsArray).allMatch(inventoryres -> inventoryres.isInStock());
        if(allinstock) {
        	orderRepository.save(order);
        	kafkatemplate.send("notificationTopic" , new OrderPlacedEvent(order.getOrderNumber()));
        	return true;
        	
        }
        
        //have to add custom exception
        else {
        	 throw new IllegalArgumentException("Please Try again Later...");
        }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}

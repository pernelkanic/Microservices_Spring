package com.springmicroservices.orderservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.springmicroservices.orderservice.dto.OrderRequest;
import com.springmicroservices.orderservice.service.OrderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="inventory" ,fallbackMethod= "fallback")
    public String placeOrder(@RequestBody OrderRequest orderRequest) {
        boolean res = orderService.placeOrder(orderRequest);
        if(res) {
        	return "Order Placed Successfully";
        }
        else {
        	return "Order not placed...";
        }
    }
    public String fallback( OrderRequest orderRequest , RuntimeException runtimeexception) {
    	return "The Service is not working properly..check it out!";
    }
}

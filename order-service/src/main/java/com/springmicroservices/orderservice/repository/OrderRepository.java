package com.springmicroservices.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springmicroservices.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

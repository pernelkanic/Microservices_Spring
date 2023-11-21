	package com.springmicroservices.inventoryservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import com.springmicroservices.inventoryservice.model.Inventory;
import com.springmicroservices.inventoryservice.repository.InventoryRepository;

@SpringBootApplication
@EnableEurekaClient
public class InventoryServiceApplication  {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }
  
    }


package com.springmicroservices.inventoryservice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springmicroservices.inventoryservice.DTO.InventoryResponse;
import com.springmicroservices.inventoryservice.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
        		.map(inventory -> 
        			InventoryResponse.builder()
        					.skucode(inventory.getSkuCode())
        					.isInStock(inventory.getQuantity() > 0 )
        					.build()
        		).toList();
    }
}

package com.example.inventoryservice.service;


import com.example.inventoryservice.dto.InventoryRequest;
import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory -> InventoryResponse.builder().skuCode(inventory.getSkuCode())
                        .isInStock(inventory.getQuantity() > 0).build()).toList();
    }

    public InventoryResponse createInventory(InventoryRequest inventoryRequest){
        Inventory inventory = new Inventory();
        inventory.setSkuCode(inventoryRequest.getSkuCode());
        inventory.setQuantity(inventoryRequest.getQuantity());

        inventoryRepository.save(inventory);

        return InventoryResponse.builder()
                .skuCode(inventoryRequest.getSkuCode())
                .quantity(inventoryRequest.getQuantity())
                .build();
    }

    public List<InventoryResponse> getInventory(){
        return inventoryRepository.findAll().stream().map(inventory ->
                        InventoryResponse.builder()
                        .quantity(inventory.getQuantity())
                .skuCode(inventory.getSkuCode())
                .build())
                .toList();
    }

    public void deleteAll(){
        inventoryRepository.deleteAll();
    }
}

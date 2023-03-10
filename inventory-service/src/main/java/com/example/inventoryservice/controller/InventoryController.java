package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.InventoryRequest;
import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode ) {
        return inventoryService.isInStock(skuCode);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponse createInventory(@RequestBody InventoryRequest inventoryRequest){
        return inventoryService.createInventory(inventoryRequest);
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> getInventory(){
        return inventoryService.getInventory();
    }

    @DeleteMapping
    public void deleteInventory(){
        inventoryService.deleteAll();
    }

}

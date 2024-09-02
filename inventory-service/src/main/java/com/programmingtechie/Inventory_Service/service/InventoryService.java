package com.programmingtechie.Inventory_Service.service;



import com.programmingtechie.Inventory_Service.model.Inventory;
import com.programmingtechie.Inventory_Service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public Optional<Inventory> isInStock(String skuCode){
        return inventoryRepository.findById(Long.valueOf(skuCode));
    }
}
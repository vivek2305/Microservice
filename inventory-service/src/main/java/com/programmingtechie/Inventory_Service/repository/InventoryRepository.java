package com.programmingtechie.Inventory_Service.repository;


import com.programmingtechie.Inventory_Service.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

}
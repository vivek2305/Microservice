package com.programmingtechie.Order_Service.repository;


import com.programmingtechie.Order_Service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
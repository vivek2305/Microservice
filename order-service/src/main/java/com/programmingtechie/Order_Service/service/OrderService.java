package com.programmingtechie.Order_Service.service;

import com.programmingtechie.Order_Service.dto.InventoryResponse;
import com.programmingtechie.Order_Service.dto.OrderLineItemsDto;
import com.programmingtechie.Order_Service.dto.OrderRequest;
import com.programmingtechie.Order_Service.model.Order;
import com.programmingtechie.Order_Service.model.OrderLineItems;
import com.programmingtechie.Order_Service.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest){
        Order order= new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> OrderLineItems= orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItemsList(OrderLineItems);
        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(com.programmingtechie.Order_Service.model.OrderLineItems::getSkuCode)
                .toList();
        //Call to Inventory
        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://Inventory-Service/api/inventory"
                ,uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
        boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                .allMatch(InventoryResponse::isInStock);
        if(allProductsInStock){
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in stock");
        }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems=new OrderLineItems();
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;

    }
}

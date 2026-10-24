package com.mod.creatorstore.controller;

import com.mod.creatorstore.dto.OrderRequest;
import com.mod.creatorstore.entities.Order;
import com.mod.creatorstore.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private  final OrderService orderService;

    @PostMapping
    public Order createOrder(@Valid @RequestBody OrderRequest orderRequest){

        return orderService.createOrder(orderRequest);
    }

    @GetMapping("/{id}")
    public Order getOrderByid(@PathVariable  Long id){

        return orderService.getOrderByid(id);
    }

    @GetMapping
    public List<Order> getAllOrders(){

        return orderService.getAllOrder();
    }

    @DeleteMapping("/{id}")
    public boolean deleteOrder(@PathVariable Long id){
        return orderService.deleteOrder(id);
    }
}

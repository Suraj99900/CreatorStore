package com.mod.creatorstore.service;

import com.mod.creatorstore.dto.OrderItemRequest;
import com.mod.creatorstore.dto.OrderRequest;
import com.mod.creatorstore.entities.Order;
import com.mod.creatorstore.entities.OrderItem;
import com.mod.creatorstore.entities.Product;
import com.mod.creatorstore.repository.OrderRepository;
import com.mod.creatorstore.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService  {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order createOrder(OrderRequest orderRequest){
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        Order order = new Order();
        order.setCustomerName(orderRequest.getCustomerName());
        order.setCustomerEmail(orderRequest.getCustomerEmail());
        order.setStatus("CONFIRMED");
        for(OrderItemRequest itemRequest : orderRequest.getItem()){
            Product product = productRepository.findById(
                    itemRequest.getProductId()
            ).orElseThrow(()-> new RuntimeException(
                "Product not found with id "+ itemRequest.getProductId()
            ) );

            // check the product stock
            if(product.getStockQuantity() < itemRequest.getQuantity()){
                throw  new RuntimeException("Not enough stock");
            }

            BigDecimal priceOfItem = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
//            calculate price
            totalPrice = totalPrice.add(priceOfItem);

            // update the product table with latest stock quantity
            product.setStockQuantity(
                    product.getStockQuantity() - itemRequest.getQuantity()
            );

            productRepository.save(product);

            //builder pattern to make obj
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .priceAtPurchase(product.getPrice())
                    .build();


            orderItems.add(orderItem);


        }

        order.setTotalPrice(totalPrice);
        order.setOrderItem(orderItems);
        return orderRepository.save(order);
    }

    public Order getOrderByid(Long id){

        return orderRepository.findById(id).
                orElseThrow(()-> new RuntimeException( "No Order found with id "+ id));
    }

    public List<Order> getAllOrder(){
        return  orderRepository.findAll();
    }


    public boolean deleteOrder(Long id){

      // check order exist or not
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Order Not Present with this id "+id));

        for (OrderItem item : order.getOrderItem()) {
            Integer quantityOrdered = item.getQuantity();
            Product product = item.getProduct();

            // 3. Restore the stock quantity back to the product
            product.setStockQuantity(product.getStockQuantity() + quantityOrdered);
            productRepository.save(product);
        }

        // 4. Delete the order (Cascades to order items if CascadeType.ALL or CascadeType.REMOVE is set on Order.orderItem)
        orderRepository.delete(order);

        return true;
    }




}

package com.mod.creatorstore.dto;

import com.mod.creatorstore.entities.OrderItem;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Enter valid email")
    private String customerEmail;


    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderItemRequest> item;
}

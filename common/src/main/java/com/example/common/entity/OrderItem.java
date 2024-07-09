package com.example.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private Long id;
    private Order order;
    private Long productId;
    private Integer quantity;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean done;
}

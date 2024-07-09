package com.example.common.entity;

import com.example.common.entity.Base.BaseEntity;
import com.example.common.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {

    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private Integer totalAmount;
    //    @Column(name = "product_id")
//    private Long productId;
    private OrderStatus orderStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private Set<OrderItem> orderItem;

    @Override
    public int hashCode() {
        return Objects.hash(id); // Override hashCode based on unique identifier
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof OrderItem)) return false;
        OrderItem other = (OrderItem) obj;
        return Objects.equals(id, other.getId()); // Override equals based on unique identifier
    }
}

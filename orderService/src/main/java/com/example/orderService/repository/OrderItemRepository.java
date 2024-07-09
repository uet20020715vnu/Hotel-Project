package com.example.orderService.repository;

import com.example.orderService.entity.Order;
import com.example.orderService.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);
    List<OrderItem> findByProductIdAndDoneIsTrue(Long id);
}

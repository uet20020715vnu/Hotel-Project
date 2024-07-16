package com.example.orderService.service;

import com.example.common.entity.Product;
import com.example.common.entity.User;
import com.example.common.event.CreateEventToNotification;
import com.example.orderService.config.KafkaProducer;
import com.example.orderService.entity.Order;
import com.example.orderService.entity.OrderItem;
import com.example.orderService.entity.ScheduleProduct;
import com.example.orderService.enums.OrderStatus;
import com.example.orderService.exception.OrderException;
import com.example.orderService.helper.LocalDatetimeConverter;
import com.example.orderService.repository.OrderItemRepository;
import com.example.orderService.repository.OrderRepository;
import com.example.orderService.repository.ScheduleProductRepository;
import com.example.orderService.request.OrderRequest;
import com.example.orderService.specification.OrderSpecification;
import com.example.orderService.specification.SearchBody;
import com.example.orderService.specification.SearchCriteria;
import com.example.orderService.specification.SearchCriteriaOperator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static java.time.LocalDateTime.now;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final RestTemplate restTemplate;
    private final ScheduleProductRepository scheduleProductRepository;
    private final KafkaProducer kafkaProducer;

    public Page<Order> findAllAndSorting(SearchBody searchBody){

        Specification specification = Specification.where(null);

        if (searchBody.getStatus() != -1){
            specification = specification.and(new OrderSpecification(new SearchCriteria("status", SearchCriteriaOperator.EQUALS, searchBody.getStatus())));
        }

        if (searchBody.getStartDate() != null){
            LocalDateTime startDate = LocalDatetimeConverter.toLocalDateTime(searchBody.getStartDate(), true);
            specification = specification.and(new OrderSpecification(new SearchCriteria("createdAt", SearchCriteriaOperator.GREATER_THAN_OR_EQUALS,startDate)));
        }

        if (searchBody.getEndDate() != null){
            LocalDateTime endDate = LocalDatetimeConverter.toLocalDateTime(searchBody.getEndDate(), false);
            specification = specification.and(new OrderSpecification(new SearchCriteria("createdAt", SearchCriteriaOperator.LESS_THAN_OR_EQUALS,endDate)));
        }

        if (searchBody.getProductName() != null){
            specification = specification.and(new OrderSpecification(new SearchCriteria("", SearchCriteriaOperator.PRODUCT_JOIN_PRODUCT_NAME_LIKE, searchBody.getProductName().trim())));
        }

        if (searchBody.getCustomerName() != null){
            specification = specification.and(new OrderSpecification(new SearchCriteria("accountName", SearchCriteriaOperator.USER_JOIN_LIKE, searchBody.getCustomerName().trim())));
        }

        if (searchBody.getCustomerEmail() != null) {
            specification = specification.and(new OrderSpecification(new SearchCriteria("email", SearchCriteriaOperator.USER_JOIN_LIKE, searchBody.getCustomerEmail().trim())));
        }

        if (searchBody.getCustomerPhone() != null) {
            specification = specification.and(new OrderSpecification(new SearchCriteria("phoneNumber", SearchCriteriaOperator.USER_JOIN_LIKE, searchBody.getCustomerPhone().trim())));
        }

        if (searchBody.getOrderId() != null) {
            specification = specification.and(new OrderSpecification(new SearchCriteria("id", SearchCriteriaOperator.EQUALS, searchBody.getOrderId().trim())));
        }

        List<Sort.Order> orders = new ArrayList<>();

        Sort.Order order1;
        order1 = new Sort.Order(Sort.Direction.DESC, "createdAt");
        if (searchBody.getTimeSorting() !=null){
            if (searchBody.getTimeSorting().contains("oldest")){
                order1 = new Sort.Order(Sort.Direction.ASC, "createdAt");
            }
        }
        if (searchBody.getPriceSorting() !=null){
            Sort.Order order2;
            if (searchBody.getPriceSorting().contains("descending")){
                order2 = new Sort.Order(Sort.Direction.DESC, "totalPrice");
            }else {
                order2 = new Sort.Order(Sort.Direction.ASC, "totalPrice");
            }
            orders.add(order2);
        }

        orders.add(order1);
        Pageable sortedPage = PageRequest.of(searchBody.getPage()-1, searchBody.getLimit(), Sort.by(orders));
        Page<Order> ordersPage = orderRepository.findAll(specification, sortedPage);

        return ordersPage;
    }

    public Order findById(Long id){
        return orderRepository.findById(id).orElse(null);
    }
    public Order createOrder(OrderRequest orderRequest){
        if(orderRequest.getStartDate().isBefore(LocalDate.now())){
            throw new OrderException("Invalid Start date", HttpStatus.BAD_REQUEST);
        }
        if(orderRequest.getEndDate().isBefore(orderRequest.getStartDate())){
            throw new OrderException("Invalid End date",HttpStatus.BAD_REQUEST);
        }
        if(!CheckQuantity(orderRequest)){
            throw new OrderException("InvalidProduct",HttpStatus.BAD_REQUEST);
        }
        Order order = new Order();
        Set<OrderItem> orderItemSet = new HashSet<>();
        Integer totalAmount = 0;
        int totalDate = orderRequest.getStartDate().until(orderRequest.getEndDate()).getDays();
        for (Long productId : orderRequest.getOrderItem().keySet()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(productId);
            orderItem.setOrder(order);
            orderItem.setQuantity(orderRequest.getOrderItem().get(productId));
            orderItem.setStartDate(orderRequest.getStartDate());
            orderItem.setEndDate(orderRequest.getEndDate());
            orderItemSet.add(orderItem);
            Product product = restTemplate.getForObject("http://localhost:8083/api/v1/product/" + productId,Product.class);
            totalAmount = totalAmount + product.getPrice()*totalDate*orderRequest.getOrderItem().get(productId);
        }
        order.setOrderItem(orderItemSet);
        order.setTotalAmount(totalAmount);
        order.setOrderDate(now());
        order.setOrderStatus(OrderStatus.pending);
        orderRepository.save(order);
        return order;
    }

    public Object updateOrder(OrderRequest orderRequest, Long id) {
        Order order = orderRepository.findById(id).orElse(null);
//        order.setOrderItem(null);
//        Set<OrderItem> orderItemSet = new HashSet<>();
        Set<OrderItem> orderItemSet = order.getOrderItem();
        Iterator<OrderItem> itemIterator = orderItemSet.iterator();
        while(itemIterator.hasNext()){
            OrderItem item = itemIterator.next();
            itemIterator.remove();
        }
        Integer totalAmount = 0;
        int totalDate = orderRequest.getStartDate().until(orderRequest.getEndDate()).getDays();
        for (Long productId : orderRequest.getOrderItem().keySet()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(productId);
            orderItem.setOrder(order);
            orderItem.setQuantity(orderRequest.getOrderItem().get(productId));
            orderItem.setStartDate(orderRequest.getStartDate());
            orderItem.setEndDate(orderRequest.getEndDate());
            orderItemSet.add(orderItem);
            Product product = restTemplate.getForObject("http://localhost:8083/api/v1/product/" + productId,Product.class);
            totalAmount = totalAmount + product.getPrice()*totalDate*orderRequest.getOrderItem().get(productId);
        }
        order.setOrderItem(orderItemSet);
        order.setTotalAmount(totalAmount);
//        order.setOrderDate(now());
//        order.setOrderStatus(OrderStatus.pending);
        orderRepository.save(order);
        return orderRepository.save(order);
    }

    public Object deleteOrder(Long id) {
        orderRepository.deleteById(id);
        return "Order deleted";
    }

    public Page<Order> findByUser(Pageable pageable, Long userId){
        return orderRepository.findByUserId(pageable, userId);
    }
    public void updateOrderStatus(Boolean statusPayment, Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
                if(!statusPayment) {
                    order.setOrderStatus(OrderStatus.cancel);
                    orderRepository.save(order);
                } else {
                    if (CheckQuantity(orderId)){
                        order.setOrderStatus(OrderStatus.completed);
//                        sentEmail(orderId);
                        saveOrderItem(orderId);
                        orderRepository.save(order);
                    } else{
                        order.setOrderStatus(OrderStatus.refunded);
                        orderRepository.save(order);
                    }
                }
        }

        public boolean CheckQuantity(Long orderId){
            Order order = orderRepository.findById(orderId).orElse(null);
            List<OrderItem> orderItemList = orderItemRepository.findByOrder(order);
            if(orderItemList.isEmpty()){
                return true;
            }
            LocalDate startDate = orderItemList.get(0).getStartDate();
            LocalDate endDate = orderItemList.get(0).getEndDate();
            for (OrderItem item : orderItemList) {
                int total = item.getQuantity();
                List<OrderItem> order1 = orderItemRepository.findByProductIdAndDoneIsTrue(item.getProductId());
                for (OrderItem orderItem :order1) {
                    Product product = restTemplate.getForObject("http://localhost:8083/api/v1/product/"+ orderItem.getProductId(), Product.class);

                    if(((!orderItem.getStartDate().isBefore(startDate)) && (!orderItem.getStartDate().isAfter(endDate)))
                            || ((!orderItem.getEndDate().isBefore(startDate)) && (!orderItem.getEndDate().isAfter(endDate)))
                            || ((!orderItem.getStartDate().isAfter(startDate)) && (!orderItem.getEndDate().isBefore(endDate)))){
                        total = total + orderItem.getQuantity();
                        if(total > product.getQuantity()){
                            return false;
                        }
                }
            }
            }
            return true;
        }
        public boolean CheckQuantity(OrderRequest order){
                LocalDate startDate = order.getStartDate();
                LocalDate endDate = order.getEndDate();
            for (Long productId: order.getOrderItem().keySet()) {
                int total = order.getOrderItem().get(productId);
                List<OrderItem> order1 = orderItemRepository.findByProductIdAndDoneIsTrue(productId);
                if(order1.isEmpty()){
                    return  true;
                }
                for (OrderItem orderItem :order1) {
                    Product product = restTemplate.getForObject("http://localhost:8083/api/v1/product/"+ orderItem.getProductId(), Product.class);
                    if(((!orderItem.getStartDate().isBefore(startDate)) && (!orderItem.getStartDate().isAfter(endDate)))
                            || ((!orderItem.getEndDate().isBefore(startDate)) && (!orderItem.getEndDate().isAfter(endDate)))
                            || ((!orderItem.getStartDate().isAfter(startDate)) && (!orderItem.getEndDate().isBefore(endDate)))){
                        total = total + orderItem.getQuantity();
                        if(total > product.getQuantity()){
                            return false;
                        }
                    }
                }

            }
            return true;
    }

//    public void updateScheduleProduct(Long productId, Long orderId) {
//        Order order = orderRepository.findById(orderId).orElse(null);
//        LocalDate date = order.getStartDate();
//        while(date.compareTo(order.getEndDate()) <= 0){
//                ScheduleProduct scheduleProduct = new ScheduleProduct();
//                scheduleProduct.setProductId(productId);
//                scheduleProduct.setReservation(date);
//                scheduleProductRepository.save(scheduleProduct);
//            date = date.plusDays(1);
//        }
//    }
    public void saveOrderItem(Long orderId){
        Order order = orderRepository.findById(orderId).orElse(null);
        List<OrderItem> orderItems = orderItemRepository.findByOrder(order);
        for (OrderItem orderItem:orderItems) {
            orderItem.setDone(true);
            orderItemRepository.save(orderItem);
        }
    }

    public void sentEmail(Long orderId){
        Order order = restTemplate.getForObject("http://localhost:8085/api/v1/order/"+ orderId, Order.class);
        User user = restTemplate.getForObject("http://localhost:8081/api/v1/user/"+ order.getUserId(), User.class);
        CreateEventToNotification createEventToNotification = new CreateEventToNotification();
        createEventToNotification.setUserId(order.getUserId());
        createEventToNotification.setPrice(order.getTotalAmount());
        createEventToNotification.setEmail(user.getEmail());
        log.info("Before publishing a PaymentCreatedEvent");

        kafkaProducer.sendMessageEmail(createEventToNotification);

        log.info("******* Returning");
    }
}

package com.bookstore.service.order;

import com.bookstore.dto.order.OrderRequestDto;
import com.bookstore.dto.order.OrderResponseDto;
import com.bookstore.dto.order.OrderUpdateStatusDto;
import com.bookstore.dto.orderitem.OrderItemsResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderResponseDto> getAllOrders(Pageable pageable);

    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);

    OrderResponseDto updateOrder(Long orderId,OrderUpdateStatusDto orderUpdateStatusDto);

    Page<OrderItemsResponseDto> getAllOrderItems(Long orderId, Pageable pageable);

    OrderItemsResponseDto getOrderItem(Long orderId, Long itemId);
}

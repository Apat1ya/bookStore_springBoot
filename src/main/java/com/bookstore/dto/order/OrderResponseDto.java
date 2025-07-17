package com.bookstore.dto.order;

import com.bookstore.dto.orderitem.OrderItemsResponseDto;
import com.bookstore.model.Status;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private Status status;
    private String shippingAddress;
    private BigDecimal total;
    private Set<OrderItemsResponseDto> orderItems = new HashSet<>();
}

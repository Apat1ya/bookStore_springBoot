package com.bookstore.dto.orderitem;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemsResponseDto {
    private Long id;
    private Long bookId;
    private int quantity;
    private BigDecimal price;
}

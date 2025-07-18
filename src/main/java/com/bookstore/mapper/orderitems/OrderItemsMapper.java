package com.bookstore.mapper.orderitems;

import com.bookstore.config.MapperConfig;
import com.bookstore.dto.orderitem.OrderItemsResponseDto;
import com.bookstore.model.CartItem;
import com.bookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemsMapper {
    @Mapping(target = "bookId", source = "book.id")
    OrderItemsResponseDto toDto(OrderItem orderItem);

    @Mapping(target = "id",ignore = true)
    OrderItem toEntityFromCartItem(CartItem cartItem);
}

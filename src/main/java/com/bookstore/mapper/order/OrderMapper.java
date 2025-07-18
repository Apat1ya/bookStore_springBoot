package com.bookstore.mapper.order;

import com.bookstore.config.MapperConfig;
import com.bookstore.dto.order.OrderResponseDto;
import com.bookstore.dto.order.OrderUpdateStatusDto;
import com.bookstore.mapper.orderitems.OrderItemsMapper;
import com.bookstore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = OrderItemsMapper.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    OrderResponseDto toDto(Order order);

    void updateOrderStatusFromDto(
            OrderUpdateStatusDto orderUpdateStatusDto,
            @MappingTarget Order order);
}

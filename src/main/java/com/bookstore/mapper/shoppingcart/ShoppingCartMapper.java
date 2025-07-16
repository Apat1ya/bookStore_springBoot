package com.bookstore.mapper.shoppingcart;

import com.bookstore.config.MapperConfig;
import com.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.bookstore.mapper.cartitem.CartItemMapper;
import com.bookstore.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(target = "userId", source = "user.id")
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);
}

package com.bookstore.service.shoppingcart;

import com.bookstore.dto.cartitem.CartItemRequestDto;
import com.bookstore.dto.cartitem.CartItemUpdateDto;
import com.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.bookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCart();

    ShoppingCartResponseDto addItemToCart(CartItemRequestDto cartItemRequestDto);

    ShoppingCartResponseDto updateQuantity(Long cartItemId,CartItemUpdateDto cartItemUpdateDto);

    ShoppingCartResponseDto deleteItemById(Long cartItemId);

    void createShoppingCart(User user);
}

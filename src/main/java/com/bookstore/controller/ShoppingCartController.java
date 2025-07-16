package com.bookstore.controller;

import com.bookstore.dto.cartitem.CartItemRequestDto;
import com.bookstore.dto.cartitem.CartItemUpdateDto;
import com.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.bookstore.service.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management",
        description = "Endpoints for adding items to cart, removing from cart,"
                + " updating item quantities, viewing cart")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(
            summary = "Get all item from shopping cart",
            description = "Returns a list of all items in the shopping cart"
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ShoppingCartResponseDto getAllItemFromShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }

    @Operation(
            summary = "Add item in shopping cart",
            description = "Returns a list of adding items in shopping cart"
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCartResponseDto addItemInCart(
            @RequestBody @Valid CartItemRequestDto cartItemRequestDto) {
        return shoppingCartService.addItemToCart(cartItemRequestDto);
    }

    @Operation(
            summary = "Update cart item quantity",
            description = "Returns the list of items in "
                    + "the shopping cart along with the updated quantity"
    )
    @PutMapping("/items/{cartItemId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ShoppingCartResponseDto updateQuantityItemsInCart(
            @PathVariable Long cartItemId,
            @RequestBody @Valid CartItemUpdateDto cartItemUpdateDto) {
        return shoppingCartService.updateQuantity(cartItemId, cartItemUpdateDto);
    }

    @Operation(
            summary = "Delete item from cart",
            description = "Returns an updated list of items in "
                    + "the shopping cart after the item has been deleted"
    )
    @DeleteMapping("/items/{cartItemId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ShoppingCartResponseDto deleteItemFromCart(@PathVariable Long cartItemId) {
        return shoppingCartService.deleteItemById(cartItemId);
    }
}

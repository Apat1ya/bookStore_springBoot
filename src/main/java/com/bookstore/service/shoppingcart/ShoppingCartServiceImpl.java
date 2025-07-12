package com.bookstore.service.shoppingcart;

import com.bookstore.dto.cartitem.CartItemRequestDto;
import com.bookstore.dto.cartitem.CartItemUpdateDto;
import com.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.bookstore.exception.EntityNotFoundException;
import com.bookstore.mapper.cartitem.CartItemMapper;
import com.bookstore.mapper.shoppingcart.ShoppingCartMapper;
import com.bookstore.model.Book;
import com.bookstore.model.CartItem;
import com.bookstore.model.ShoppingCart;
import com.bookstore.repository.book.BookRepository;
import com.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.bookstore.service.user.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final UserService userService;
    private final BookRepository bookRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartResponseDto getShoppingCart() {
        Long userId = userService.getCurrentUserId();
        return shoppingCartRepository.findByUserId(userId)
                .map(shoppingCartMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found for user: "
                        + userId));
    }

    @Override
    public ShoppingCartResponseDto addItemToCart(CartItemRequestDto cartItemRequestDto) {
        Long userId = userService.getCurrentUserId();
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found for user: "
                        + userId));;
        Book book = bookRepository.findById(cartItemRequestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found by id: "
                        + cartItemRequestDto.getBookId()));
        Optional<CartItem> existingItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getBook().getId().equals(cartItemRequestDto.getBookId()))
                .findFirst();
        if (existingItem.isEmpty()) {
            CartItem newItem = new CartItem();
            newItem.setBook(book);
            newItem.setQuantity(cartItemRequestDto.getQuantity());
            newItem.setShoppingCart(cart);
            cart.getCartItems().add(newItem);
        } else {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + cartItemRequestDto.getQuantity());
        }
        shoppingCartRepository.save(cart);
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public ShoppingCartResponseDto updateQuantity(Long cartItemId,
                                                  CartItemUpdateDto cartItemUpdateDto) {
        Long userId = userService.getCurrentUserId();
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found for user: "
                        + userId));
        Optional<CartItem> existingItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst();
        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItemUpdateDto.getQuantity());
        } else {
            throw new EntityNotFoundException("Item with id " + cartItemId
            + "not found in cart");
        }
        shoppingCartRepository.save(cart);
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public ShoppingCartResponseDto deleteItemById(Long cartItemId) {
        Long userId = userService.getCurrentUserId();
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found for user: "
                        + userId));
        Optional<CartItem> existingItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst();
        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cart.getCartItems().remove(cartItem);
            shoppingCartRepository.save(cart);
        } else {
            throw new EntityNotFoundException("Item with id: " + cartItemId
            + "not found in shopping cart");
        }
        return shoppingCartMapper.toDto(cart);
    }
}

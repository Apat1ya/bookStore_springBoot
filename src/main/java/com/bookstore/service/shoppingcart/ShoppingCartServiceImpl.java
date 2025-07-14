package com.bookstore.service.shoppingcart;

import com.bookstore.dto.cartitem.CartItemRequestDto;
import com.bookstore.dto.cartitem.CartItemUpdateDto;
import com.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.bookstore.exception.EntityNotFoundException;
import com.bookstore.mapper.shoppingcart.ShoppingCartMapper;
import com.bookstore.model.Book;
import com.bookstore.model.CartItem;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;
import com.bookstore.repository.CartItemRepository;
import com.bookstore.repository.book.BookRepository;
import com.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.bookstore.repository.user.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Override
    public ShoppingCartResponseDto getShoppingCart() {
        ShoppingCart cart = getShoppingCartForCurrentUser();
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public ShoppingCartResponseDto addItemToCart(CartItemRequestDto cartItemRequestDto) {
        ShoppingCart cart = getShoppingCartForCurrentUser();
        Book book = bookRepository.findById(cartItemRequestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found by id: "
                        + cartItemRequestDto.getBookId()));
        Optional<CartItem> existingItem = cartItemRepository.findByShoppingCartAndBookId(
                cart.getId(), cartItemRequestDto.getBookId());
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
        ShoppingCart cart = getShoppingCartForCurrentUser();
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(
                cartItemId, cart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Item with id " + cartItemId
                        + " not found in cart"));

        cartItem.setQuantity(cartItemUpdateDto.getQuantity());
        shoppingCartRepository.save(cart);
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public ShoppingCartResponseDto deleteItemById(Long cartItemId) {
        ShoppingCart cart = getShoppingCartForCurrentUser();
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(
                        cartItemId, cart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Item with id " + cartItemId
                        + " not found in cart"));
        cart.getCartItems().remove(cartItem);
        shoppingCartRepository.save(cart);
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);
        return user.get().getId();
    }

    private ShoppingCart getShoppingCartForCurrentUser() {
        Long userId = getCurrentUserId();
        return shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found for user: "
                        + userId));
    }
}

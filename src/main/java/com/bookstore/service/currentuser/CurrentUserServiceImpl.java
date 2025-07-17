package com.bookstore.service.currentuser;

import com.bookstore.exception.EntityNotFoundException;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;
import com.bookstore.repository.order.OrderRepository;
import com.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.bookstore.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserServiceImpl implements CurrentUserService {
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;

    @Override
    public ShoppingCart getShoppingCartForCurrentUser() {
        Long userId = getCurrentUserId();
        return shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found for user: "
                        + userId));
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: "
                        + email));
    }

    @Override
    public Long getCurrentUserId() {
        User user = getCurrentUser();
        return user.getId();
    }
}

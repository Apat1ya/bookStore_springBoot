package com.bookstore.service.currentuser;

import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;

public interface CurrentUserService {
    Long getCurrentUserId();

    ShoppingCart getShoppingCartForCurrentUser();

    User getCurrentUser();
}

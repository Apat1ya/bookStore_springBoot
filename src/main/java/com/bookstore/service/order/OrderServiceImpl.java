package com.bookstore.service.order;

import com.bookstore.dto.order.OrderRequestDto;
import com.bookstore.dto.order.OrderResponseDto;
import com.bookstore.dto.order.OrderUpdateStatusDto;
import com.bookstore.dto.orderitem.OrderItemsResponseDto;
import com.bookstore.exception.OrderProcessingException;
import com.bookstore.mapper.order.OrderMapper;
import com.bookstore.mapper.orderitems.OrderItemsMapper;
import com.bookstore.model.Order;
import com.bookstore.model.OrderItem;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.Status;
import com.bookstore.repository.order.OrderRepository;
import com.bookstore.repository.orderitem.OrderItemRepository;
import com.bookstore.service.currentuser.CurrentUserService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemsMapper orderItemsMapper;
    private final OrderItemRepository orderItemRepository;
    private final CurrentUserService currentUserService;

    @Override
    public Page<OrderResponseDto> getAllOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findAllByUserId(currentUserService.getCurrentUserId(),
                pageable);
        return orders.map(orderMapper::toDto);
    }

    @Override
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        ShoppingCart cart = currentUserService.getShoppingCartForCurrentUser();
        if (cart.getCartItems().isEmpty()) {
            throw new OrderProcessingException("Can`t create order with empty shopping cart");
        }
        Order order = orderRepository.save(buildOrderFromCart(cart,orderRequestDto));
        return orderMapper.toDto(order);
    }

    @Override
    public OrderResponseDto updateOrder(Long orderId,OrderUpdateStatusDto orderUpdateStatusDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderProcessingException("Not found order with id: "
                        + orderId));

        orderMapper.updateOrderStatusFromDto(orderUpdateStatusDto,order);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public Page<OrderItemsResponseDto> getAllOrderItems(Long orderId, Pageable pageable) {
        return orderItemRepository.findAllByOrderId(orderId,pageable)
                .map(orderItemsMapper::toDto);
    }

    @Override
    public OrderItemsResponseDto getOrderItem(Long orderId, Long itemId) {
        return orderItemsMapper.toDto(orderItemRepository.findByIdAndOrderId(itemId,orderId));
    }

    private Order buildOrderFromCart(ShoppingCart shoppingCart,OrderRequestDto orderRequestDto) {
        Order order = new Order();
        order.setUser(currentUserService.getCurrentUser());
        order.setShippingAddress(orderRequestDto.getShippingAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Status.PENDING);

        Set<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = orderItemsMapper.toEntityFromCartItem(cartItem);
                    orderItem.setOrder(order);
                    orderItem.setPrice(cartItem.getBook().getPrice()
                            .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
                    return orderItem;
                })
                .collect(Collectors.toSet());
        order.setOrderItems(orderItems);
        order.setTotal(calculateTotal(orderItems));
        return order;
    }

    private BigDecimal calculateTotal(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> orderItem.getPrice()
                        .multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

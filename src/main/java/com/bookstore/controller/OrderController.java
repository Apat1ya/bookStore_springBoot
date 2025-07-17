package com.bookstore.controller;

import com.bookstore.dto.order.OrderRequestDto;
import com.bookstore.dto.order.OrderResponseDto;
import com.bookstore.dto.order.OrderUpdateStatusDto;
import com.bookstore.dto.orderitem.OrderItemsResponseDto;
import com.bookstore.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Orders management",
        description = "Endpoints for managing orders and order items")
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Get orders history",
            description = "Endpoint for retrieving orders history for a authenticated user")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public Page<OrderResponseDto> getOrderHistory(Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    @Operation(
            summary = "Create new order",
            description = "Creates a new order from the current user's shopping cart"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto createOrder(@RequestBody @Valid OrderRequestDto orderRequestDto) {
        return orderService.createOrder(orderRequestDto);
    }

    @Operation(
            summary = "Update order status",
            description = "Allows admin to update the status of an existing order"
    )
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/{id}")
    public OrderResponseDto updateStatusOrder(
            @PathVariable Long id,
            @RequestBody @Valid OrderUpdateStatusDto orderUpdateStatusDto) {
        return orderService.updateOrder(id,orderUpdateStatusDto);
    }

    @Operation(
            summary = "Get all order items",
            description = "Retrieves list of items for a specific order"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{orderId}")
    public Page<OrderItemsResponseDto> getAllOrderItemsFromOrder(
            @PathVariable Long orderId,
            Pageable pageable) {
        return orderService.getAllOrderItems(orderId,pageable);
    }

    @Operation(
            summary = "Get specific order item",
            description = "Retrieves details of a specific item from a specific order"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{orderId}/items/{id}")
    public OrderItemsResponseDto getOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderService.getOrderItem(orderId,itemId);
    }
}

package mate.academy.intro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.order.CreateOrderRequestDto;
import mate.academy.intro.dto.order.OrderDto;
import mate.academy.intro.dto.order.OrderItemDto;
import mate.academy.intro.dto.order.UpdateOrderStatusRequestDto;
import mate.academy.intro.model.User;
import mate.academy.intro.service.order.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing orders")
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new order", description = "Add a new order")
    public OrderDto addOrder(Authentication authentication,
                           @Valid @RequestBody CreateOrderRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.createOrder(user.getId(), requestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get all orders", description = "Get a list of all available orders")
    public List<OrderDto> getAllOrders(Authentication authentication, Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.getAllOrders(user.getId(), pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get all items", description = "Get a list of all available items")
    public List<OrderItemDto> getAllOrderItems(@PathVariable Long orderId,
                                               Authentication authentication,
                                               Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.getAllOrderItemsByOrderId(orderId, user.getId(), pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items/{orderItemId}")
    @Operation(summary = "Get item by id", description = "Get item by id")
    public OrderItemDto getOrderItem(@PathVariable Long orderId,
                                     @PathVariable Long orderItemId,
                                     Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderItemByOrderIdAndOrderItemId(orderId, orderItemId, user.getId());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update status for order", description = "Update status for order")
    public OrderDto updateStatusForOrder(@PathVariable Long id,
                                       @RequestBody @Valid UpdateOrderStatusRequestDto requestDto) {
        return orderService.updateOrderStatus(id, requestDto);
    }
}

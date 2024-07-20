package mate.academy.intro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.order.CreateOrderRequestDto;
import mate.academy.intro.model.User;
import mate.academy.intro.service.order.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public Object addOrder(Authentication authentication,
                           @Valid @RequestBody CreateOrderRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.createOrder(user.getId(), requestDto);
    }

}

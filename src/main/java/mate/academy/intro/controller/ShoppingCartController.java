package mate.academy.intro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.book.BookDto;
import mate.academy.intro.dto.shoppingсart.CreateCartItemRequestDto;
import mate.academy.intro.service.shopping.cart.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Shopping cart management", description = "Endpoints for managing shopping carts")
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new cart item", description = "Add a new cart item")
    public BookDto createBook(Authentication authentication,
                              @RequestBody @Valid CreateCartItemRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return null;
    }
}

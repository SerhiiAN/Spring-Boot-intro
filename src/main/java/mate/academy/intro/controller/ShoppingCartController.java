package mate.academy.intro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.shoppingcart.CartItemDto;
import mate.academy.intro.dto.shoppingcart.CreateCartItemRequestDto;
import mate.academy.intro.dto.shoppingcart.ShoppingCartDto;
import mate.academy.intro.dto.shoppingcart.UpdateCartItemRequestDto;
import mate.academy.intro.model.User;
import mate.academy.intro.service.shoppingcart.CartItemService;
import mate.academy.intro.service.shoppingcart.ShoppingCartService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for managing shopping carts")
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final CartItemService cartItemService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new cart item", description = "Add a new cart item")
    public ShoppingCartDto addBook(Authentication authentication,
                           @RequestBody @Valid CreateCartItemRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addOrUpdateCartItem(user.getId(), requestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all cart items in shopping cart",
            description = "Retrieve all cart items in the user's shopping cart")
    public ShoppingCartDto getAllCartItemsInShoppingCart(Authentication authentication,
                                                           Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getCartItemsByUserId(user.getId(), pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update cart item", description = "update cart item quantity.")
public CartItemDto updateQuantity(@PathVariable Long cartItemId,
                                  @RequestBody @Valid UpdateCartItemRequestDto requestDto) {
        return cartItemService.update(cartItemId, requestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete cart item", description = "Delete cart item")
    public void deleteCartItem(@PathVariable Long cartItemId) {
        cartItemService.deleteById(cartItemId);
    }
}

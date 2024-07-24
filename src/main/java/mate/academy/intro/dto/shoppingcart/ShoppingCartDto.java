package mate.academy.intro.dto.shoppingcart;

import java.util.List;

public record ShoppingCartDto(
        Long id,
        Long userId,
        List<CartItemDto> cartItems
) {
}

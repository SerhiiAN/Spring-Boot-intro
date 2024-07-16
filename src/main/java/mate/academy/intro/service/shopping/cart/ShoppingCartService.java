package mate.academy.intro.service.shopping.cart;

import mate.academy.intro.dto.shoppingсart.ShoppingCartDto;
import mate.academy.intro.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCartDto save(ShoppingCart shoppingCart);

    ShoppingCartDto getShoppingCartByUserId(Long userId);
}

package mate.academy.intro.service.shoppingcart;

import mate.academy.intro.dto.shoppingcart.CreateCartItemRequestDto;
import mate.academy.intro.dto.shoppingcart.ShoppingCartDto;
import mate.academy.intro.model.User;
import org.springframework.data.domain.Pageable;

public interface ShoppingCartService {
    void addNewShoppingCart(User user);

    ShoppingCartDto addOrUpdateCartItem(Long id, CreateCartItemRequestDto requestDto);

    ShoppingCartDto getCartItemsByUserId(Long userId);

    void cleanShoppingCart(Long shoppingCartId);
}

package mate.academy.intro.service.shopping.cart;

import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.shoppingсart.ShoppingCartDto;
import mate.academy.intro.mapper.ShoppingCartMapper;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.repository.shopping.cart.ShoppingCartRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCartDto save(ShoppingCart shoppingCart) {
        return shoppingCartMapper.toShoppingCartDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public ShoppingCartDto getShoppingCartByUserId(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserId(userId);
        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    @Override
    public void cleanShoppingCart(Long cartItemId) {

    }
}

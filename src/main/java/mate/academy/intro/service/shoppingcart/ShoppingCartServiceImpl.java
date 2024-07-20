package mate.academy.intro.service.shoppingcart;

import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.shoppingcart.CreateCartItemRequestDto;
import mate.academy.intro.dto.shoppingcart.ShoppingCartDto;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.mapper.CartItemMapper;
import mate.academy.intro.mapper.ShoppingCartMapper;
import mate.academy.intro.model.Book;
import mate.academy.intro.model.CartItem;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.model.User;
import mate.academy.intro.repository.shoppingcart.ShoppingCartRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Transactional
    @Override
    public void addNewShoppingCart(User user) {
        Set<CartItem> cartItems = new HashSet<>();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setCartItems(cartItems);
        shoppingCartRepository.save(shoppingCart);
    }

    @Transactional
    @Override
    public ShoppingCartDto addOrUpdateCartItem(Long id, CreateCartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = getShoppingCartByUserId(id);
        CartItem cartItem = shoppingCart.getCartItems().stream()
                .filter(c -> c.getBook().getId().equals(requestDto.bookId()))
                .findFirst()
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + requestDto.quantity());
        } else {
            CartItem newCartItem = new CartItem();
            Book book = new Book();
            book.setId(requestDto.bookId());
            newCartItem.setBook(book);
            newCartItem.setQuantity(requestDto.quantity());
            newCartItem.setShoppingCart(shoppingCart);
            shoppingCart.getCartItems().add(newCartItem);
        }
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    public ShoppingCart getShoppingCartByUserId(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserId(userId);
        return shoppingCart;
    }

    @Override
    public ShoppingCartDto getCartItemsByUserId(Long userId) {
        ShoppingCart shoppingCart = getShoppingCartByUserId(userId);
        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    @Transactional
    @Override
    public void cleanShoppingCart(Long shoppingCartId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(shoppingCartId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found with id: "
                        + shoppingCartId));
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);
    }
}

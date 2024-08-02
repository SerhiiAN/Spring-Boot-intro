package mate.academy.intro.service.shoppingcart;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.intro.dto.shoppingcart.CartItemDto;
import mate.academy.intro.dto.shoppingcart.CreateCartItemRequestDto;
import mate.academy.intro.dto.shoppingcart.ShoppingCartDto;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.mapper.CartItemMapper;
import mate.academy.intro.mapper.ShoppingCartMapper;
import mate.academy.intro.model.Book;
import mate.academy.intro.model.CartItem;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.model.User;
import mate.academy.intro.repository.shoppingcart.CartItemRepository;
import mate.academy.intro.repository.shoppingcart.ShoppingCartRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private CartItemRepository cartItemRepository;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;
    @InjectMocks
    private CartItemServiceImpl cartItemService;
    private ShoppingCartDto shoppingCartDto;
    private ShoppingCart shoppingCart;
    private CartItem cartItem;
    private CartItemDto cartItemDto;
    private CreateCartItemRequestDto requestDto;
    private Long firstLong;
    private Book book;
    private User user;

    @BeforeEach
    void setUp() {
        firstLong = 1L;

        user = new User();
        user.setEmail("e@gmail.com");
        user.setId(firstLong);

        book = new Book();
        book.setId(firstLong);
        book.setTitle("Title 1");

        cartItem = new CartItem();
        cartItem.setId(firstLong);
        cartItem.setBook(book);
        cartItem.setQuantity(10);

        cartItemDto = new CartItemDto(
                cartItem.getId(),
                cartItem.getBook().getId(),
                cartItem.getBook().getTitle(),
                cartItem.getQuantity()
        );

        shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setId(firstLong);

        requestDto = new CreateCartItemRequestDto(
                firstLong, 10
        );
        shoppingCartDto = new ShoppingCartDto(
                firstLong, firstLong, List.of(cartItemDto)
        );
    }

    @Test
    @DisplayName("Add new shopping cart")
    public void addNewShoppingCart_shouldSaveShoppingCart() {
        shoppingCartService.addNewShoppingCart(user);
        verify(shoppingCartRepository).save(any(ShoppingCart.class));
    }

    @Test
    @DisplayName("Add or update cart item")
    public void addOrUpdateCartItem_ValidCartItem_ShouldReturnCartItem() {
        when(shoppingCartRepository.findShoppingCartByUserId(firstLong)).thenReturn(shoppingCart);
        when(shoppingCartRepository.save(shoppingCart)).thenReturn(shoppingCart);
        when(shoppingCartMapper.toShoppingCartDto(shoppingCart)).thenReturn(shoppingCartDto);
        ShoppingCartDto actual = shoppingCartService.addOrUpdateCartItem(firstLong, requestDto);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(10, actual.cartItems().get(0).quantity());
    }

    @Test
    @DisplayName("Get ShoppingCart by UserId")
    public void getShoppingCartByUserId_ValidUserId_ShouldReturnShoppingCart() {
        when(shoppingCartRepository.findShoppingCartByUserId(firstLong))
                .thenReturn(shoppingCart);
        ShoppingCart actual = shoppingCartService.getShoppingCartByUserId(firstLong);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(shoppingCart, actual);
    }

    @Test
    @DisplayName("Get cart items by userId")
    void getCartItemsByUserId_ValidUserId_ShouldReturnShoppingCartDto() {
        Pageable pageable = PageRequest.of(0, 30);
        when(shoppingCartRepository.findShoppingCartByUserId(firstLong)).thenReturn(shoppingCart);
        when(shoppingCartMapper.toShoppingCartDto(shoppingCart)).thenReturn(shoppingCartDto);
        ShoppingCartDto actual = shoppingCartService.getCartItemsByUserId(firstLong, pageable);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(shoppingCartDto, actual);
    }

    @Test
    @DisplayName("Clean shopping cart - shopping cart not found")
    void cleanShoppingCart_ShoppingCartNotFound_ShouldThrowException() {
        when(shoppingCartRepository.findById(firstLong)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = Assertions.assertThrows(
                EntityNotFoundException.class, () -> {
                shoppingCartService.cleanShoppingCart(firstLong);
            });
        Assertions.assertEquals("Shopping cart not found with id: "
                + firstLong, thrown.getMessage());

    }
}

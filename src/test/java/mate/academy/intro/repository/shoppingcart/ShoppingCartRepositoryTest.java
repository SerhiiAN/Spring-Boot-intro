package mate.academy.intro.repository.shoppingcart;

import mate.academy.intro.model.ShoppingCart;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShoppingCartRepositoryTest {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @Sql(scripts = {
            "classpath:database/shoppingcart/delete-shopping-cart.sql",
            "classpath:database/shoppingcart/create-shopping-cart.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/shoppingcart/delete-shopping-cart.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Find shopping cart by user id")
    void findShoppingCartByUserId_ValidCategoryId_ShouldReturnListOfBooks() {
        Long userId = 1L;
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserId(userId);
        Assertions.assertNotNull(shoppingCart);
        Assertions.assertEquals(1L, shoppingCart.getId());
    }
}

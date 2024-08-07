package mate.academy.intro.repository.shoppingcart;

import mate.academy.intro.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long>,
        JpaSpecificationExecutor<ShoppingCart> {
    ShoppingCart findShoppingCartByUserId(Long id);
}

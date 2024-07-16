package mate.academy.intro.repository.shopping.cart;

import mate.academy.intro.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long>,
        JpaSpecificationExecutor<ShoppingCart> {
    ShoppingCart findShoppingCartByUserId(Long id);
}

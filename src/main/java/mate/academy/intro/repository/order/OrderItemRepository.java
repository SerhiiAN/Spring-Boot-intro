package mate.academy.intro.repository.order;

import mate.academy.intro.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long>,
        JpaSpecificationExecutor<OrderItem> {
}

package mate.academy.intro.repository.order;

import java.util.List;
import mate.academy.intro.model.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>,
        JpaSpecificationExecutor<OrderItem> {
    List<OrderItem> findAllByOrderId(Long id, Pageable pageable);
}

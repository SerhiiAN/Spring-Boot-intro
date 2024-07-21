package mate.academy.intro.repository.order;

import mate.academy.intro.model.Order;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>,
        JpaSpecificationExecutor<Order> {
    List<Order> findOrdersByUserId(Long userId, Pageable pageable);

    boolean existsByIdAndUserId(Long Id, Long userId);
}

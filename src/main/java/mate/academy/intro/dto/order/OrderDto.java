package mate.academy.intro.dto.order;

import mate.academy.intro.model.Status;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderDto(
        Long id,
        Long userId,
        Set<OrderItemDto> orderItems,
        LocalDateTime orderDate,
        BigDecimal total,
        Status status
) {
}

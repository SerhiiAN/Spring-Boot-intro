package mate.academy.intro.service.order;

import mate.academy.intro.dto.order.CreateOrderRequestDto;
import mate.academy.intro.dto.order.OrderDto;

public interface OrderService {
    OrderDto createOrder(Long userId, CreateOrderRequestDto createOrderRequestDto);
}

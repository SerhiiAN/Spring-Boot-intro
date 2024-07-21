package mate.academy.intro.service.order;

import mate.academy.intro.dto.order.CreateOrderRequestDto;
import mate.academy.intro.dto.order.OrderDto;
import mate.academy.intro.dto.order.OrderItemDto;
import mate.academy.intro.dto.order.UpdateOrderStatusRequestDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(Long userId, CreateOrderRequestDto requestDto);

    List<OrderDto> getAllOrders(Long userId, Pageable pageable);

    List<OrderItemDto> getAllOrderItemsByOrderId(Long orderId, Long userId, Pageable pageable);

    OrderItemDto getOrderItemByOrderIdAndOrderItemId(Long orderId, Long orderItemId, Long userId);

    OrderDto updateOrderStatus(Long id, UpdateOrderStatusRequestDto requestDto);
}

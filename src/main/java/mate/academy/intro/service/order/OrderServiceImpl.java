package mate.academy.intro.service.order;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.order.CreateOrderRequestDto;
import mate.academy.intro.dto.order.OrderDto;
import mate.academy.intro.dto.order.OrderItemDto;
import mate.academy.intro.dto.order.UpdateOrderStatusRequestDto;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.mapper.OrderItemMapper;
import mate.academy.intro.mapper.OrderMapper;
import mate.academy.intro.model.Order;
import mate.academy.intro.model.OrderItem;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.repository.order.OrderItemRepository;
import mate.academy.intro.repository.order.OrderRepository;
import mate.academy.intro.repository.shoppingcart.ShoppingCartRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Transactional
    @Override
    public OrderDto createOrder(Long userId, CreateOrderRequestDto requestDto) {
        ShoppingCart cart = shoppingCartRepository.findShoppingCartByUserId(userId);
        if (cart.getCartItems().isEmpty()) {
            throw new EntityNotFoundException("Cart is empty for user: " + userId);
        }
        Order order = orderMapper.cartToOrder(cart, requestDto.shippingAddress());
        cart.clearCart();
        return orderMapper.toOrderDto(orderRepository.save(order));
    }

    @Override
    public List<OrderDto> getAllOrders(Long userId, Pageable pageable) {
        return orderMapper.toOrderDtoList(orderRepository.findAllByUserId(userId, pageable));
    }

    @Override
    public Set<OrderItemDto> getAllOrderItemsByOrderId(Long orderId,
                                                       Long userId,
                                                       Pageable pageable) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Order not found for id: " + orderId));
        return orderItemMapper.toOrderItemDtoList(order.getOrderItems());
    }

    @Override
    public OrderItemDto getOrderItemByOrderIdAndOrderItemId(Long orderItemId,
                                                            Long orderId,
                                                            Long userId) {
        OrderItem item = orderItemRepository.findByIdAndOrderIdAndOrderUserId(orderItemId,
                orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order item not found for id: " + orderItemId));
        return orderItemMapper.toOrderItemDto(item);
    }

    @Override
    public OrderDto updateOrderStatus(Long id, UpdateOrderStatusRequestDto requestDto) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Order not found with id: " + id));
        order.setStatus(requestDto.status());
        return orderMapper.toOrderDto(orderRepository.save(order));
    }
}

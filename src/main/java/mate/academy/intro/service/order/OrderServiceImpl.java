package mate.academy.intro.service.order;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.order.CreateOrderRequestDto;
import mate.academy.intro.dto.order.OrderDto;
import mate.academy.intro.dto.order.OrderItemDto;
import mate.academy.intro.dto.order.UpdateOrderStatusRequestDto;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.mapper.OrderItemMapper;
import mate.academy.intro.mapper.OrderMapper;
import mate.academy.intro.model.CartItem;
import mate.academy.intro.model.Order;
import mate.academy.intro.model.OrderItem;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.model.Status;
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
        Order order = newOrder(cart, requestDto.shippingAddress());
        order.setOrderItems(createSetOfOrderItems(order, cart.getCartItems()));
        OrderDto orderDto = orderMapper.toOrderDto(order);
        cart.clearCart();
        return orderDto;
    }

    @Transactional
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

    private BigDecimal countTotalOrderPrice(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(c -> c.getBook()
                        .getPrice()
                        .multiply(BigDecimal.valueOf(c.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Order newOrder(ShoppingCart cart, String shippingAddress) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus(Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(shippingAddress);
        order.setTotal(countTotalOrderPrice(cart.getCartItems()));
        return orderRepository.save(order);
    }

    private Set<OrderItem> createSetOfOrderItems(Order order, Set<CartItem> cartItems) {
        Set<OrderItem> orderItems = cartItems.stream()
                .map(c -> {
                    OrderItem orderItem = orderMapper.cartItemToOrderItem(c);
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toSet());
        orderItemRepository.saveAll(orderItems);
        return orderItems;
    }
}

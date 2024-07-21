package mate.academy.intro.service.order;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.order.CreateOrderRequestDto;
import mate.academy.intro.dto.order.OrderDto;
import mate.academy.intro.dto.order.OrderItemDto;
import mate.academy.intro.dto.order.UpdateOrderStatusRequestDto;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.mapper.OrderMapper;
import mate.academy.intro.model.Order;
import mate.academy.intro.model.OrderItem;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.model.Status;
import mate.academy.intro.repository.order.OrderItemRepository;
import mate.academy.intro.repository.order.OrderRepository;
import mate.academy.intro.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.intro.service.shoppingcart.ShoppingCartService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    @Override
    public OrderDto createOrder(Long userId, CreateOrderRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserId(userId);
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new EntityNotFoundException("Shopping cart " + userId + " is empty.");
        }
        Order order = new Order();
        order.setUser(shoppingCart.getUser());
        order.setOrderItems(shoppingCart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setBook(cartItem.getBook());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getBook().getPrice());
                    orderItem.setOrder(order);
                    return orderItem;
                }).collect(Collectors.toSet()));
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Status.PENDING);
        order.setShippingAddress(requestDto.shippingAddress());
        order.setTotal(order.getOrderItems().stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        Order savedOrder = orderRepository.save(order);
        shoppingCartService.cleanShoppingCart(shoppingCart.getId());
        return orderMapper.toOrderDto(savedOrder);
    }

    @Override
    public List<OrderDto> getAllOrders(Long userId, Pageable pageable) {
        List<Order> orders = orderRepository.findOrdersByUserId(userId, pageable);
        return orders.stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }

    @Override
    public List<OrderItemDto> getAllOrderItemsByOrderId(Long orderId,Long userId, Pageable pageable) {
        if (!orderRepository.existsByIdAndUserId(orderId, userId)) {
            throw new EntityNotFoundException("Order not found with id: "
                    + orderId + " for user with id: " + userId);
        }
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(orderId, pageable);
        return orderItems.stream()
                .map(orderMapper::toOrderItemDto)
                .toList();
    }

    @Override
    public OrderItemDto getOrderItemByOrderIdAndOrderItemId(Long orderId, Long orderItemId, Long userId) {
        if (!orderRepository.existsByIdAndUserId(orderId, userId)) {
            throw new EntityNotFoundException("Order not found with id: "
                    + orderId + " for user with id: " + userId);
        }
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderItemId));
        if (!orderItem.getOrder().getId().equals(orderId)) {
            throw new EntityNotFoundException("Order with id: " + orderId + " for user with id" + userId);
        }
        return orderMapper.toOrderItemDto(orderItem);
    }

    @Transactional
    @Override
    public OrderDto updateOrderStatus(Long id, UpdateOrderStatusRequestDto requestDto) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Order not found with id: " + id));
        order.setStatus(requestDto.status());
        return orderMapper.toOrderDto(orderRepository.save(order));
    }
}

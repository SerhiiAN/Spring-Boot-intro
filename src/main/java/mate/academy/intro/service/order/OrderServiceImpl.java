package mate.academy.intro.service.order;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.order.CreateOrderRequestDto;
import mate.academy.intro.dto.order.OrderDto;
import mate.academy.intro.dto.shoppingcart.ShoppingCartDto;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.model.Order;
import mate.academy.intro.model.OrderItem;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.model.Status;
import mate.academy.intro.repository.order.OrderRepository;
import mate.academy.intro.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.intro.service.shoppingcart.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartRepository shoppingCartRepository;

    @Transactional
    @Override
    public OrderDto createOrder(Long userId, CreateOrderRequestDto createOrderRequestDto) {
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
        order.setTotal(order.getOrderItems().stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        Order savedOrder = orderRepository.save(order);
        shoppingCartService.cleanShoppingCart(shoppingCart.getId());
        return orderMapper.toOrderDto;
    }
}

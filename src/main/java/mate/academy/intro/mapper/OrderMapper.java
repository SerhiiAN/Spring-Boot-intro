package mate.academy.intro.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.order.OrderDto;
import mate.academy.intro.dto.order.OrderItemDto;
import mate.academy.intro.model.CartItem;
import mate.academy.intro.model.Order;
import mate.academy.intro.model.OrderItem;
import mate.academy.intro.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = {OrderItemMapper.class})
public interface OrderMapper {

    @Mapping(source = "user.id", target = "userId")
    OrderDto toOrderDto(Order order);

    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toOrderItemDto(OrderItem orderItem);

    List<OrderDto> toOrderDtoList(List<Order> orders);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    OrderItem cartItemToOrderItem(CartItem cartItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "total", source = "cart.cartItems", qualifiedByName = "total")
    @Mapping(target = "orderItems", source = "cart.cartItems",
            qualifiedByName = "cartItemsToOrderItemsNoTarget")
    Order cartToOrder(ShoppingCart cart, String shippingAddress);

    @Named("total")
    default BigDecimal getTotal(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(i -> i.getBook().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Named("cartItemsToOrderItemsNoTarget")
    default Set<OrderItem> cartItemsToOrderItemsNoTarget(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::cartItemToOrderItem)
                .collect(Collectors.toSet());
    }

    default Set<OrderItem> cartItemsToOrderItems(Set<CartItem> cartItems,
                                                 @MappingTarget Order order) {
        Set<OrderItem> orderItems = cartItemsToOrderItemsNoTarget(cartItems);
        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        return orderItems;
    }
}

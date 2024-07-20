package mate.academy.intro.mapper;

import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.order.OrderDto;
import mate.academy.intro.dto.order.OrderItemDto;
import mate.academy.intro.model.Order;
import mate.academy.intro.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {

    @Mapping(source = "user.id", target = "userId")
    OrderDto toOrderDto(Order order);

    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toOrderItemDto(OrderItem orderItem);
}

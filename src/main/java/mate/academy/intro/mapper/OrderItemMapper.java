package mate.academy.intro.mapper;

import java.util.Set;
import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.order.OrderItemDto;
import mate.academy.intro.model.OrderItem;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    Set<OrderItemDto> toOrderItemDtoList(Set<OrderItem> orderItems);

    OrderItemDto toOrderItemDto(OrderItem orderItem);
}

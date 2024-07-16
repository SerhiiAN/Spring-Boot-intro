package mate.academy.intro.mapper;

import mate.academy.intro.config.MapperConfig;
<<<<<<< HEAD
import mate.academy.intro.dto.shoppingcart.ShoppingCartDto;
=======
import mate.academy.intro.dto.shoppingсart.ShoppingCartDto;
>>>>>>> d6e2294 (first stage)
import mate.academy.intro.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {CartItemMapper.class})
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    ShoppingCartDto toShoppingCartDto(ShoppingCart shoppingCart);
}

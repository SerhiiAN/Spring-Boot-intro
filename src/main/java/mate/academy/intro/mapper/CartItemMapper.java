package mate.academy.intro.mapper;

import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.shoppingсart.CartItemDto;
import mate.academy.intro.dto.shoppingсart.CreateCartItemRequestDto;
import mate.academy.intro.model.Book;
import mate.academy.intro.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemDto toCartItemDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem toModel(CreateCartItemRequestDto requestDto);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    void updateModel(CreateCartItemRequestDto requestDto, @MappingTarget CartItem cartItem);

    @Named("bookFromId")
    default Book bookFromId(Long id) {
        Book book = new Book();
        book.setId(id);
        return book;
    }
}

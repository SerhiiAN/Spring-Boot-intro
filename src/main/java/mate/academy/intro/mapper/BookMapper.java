package mate.academy.intro.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.book.BookDto;
import mate.academy.intro.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.intro.dto.book.CreateBookRequestDto;
import mate.academy.intro.model.Book;
import mate.academy.intro.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    void updateModel(CreateBookRequestDto requestDto, @MappingTarget Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategoryIds(
                book.getCategories().stream()
                        .map(Category::getId)
                        .collect(Collectors.toSet()));
    }

    @AfterMapping
    default void setCategories(@MappingTarget Book book, CreateBookRequestDto requestDto) {
        Set<Category> categories = requestDto.getCategoryIds().stream()
                .map(id -> {
                    Category category = new Category();
                    category.setId(id);
                    return category;
                })
                .collect(Collectors.toSet());
        book.setCategories(categories);
    }
}

package mate.academy.intro.dto.shoppingсart;

public record CartItemDto(
    Long id,
    Long bookId,
    String bookTitle,
    int quantity
) {
}

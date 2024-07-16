package mate.academy.intro.dto.shoppingсart;

public record CreateCartItemRequestDto(
    Long bookId,
    int quantity
) {
}

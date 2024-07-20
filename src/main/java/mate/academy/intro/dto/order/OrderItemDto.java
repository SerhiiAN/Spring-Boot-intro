package mate.academy.intro.dto.order;

public record OrderItemDto(
        Long id,
        Long bookId,
        int quantity
) {
}

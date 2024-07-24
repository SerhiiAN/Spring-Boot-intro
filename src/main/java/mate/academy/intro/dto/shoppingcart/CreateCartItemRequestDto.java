package mate.academy.intro.dto.shoppingcart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public record CreateCartItemRequestDto(
        @Positive(message = "Book ID must be a positive number.")
        Long bookId,
        @Min(value = 1, message = "Quantity must be at least 1.")
        int quantity
) {
}

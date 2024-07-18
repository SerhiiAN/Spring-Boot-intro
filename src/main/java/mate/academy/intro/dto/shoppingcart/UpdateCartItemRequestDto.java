package mate.academy.intro.dto.shoppingcart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateCartItemRequestDto(
        @NotNull(message = "Quantity is mandatory.")
        @Min(value = 1, message = "Quantity must be at least 1.")
        int quantity
) {
}

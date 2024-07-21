package mate.academy.intro.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOrderRequestDto(
        @NotBlank(message = "Shipping address is mandatory.")
        @Size(max = 255, message = "Shipping address should not exceed 255 characters.")
        String shippingAddress
) {
}

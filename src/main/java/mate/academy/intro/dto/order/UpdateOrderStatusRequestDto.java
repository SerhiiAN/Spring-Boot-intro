package mate.academy.intro.dto.order;

import jakarta.validation.constraints.NotNull;
import mate.academy.intro.model.Status;

public record UpdateOrderStatusRequestDto(
        @NotNull
        Status status
) {
}

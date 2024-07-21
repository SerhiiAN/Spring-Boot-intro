package mate.academy.intro.dto.order;

import mate.academy.intro.model.Status;

public record UpdateOrderStatusRequestDto(
        Status status
) {
}

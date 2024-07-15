package mate.academy.intro.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequestDto(
        @NotBlank(message = "The name is mandatory.")
        @Size(max = 255, message = "The name should not exceed 255 characters.")
        String name,
        @NotBlank(message = "The description is mandatory.")
        @Size(max = 255, message = "The description should not exceed 255 characters.")
        String description
) {
}

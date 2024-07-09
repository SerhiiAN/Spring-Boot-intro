package mate.academy.intro.dto.book;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotBlank(message = "The title is mandatory.")
    @Size(max = 255, message = "The title should not exceed 255 characters.")
    private String title;
    @NotBlank(message = "The author is mandatory.")
    @Size(max = 255, message = "The author should not exceed 255 characters.")
    private String author;
    @NotBlank(message = "The ISBN is mandatory.")
    @Pattern(regexp = "^(97(8|9))?\\d{9}(\\d|X)$", message = "not the correct format")
    private String isbn;
    @NotNull(message = "The price is mandatory")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "The price must be more than 0")
    private BigDecimal price;
    @NotBlank(message = "The cover image is mandatory.")
    @Pattern(regexp = "^(https?:\\/\\/.*\\.(?:png|jpg|jpeg|gif))$",
            message = "not the correct format for cover image")

    private String coverImage;
}

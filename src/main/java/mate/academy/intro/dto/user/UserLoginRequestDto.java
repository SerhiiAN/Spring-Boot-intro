package mate.academy.intro.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @Email
        @Size(min = 8, max = 35)
        @NotBlank
        String email,
        @NotBlank
        @Size(min = 8, max = 35)
        String password
) {
}

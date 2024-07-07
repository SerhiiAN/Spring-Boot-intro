package mate.academy.intro.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @Email
        @Size(min = 8, max = 35)
        @NotEmpty
        String email,
        @NotEmpty
        @Size(min = 8, max = 35)
        String password
) {
}

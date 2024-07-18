package mate.academy.intro.service.user;

import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.user.UserRegistrationRequestDto;
import mate.academy.intro.dto.user.UserResponseDto;
import mate.academy.intro.exception.RegistrationException;
import mate.academy.intro.mapper.UserMapper;
import mate.academy.intro.model.User;
import mate.academy.intro.repository.user.UserRepository;
import mate.academy.intro.service.shoppingcart.ShoppingCartService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ShoppingCartService shoppingCartService;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException(String
                    .format("User with this email: %s already exists",
                    requestDto.getEmail()));
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserResponseDto responseDto = userMapper.toUserResponseDto(userRepository.save(user));
        shoppingCartService.addNewShoppingCart(user);
        return responseDto;
    }
}

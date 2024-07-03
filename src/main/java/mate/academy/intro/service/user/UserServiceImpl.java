package mate.academy.intro.service.user;

import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.user.UserRegistrationRequestDto;
import mate.academy.intro.dto.user.UserResponseDto;
import mate.academy.intro.exception.RegistrationException;
import mate.academy.intro.mapper.UserMapper;
import mate.academy.intro.model.User;
import mate.academy.intro.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Cannot register user" + requestDto.getEmail());
        }
        User user = userMapper.toModel(requestDto);
        return userMapper.toUserResponseDto(userRepository.save(user));
    }
}

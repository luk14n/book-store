package com.lukian.bookstore.service.user;

import com.lukian.bookstore.dto.user.CreateUserRequestDto;
import com.lukian.bookstore.dto.user.UserDto;
import com.lukian.bookstore.exception.RegistrationException;
import com.lukian.bookstore.mapper.UserMapper;
import com.lukian.bookstore.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto register(CreateUserRequestDto requestDto) throws RegistrationException {
        if ((userRepository.findByEmail(requestDto.getEmail()).isEmpty())) {
            return userMapper.toDto(userRepository.save(userMapper.toModel(requestDto)));
        }
        throw new RegistrationException(
                "User with email: " + requestDto.getEmail() + " already exist");
    }
}
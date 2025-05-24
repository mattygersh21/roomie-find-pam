package emsquare.roomie_find.pam.services;

import emsquare.roomie_find.pam.dtos.UserDto;
import emsquare.roomie_find.pam.entities.User;
import emsquare.roomie_find.pam.mappers.UserMapper;
import emsquare.roomie_find.pam.repositories.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Primary
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto registerUser(UserDto userDto) {
        User user = UserMapper.toEntity(userDto);
        userRepository.save(user);

        UserDto savedUser = UserMapper.toDto(user);
        return savedUser;
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

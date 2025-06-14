package emsquare.roomie_find.pam.services;

import emsquare.roomie_find.pam.dtos.LoginRequest;
import emsquare.roomie_find.pam.dtos.LoginResponse;
import emsquare.roomie_find.pam.dtos.UserDto;
import emsquare.roomie_find.pam.entities.User;
import emsquare.roomie_find.pam.exceptions.EmailNotFoundException;
import emsquare.roomie_find.pam.exceptions.EmailUsedException;
import emsquare.roomie_find.pam.exceptions.PasswordIncorrectException;
import emsquare.roomie_find.pam.mappers.UserMapper;
import emsquare.roomie_find.pam.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@Primary
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto registerUser(UserDto userDto) {
        String newEmail = userDto.getEmail();
        if (findUserByEmail(newEmail).isPresent()) {
            throw new EmailUsedException("Email address " + newEmail + " is already in use.");
        }

        User user = UserMapper.toEntity(userDto);
        userRepository.save(user);

        UserDto savedUser = UserMapper.toDto(user);
        return savedUser;
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public LoginResponse attemptLogin(LoginRequest loginRequest) throws PasswordIncorrectException, EmailNotFoundException {
        Optional<User> userOptional = findUserByEmail(loginRequest.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (BCrypt.checkpw(loginRequest.getPassword(), user.getPasswordHash())) {
                String secretKey = System.getProperty("JWT_SECRET_KEY");
                if (secretKey == null || secretKey.isEmpty()) {
                    throw new RuntimeException("JWT secret key is not configured.");
                }
                Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

                String token = Jwts.builder()
                        .setSubject(user.getEmail())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                        .signWith(key)
                        .compact();

                return new LoginResponse(token);
            } else {
                throw new PasswordIncorrectException("Password is incorrect for user: " + loginRequest.getEmail());
            }
        }
        throw new EmailNotFoundException("Email address " + loginRequest.getEmail() + " not found.");
    }
}

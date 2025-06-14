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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;

    private UserDto userDto;
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String EMAIL = "john.doe@example.com";
    private static final String PASSWORD = "password";
    private static final LocalDate BIRTH_DATE = LocalDate.of(1990, 1, 1);

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userRepository);

        userDto = new UserDto();
        userDto.setFirstName(FIRST_NAME);
        userDto.setLastName(LAST_NAME);
        userDto.setEmail(EMAIL);
        userDto.setPassword(PASSWORD);
        userDto.setBirthDate(BIRTH_DATE);

        System.setProperty("JWT_SECRET_KEY", "mocked-secret-key-with-more-chars-to-meet-the-length-requirement");
    }

    @Test
    public void testRegisterUser() {
        User savedUser = UserMapper.toEntity(userDto);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDto result = userService.registerUser(userDto);

        assertNotNull(result);
        assertEquals(FIRST_NAME, result.getFirstName());
        assertEquals(LAST_NAME, result.getLastName());
        assertEquals(EMAIL, result.getEmail());
        assertNull(result.getPassword());
        assertEquals(BIRTH_DATE, result.getBirthDate());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testFindUserByEmail() {
        Optional<User> user = Optional.of(UserMapper.toEntity(userDto));
        when(userRepository.findByEmail(any(String.class))).thenReturn(user);
        Optional<User> savedUser = userService.findUserByEmail(EMAIL);

        assertEquals(savedUser, user);

        verify(userRepository, times(1)).findByEmail(any(String.class));
    }

    @Test
    public void testRegisterUserThrowsEmailUsedException() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(UserMapper.toEntity(userDto)));

        EmailUsedException exception = assertThrows(EmailUsedException.class, () -> {
            userService.registerUser(userDto);
        });

        assertEquals("Email address " + EMAIL + " is already in use.", exception.getMessage());

        verify(userRepository, times(1)).findByEmail(EMAIL);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testAttemptLoginSuccess() {
        User user = UserMapper.toEntity(userDto);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(EMAIL);
        loginRequest.setPassword(PASSWORD);

        LoginResponse loginResponse = userService.attemptLogin(loginRequest);

        assertNotNull(loginResponse);
        assertNotNull(loginResponse.getToken());

        verify(userRepository, times(1)).findByEmail(EMAIL);
    }

    @Test
    public void testAttemptLoginEmailNotFound() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(EMAIL);
        loginRequest.setPassword(PASSWORD);

        EmailNotFoundException exception = assertThrows(EmailNotFoundException.class, () -> {
            userService.attemptLogin(loginRequest);
        });

        assertEquals("Email address " + EMAIL + " not found.", exception.getMessage());

        verify(userRepository, times(1)).findByEmail(EMAIL);
    }

    @Test
    public void testAttemptLoginIncorrectPassword() {
        User user = UserMapper.toEntity(userDto);
        user.setPasswordHash(BCrypt.hashpw("differentpassword", BCrypt.gensalt()));

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(EMAIL);
        loginRequest.setPassword(PASSWORD);

        PasswordIncorrectException exception = assertThrows(PasswordIncorrectException.class, () -> {
            userService.attemptLogin(loginRequest);
        });

        assertEquals("Password is incorrect for user: " + EMAIL, exception.getMessage());

        verify(userRepository, times(1)).findByEmail(EMAIL);
    }
}

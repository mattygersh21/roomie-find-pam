package emsquare.roomie_find.pam.services;

import emsquare.roomie_find.pam.dtos.UserDto;
import emsquare.roomie_find.pam.entities.User;
import emsquare.roomie_find.pam.mappers.UserMapper;
import emsquare.roomie_find.pam.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
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
}

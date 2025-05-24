package emsquare.roomie_find.pam.mappers;

import emsquare.roomie_find.pam.dtos.UserDto;
import emsquare.roomie_find.pam.entities.User;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String EMAIL = "john.doe@example.com";
    private static final String PASSWORD = "password";
    private static final LocalDate BIRTH_DATE = LocalDate.of(1990, 1, 1);

    @Test
    public void testToEntity() {
        UserDto userDto = new UserDto();
        userDto.setFirstName(FIRST_NAME);
        userDto.setLastName(LAST_NAME);
        userDto.setEmail(EMAIL);
        userDto.setPassword(PASSWORD);
        userDto.setBirthDate(BIRTH_DATE);

        User user = UserMapper.toEntity(userDto);

        assertNotNull(user);
        assertEquals(FIRST_NAME, user.getFirstName());
        assertEquals(LAST_NAME, user.getLastName());
        assertEquals(EMAIL, user.getEmail());
        assertTrue(BCrypt.checkpw(PASSWORD, user.getPasswordHash()));
        assertEquals(BIRTH_DATE, user.getBirthDate());
    }

    @Test
    public void testToEntity_NullInput() {
        User user = UserMapper.toEntity(null);
        assertNull(user);
    }

    @Test
    public void testToDto() {
        User user = new User();
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setEmail(EMAIL);
        user.setPasswordHash(BCrypt.hashpw(PASSWORD, BCrypt.gensalt()));
        user.setBirthDate(BIRTH_DATE);

        UserDto userDto = UserMapper.toDto(user);

        assertNotNull(userDto);
        assertEquals(FIRST_NAME, userDto.getFirstName());
        assertEquals(LAST_NAME, userDto.getLastName());
        assertEquals(EMAIL, userDto.getEmail());
        assertNull(userDto.getPassword()); // Password is intentionally not mapped back to DTO.
        assertEquals(BIRTH_DATE, userDto.getBirthDate());
    }

    @Test
    public void testToDto_NullInput() {
        UserDto userDto = UserMapper.toDto(null);
        assertNull(userDto);
    }
}

package emsquare.roomie_find.pam.mappers;

import org.mindrot.jbcrypt.BCrypt;

import emsquare.roomie_find.pam.dtos.UserDto;
import emsquare.roomie_find.pam.entities.User;

public class UserMapper {

    public static User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPasswordHash(BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt()));
        user.setBirthDate(userDto.getBirthDate());
        return user;
    }

    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        // Password is not set intentionally as it is only need to validate against sign in.
        userDto.setBirthDate(user.getBirthDate());
        return userDto;
    }
}
package emsquare.roomie_find.pam.services;

import java.util.Optional;

import emsquare.roomie_find.pam.dtos.LoginRequest;
import emsquare.roomie_find.pam.dtos.LoginResponse;
import emsquare.roomie_find.pam.dtos.UserDto;
import emsquare.roomie_find.pam.entities.User;

public interface UserService {
    UserDto registerUser(UserDto userDto);
    Optional<User> findUserByEmail(String email);
    LoginResponse attemptLogin(LoginRequest loginRequest);
}

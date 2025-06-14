package emsquare.roomie_find.pam.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import emsquare.roomie_find.pam.dtos.LoginRequest;
import emsquare.roomie_find.pam.dtos.LoginResponse;
import emsquare.roomie_find.pam.dtos.UserDto;
import emsquare.roomie_find.pam.services.UserService;

@RestController
@RequestMapping("/pam")
@CrossOrigin(origins = "*")
public class AccessController {
    
    private final UserService userService;

    public AccessController(UserService userService) {
        this.userService = userService;
    }

    // @PostMapping("/login")
    // public LoginResponse login(@RequestBody LoginRequest loginRequest) {
    //     return userService.attemptLogin(loginRequest);
    // }

    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    LoginResponse response = userService.attemptLogin(loginRequest);
    return ResponseEntity.ok(response);
}

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        UserDto registeredUserDto = userService.registerUser(userDto);
        return ResponseEntity.ok(registeredUserDto);
    }

}

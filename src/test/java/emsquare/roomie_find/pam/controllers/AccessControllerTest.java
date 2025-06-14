package emsquare.roomie_find.pam.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import emsquare.roomie_find.pam.dtos.LoginRequest;
import emsquare.roomie_find.pam.dtos.LoginResponse;
import emsquare.roomie_find.pam.dtos.UserDto;
import emsquare.roomie_find.pam.exceptions.PasswordIncorrectException;
import emsquare.roomie_find.pam.services.UserService;

@WebMvcTest(AccessController.class)
public class AccessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    public void testLoginSuccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("admin@example.com");
        loginRequest.setPassword("password");

        LoginResponse loginResponse = new LoginResponse("mocked-jwt-token");

        Mockito.when(userService.attemptLogin(Mockito.any(LoginRequest.class))).thenReturn(loginResponse);

        mockMvc.perform(post("/pam/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
    }

    @Test
    public void testLoginFailurePasswordIncorrect() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("admin@example.com");
        loginRequest.setPassword("wrongpassword");

        Mockito.when(userService.attemptLogin(Mockito.any(LoginRequest.class)))
                .thenThrow(new PasswordIncorrectException("Password is incorrect for user: admin@example.com"));

        mockMvc.perform(post("/pam/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegisterUser() throws Exception {
        String firstName = "firstname";
        String password = "password";
        UserDto userDto = new UserDto();
        userDto.setFirstName(firstName);
        userDto.setPassword(password);

        UserDto registeredUserDto = new UserDto();
        registeredUserDto.setFirstName(firstName);
        registeredUserDto.setPassword(password);

        Mockito.when(userService.registerUser(Mockito.any(UserDto.class))).thenReturn(registeredUserDto);

        mockMvc.perform(post("/pam/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.password").value(password));
    }
}
package emsquare.roomie_find.pam.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import emsquare.roomie_find.pam.dtos.LoginRequest;
import emsquare.roomie_find.pam.dtos.UserDto;
import emsquare.roomie_find.pam.services.UserService;
import jakarta.servlet.ServletException;

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
        loginRequest.setEmail("admin");
        loginRequest.setPassword("password");

        mockMvc.perform(post("/pam/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()));
    }

    @Test
    public void testLoginFailure() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("admin");
        loginRequest.setPassword("wrongpassword");

        assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/pam/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk());
        });
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
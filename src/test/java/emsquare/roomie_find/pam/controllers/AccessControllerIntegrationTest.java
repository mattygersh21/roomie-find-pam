package emsquare.roomie_find.pam.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import emsquare.roomie_find.pam.dtos.UserDto;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Searches for the application-test.properties file for configuration. If this were not specified, it would use the default application.properties file. The text between the "application-" and ".properties" is the name of the profile. 
public class AccessControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegisterUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setPassword("password");
        userDto.setBirthDate(LocalDate.now());

        mockMvc.perform(post("/pam/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    public void testRegisterUserWithUsedEmail() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("used.email@example.com");
        userDto.setPassword("password");
        userDto.setBirthDate(LocalDate.now());

        mockMvc.perform(post("/pam/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("used.email@example.com"));

        mockMvc.perform(post("/pam/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email address used.email@example.com is already in use."));
    }
}
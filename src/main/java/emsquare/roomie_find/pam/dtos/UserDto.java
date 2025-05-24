package emsquare.roomie_find.pam.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDate birthDate;
}
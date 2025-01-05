package emsquare.roomie_find.pam.controllers;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import emsquare.roomie_find.pam.dtos.LoginRequest;
import emsquare.roomie_find.pam.dtos.LoginResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@RestController
@RequestMapping("/pam")
public class AccessController {
    
    // TODO: Move non-controller logic to business layer.
    
    // TODO: Replace with secret key.
    private static final String SECRET_KEY_STRING = "someBase64StringThatMeetsTheStandardOfBeingAtLeast";
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY_STRING));

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        // TODO: Replace this with legitimate authentication logic.
        if ("admin".equals(loginRequest.getUsername()) && "password".equals(loginRequest.getPassword())) {
            String token = Jwts.builder()
                .claim("sub", loginRequest.getUsername())
                .claim("iat", new Date())
                .claim("exp", new Date(System.currentTimeMillis() + 3600000))
                .signWith(SECRET_KEY)
                .compact();
            return new LoginResponse(token);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

}

package emsquare.roomie_find.pam.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class JwtConfiguration {
    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    @PostConstruct
    public void setJwtSecretKey() {
        System.setProperty("JWT_SECRET_KEY", jwtSecretKey);
    }
}
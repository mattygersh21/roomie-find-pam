package emsquare.roomie_find.pam.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import emsquare.roomie_find.pam.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    
}

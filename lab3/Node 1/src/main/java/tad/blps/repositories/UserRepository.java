package tad.blps.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tad.blps.entity.User;

import java.util.Optional;

@Repository("UserRepository")
public interface UserRepository
        extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndPassword(
            String username, 
            String password);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User findTopByOrderByIdDesc();
}

package hcmute.manage.club.uteclubs.repository;

import hcmute.manage.club.uteclubs.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByStudentId(String studentId);
    Optional<User> findUserByEmail(String email);
    boolean existsByStudentId(String studentId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

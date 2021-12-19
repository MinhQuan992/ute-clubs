package hcmute.manage.club.uteclubs.repository;

import hcmute.manage.club.uteclubs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByStudentId(String studentId);
    Optional<User> findUserByEmail(String email);
    boolean existsByStudentId(String studentId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

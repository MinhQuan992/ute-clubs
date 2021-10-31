package hcmute.manage.club.uteclubs.repository;

import hcmute.manage.club.uteclubs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);
    boolean existsByStudentId(String studentId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

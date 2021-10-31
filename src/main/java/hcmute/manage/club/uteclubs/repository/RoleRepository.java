package hcmute.manage.club.uteclubs.repository;

import hcmute.manage.club.uteclubs.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByRoleName(String roleName);
}

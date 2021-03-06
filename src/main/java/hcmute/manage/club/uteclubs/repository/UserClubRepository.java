package hcmute.manage.club.uteclubs.repository;

import hcmute.manage.club.uteclubs.model.Club;
import hcmute.manage.club.uteclubs.model.User;
import hcmute.manage.club.uteclubs.model.UserClub;
import hcmute.manage.club.uteclubs.model.composite_key.UserClubId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserClubRepository extends JpaRepository<UserClub, UserClubId> {
    Optional<UserClub> findUserClubByUserAndClub(User user, Club club);

    @Query("SELECT DISTINCT uc.roleInClub FROM UserClub uc WHERE uc.club = :club")
    List<String> getRolesInClub(@Param("club") Club club);

    @Query("SELECT uc.club FROM UserClub uc WHERE uc.user = :user AND uc.accepted = true")
    List<Club> getJoinedClubs(@Param("user") User user);

    @Query("SELECT uc.club FROM UserClub uc WHERE uc.user = :user AND uc.roleInClub <> 'ROLE_MEMBER'")
    List<Club> getManagedClubs(@Param("user") User user);

    @Query("SELECT uc.user FROM UserClub uc " +
            "WHERE uc.club = :club AND uc.roleInClub = :role AND uc.accepted = true " +
            "ORDER BY uc.user.studentId ASC")
    List<User> getMembersUsingRole(@Param("club") Club club, @Param("role") String role);

    @Query("SELECT uc.user FROM UserClub uc "
        + "WHERE uc.club = :club AND (uc.user.fullName LIKE :query OR uc.user.studentId LIKE :query) AND uc.accepted = true "
        + "ORDER BY uc.user.studentId ASC")
    List<User> getMembersUsingSearchedQuery(@Param("club") Club club, @Param("query") String query);

    @Query("SELECT uc.user FROM UserClub uc WHERE uc.club = :club AND uc.accepted = false")
    Page<User> getMemberRequests(@Param("club") Club club, Pageable pageable);
}

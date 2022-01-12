package hcmute.manage.club.uteclubs.framework.api;

import hcmute.manage.club.uteclubs.framework.dto.club.ClubResponse;
import hcmute.manage.club.uteclubs.framework.dto.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Optional;

import static hcmute.manage.club.uteclubs.framework.common.RegexConstant.COMMON_ID_PATTERN;
import static hcmute.manage.club.uteclubs.framework.common.RegexConstant.ROLE_IN_CLUB_PATTERN;

@RequestMapping("/clubs")
@Validated
public interface ClubAPI_User {
    @GetMapping
    ResponseEntity<Page<ClubResponse>> getClubs(@RequestParam Optional<Integer> page);

    @GetMapping("/{clubId}")
    ResponseEntity<ClubResponse> getClubById(
            @PathVariable("clubId")
            @NotBlank(message = "The club ID is required")
            @Pattern(regexp = COMMON_ID_PATTERN, message = "The club ID must contain numeric characters only")
                    String clubId
    );

    @GetMapping("/{clubId}/members")
    ResponseEntity<List<UserResponse>> getMembersByRole(
            @PathVariable("clubId")
            @NotBlank(message = "The club ID is required")
            @Pattern(regexp = COMMON_ID_PATTERN, message = "The club ID must contain numeric characters only")
                    String clubId,
            @Pattern(regexp = ROLE_IN_CLUB_PATTERN, message = "The role must be ROLE_LEADER, ROLE_MODERATOR or ROLE_MEMBER")
            @RequestParam String role
    );

    @GetMapping("/{clubId}/get-role")
    ResponseEntity<String> getRoleInClubOfCurrentUser(
            @PathVariable("clubId")
            @NotBlank(message = "The club ID is required")
            @Pattern(regexp = COMMON_ID_PATTERN, message = "The club ID must contain numeric characters only")
                    String clubId
    );

    @DeleteMapping("/{clubId}/leave")
    ResponseEntity<String> leaveClub(
            @PathVariable("clubId")
            @NotBlank(message = "The club ID is required")
            @Pattern(regexp = COMMON_ID_PATTERN, message = "The club ID must contain numeric characters only")
                    String clubId
    );
}

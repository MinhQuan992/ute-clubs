package hcmute.manage.club.uteclubs.framework.api;

import hcmute.manage.club.uteclubs.framework.dto.club.ClubResponse;
import hcmute.manage.club.uteclubs.framework.dto.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Optional;

import static hcmute.manage.club.uteclubs.framework.common.RegexConstant.COMMON_ID_PATTERN;

@RequestMapping("/clubs")
@Validated
public interface ClubAPI_User {
    @GetMapping
    ResponseEntity<Page<ClubResponse>> getClubs(@RequestParam Optional<Integer> page);

    @GetMapping("/{clubId}")
    ResponseEntity<ClubResponse> getClubById(
            @PathVariable("clubId")
            @NotBlank(message = "The club ID is required")
            @Pattern(regexp = COMMON_ID_PATTERN, message = "The club ID contains numeric characters only")
                    String clubId
    );

    @GetMapping("/{clubId}/members")
    ResponseEntity<Page<UserResponse>> getMembers(
            @PathVariable("clubId")
            @NotBlank(message = "The club ID is required")
            @Pattern(regexp = COMMON_ID_PATTERN, message = "The club ID contains numeric characters only")
                    String clubId,
            @RequestParam Optional<Integer> page
    );
}

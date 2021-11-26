package hcmute.manage.club.uteclubs.framework.api;

import hcmute.manage.club.uteclubs.framework.dto.club.ClubAcceptOrRejectMemberParam;
import hcmute.manage.club.uteclubs.framework.dto.club.ClubAddPersonParams;
import hcmute.manage.club.uteclubs.framework.dto.club.ClubResponse;
import hcmute.manage.club.uteclubs.framework.dto.user.UserResponse;
import hcmute.manage.club.uteclubs.framework.dto.user_club.UserClubResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Optional;

import static hcmute.manage.club.uteclubs.framework.common.RegexConstant.COMMON_ID_PATTERN;

@RequestMapping("/club-management")
@Validated
public interface ClubAPI_Leader {
    @GetMapping
    ResponseEntity<List<ClubResponse>> getManagedClubs();

    @GetMapping("/{clubId}")
    ResponseEntity<ClubResponse> getClubInfo(
            @PathVariable("clubId")
            @NotBlank(message = "The club ID is required")
            @Pattern(regexp = COMMON_ID_PATTERN, message = "The club ID contains numeric characters only")
                    String clubId
    );

    @PostMapping("/{clubId}/add-members")
    ResponseEntity<UserClubResponse> addMember(
            @PathVariable("clubId")
            @NotBlank(message = "The club ID is required")
            @Pattern(regexp = COMMON_ID_PATTERN, message = "The club ID contains numeric characters only")
                    String clubId,
            @RequestBody @Valid ClubAddPersonParams params
    );

    @GetMapping("/{clubId}/member-requests")
    ResponseEntity<Page<UserResponse>> getMemberRequests(
            @PathVariable("clubId")
            @NotBlank(message = "The club ID is required")
            @Pattern(regexp = COMMON_ID_PATTERN, message = "The club ID contains numeric characters only")
                    String clubId,
            @RequestParam Optional<Integer> page
    );

    @PutMapping("/{clubId}/member-requests")
    ResponseEntity<String> acceptMember(
            @PathVariable("clubId")
            @NotBlank(message = "The club ID is required")
            @Pattern(regexp = COMMON_ID_PATTERN, message = "The club ID contains numeric characters only")
                    String clubId,
            @RequestBody @Valid ClubAcceptOrRejectMemberParam param
    );

    @DeleteMapping("/{clubId}/member-requests")
    ResponseEntity<String> rejectMember(
            @PathVariable("clubId")
            @NotBlank(message = "The club ID is required")
            @Pattern(regexp = COMMON_ID_PATTERN, message = "The club ID contains numeric characters only")
                    String clubId,
            @RequestBody @Valid ClubAcceptOrRejectMemberParam param
    );
}
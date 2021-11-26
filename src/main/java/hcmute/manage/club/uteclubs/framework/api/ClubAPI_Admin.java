package hcmute.manage.club.uteclubs.framework.api;

import hcmute.manage.club.uteclubs.framework.dto.club.ClubAddOrUpdateInfoParams;
import hcmute.manage.club.uteclubs.framework.dto.club.ClubAddPersonParams;
import hcmute.manage.club.uteclubs.framework.dto.club.ClubResponse;
import hcmute.manage.club.uteclubs.framework.dto.user_club.UserClubResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Optional;

import static hcmute.manage.club.uteclubs.framework.common.RegexConstant.COMMON_ID_PATTERN;

@RequestMapping("/admin/club-management")
@Validated
public interface ClubAPI_Admin {
    @GetMapping
    ResponseEntity<Page<ClubResponse>> getClubs(@RequestParam Optional<Integer> page);

    @GetMapping("/{clubId}")
    ResponseEntity<ClubResponse> getClubById(
            @PathVariable("clubId")
            @NotBlank(message = "The club ID is required")
            @Pattern(regexp = COMMON_ID_PATTERN, message = "The club ID contains numeric characters only")
                    String clubId
    );

    @PostMapping
    ResponseEntity<ClubResponse> addNewClub(@RequestBody @Valid ClubAddOrUpdateInfoParams params);

    @PostMapping("/{clubId}/add-person")
    ResponseEntity<UserClubResponse> addPersonToClub(
            @PathVariable("clubId")
            @NotBlank(message = "The club ID is required")
            @Pattern(regexp = COMMON_ID_PATTERN, message = "The club ID contains numeric characters only")
                    String clubId,
            @RequestBody @Valid ClubAddPersonParams params
    );

    @PutMapping("/{clubId}/update-info")
    ResponseEntity<ClubResponse> updateInfo(
            @PathVariable("clubId")
            @NotBlank(message = "The club ID is required")
            @Pattern(regexp = COMMON_ID_PATTERN, message = "The club ID contains numeric characters only")
                    String clubId,
            @RequestBody @Valid ClubAddOrUpdateInfoParams params
    );
}

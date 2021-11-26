package hcmute.manage.club.uteclubs.framework.api;

import hcmute.manage.club.uteclubs.framework.dto.club.ClubRegisterOrCancelRequestParam;
import hcmute.manage.club.uteclubs.framework.dto.club.ClubResponse;
import hcmute.manage.club.uteclubs.framework.dto.user.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Optional;

import static hcmute.manage.club.uteclubs.framework.common.RegexConstant.COMMON_ID_PATTERN;

@RequestMapping("/users")
@Validated
public interface UserAPI {
    @GetMapping("/{userId}")
    ResponseEntity<UserResponse> getUser(
            @PathVariable("userId")
            @NotBlank(message = "The user ID is required")
            @Pattern(regexp = COMMON_ID_PATTERN, message = "The user ID contains numeric characters only")
                    String userId);

    @PostMapping("/signup")
    ResponseEntity<UserResponse> validateInfoAndGenerateOtp(@Valid @RequestBody UserSignUpWithoutOTPParams params);

    @PostMapping("/signup/verify")
    ResponseEntity<UserResponse> addNewUser(@Valid @RequestBody UserSignUpWithOTPParams params);

    @PutMapping("/{userId}/update-info")
    ResponseEntity<UserResponse> updateUserInfo(
            @PathVariable("userId")
            @NotBlank(message = "The user ID is required")
            @Pattern(regexp = COMMON_ID_PATTERN, message = "The user ID contains numeric characters only")
                    String userId,
            @Valid @RequestBody UserUpdateInfoParams params
    );

    @PutMapping("/{userId}/change-password")
    ResponseEntity<UserResponse> changePassword(
            @PathVariable("userId")
            @NotBlank(message = "The user ID is required")
            @Pattern(regexp = COMMON_ID_PATTERN, message = "The user ID contains numeric characters only")
                    String userId,
            @Valid @RequestBody UserChangePasswordParams params
    );

    @GetMapping("/{userId}/joined-clubs")
    ResponseEntity<List<ClubResponse>> getJoinedClubs(
            @PathVariable("userId")
            @NotBlank(message = "The user ID is required")
            @Pattern(regexp = COMMON_ID_PATTERN, message = "The user ID contains numeric characters only")
                    String userId,
            @RequestParam Optional<Integer> page
    );

    @PostMapping("/register-to-club")
    ResponseEntity<String> registerToClub(@RequestBody @Valid ClubRegisterOrCancelRequestParam param);

    @DeleteMapping("/cancel-request")
    ResponseEntity<String> cancelRequest(@RequestBody @Valid ClubRegisterOrCancelRequestParam param);
}

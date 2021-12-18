package hcmute.manage.club.uteclubs.framework.api;

import hcmute.manage.club.uteclubs.framework.dto.club.ClubRegisterParam;
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
            @Pattern(regexp = COMMON_ID_PATTERN, message = "The user ID must contain numeric characters only")
                    String userId
    );

    @PostMapping("/signup")
    ResponseEntity<UserResponse> validateInfoAndGenerateOtp(@Valid @RequestBody UserSignUpWithoutOTPParams params);

    @PostMapping("/signup/verify")
    ResponseEntity<UserResponse> addNewUser(@Valid @RequestBody UserSignUpWithOTPParams params);

    @PutMapping("/update-info")
    ResponseEntity<UserResponse> updateUserInfo(@Valid @RequestBody UserUpdateInfoParams params);

    @PutMapping("/change-password")
    ResponseEntity<String> changePassword(@Valid @RequestBody UserChangePasswordParams params);

    @PostMapping("reset-password/input-email")
    ResponseEntity<UserResponse> validateEmail(@Valid @RequestBody UserInputEmailParam param);

    @PutMapping("/reset-password/input-new-password")
    ResponseEntity<String> resetPassword(@Valid @RequestBody UserInputOTPAndNewPassParams params);

    @GetMapping("/joined-clubs")
    ResponseEntity<List<ClubResponse>> getJoinedClubs();

    @GetMapping("/not-joined-clubs")
    ResponseEntity<List<ClubResponse>> getNotJoinedClubs();

    @PostMapping("/register-to-club")
    ResponseEntity<String> registerToClub(@Valid @RequestBody ClubRegisterParam param);

    @DeleteMapping("/cancel-request/{clubId}")
    ResponseEntity<String> cancelRequest(
            @PathVariable("clubId")
            @NotBlank(message = "The club ID is required")
            @Pattern(regexp = COMMON_ID_PATTERN, message = "The club ID must contain numeric characters only")
                    String clubID
    );
}

package hcmute.manage.club.uteclubs.framework;

import hcmute.manage.club.uteclubs.exception.*;
import hcmute.manage.club.uteclubs.framework.dto.user.UserChangePasswordParams;
import hcmute.manage.club.uteclubs.framework.dto.user.UserSignUpWithOTPParams;
import hcmute.manage.club.uteclubs.framework.dto.user.UserSignUpWithoutOTPParams;
import hcmute.manage.club.uteclubs.framework.dto.user.UserUpdateInfoParams;
import hcmute.manage.club.uteclubs.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.text.ParseException;
import java.util.List;

import static hcmute.manage.club.uteclubs.framework.common.RegexConstant.COMMON_ID_PATTERN;

@RequestMapping("/users")
@Validated
public interface UserAPI {
    @GetMapping("/find/{userId}")
    ResponseEntity<User> getUser(
            @PathVariable("userId")
            @NotBlank(message = "The user ID is required")
            @Pattern(regexp = COMMON_ID_PATTERN, message = "The user ID contains numeric characters only")
                    String userId) throws NotFoundException;

    @PostMapping("/signup")
    ResponseEntity<User> validateInfoAndGenerateOtp(@Valid @RequestBody UserSignUpWithoutOTPParams params)
            throws PasswordsDoNotMatchException, InvalidRequestException, DateException, UnderageException;

    @PostMapping("/signup/verify")
    ResponseEntity<User> addNewUser(@Valid @RequestBody UserSignUpWithOTPParams params)
            throws OtpException, ParseException;

    @PutMapping("/update-info/{userId}")
    ResponseEntity<User> updateUserInfo(
            @PathVariable("userId")
            @NotBlank(message = "The user ID is required")
            @Pattern(regexp = COMMON_ID_PATTERN, message = "The user ID contains numeric characters only")
                    String userId,
            @Valid @RequestBody UserUpdateInfoParams params
    ) throws DateException, NotFoundException, InvalidRequestException, UnderageException;

    @PutMapping("/change-password/{userId}")
    ResponseEntity<User> changePassword(
            @PathVariable("userId")
            @NotBlank(message = "The user ID is required")
            @Pattern(regexp = COMMON_ID_PATTERN, message = "The user ID contains numeric characters only")
                    String userId,
            @Valid @RequestBody UserChangePasswordParams params
    ) throws PasswordsDoNotMatchException, NotFoundException, InvalidRequestException;
}

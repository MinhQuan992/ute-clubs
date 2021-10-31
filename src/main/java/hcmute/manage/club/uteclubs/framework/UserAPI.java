package hcmute.manage.club.uteclubs.framework;

import hcmute.manage.club.uteclubs.exception.*;
import hcmute.manage.club.uteclubs.framework.dto.user.UserSignUpWithOTPParams;
import hcmute.manage.club.uteclubs.framework.dto.user.UserSignUpWithoutOTPParams;
import hcmute.manage.club.uteclubs.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;

@RequestMapping("/users")
@Validated
public interface UserAPI {
    @PostMapping("/signup")
    ResponseEntity<User> validateInfoAndGenerateOtp(@Valid @RequestBody UserSignUpWithoutOTPParams params)
            throws ParseException, NoContentException, PasswordsDoNotMatchException, ConflictWorkFlowException, DateException;

    @PostMapping("/signup/verify")
    ResponseEntity<User> addNewUser(@Valid @RequestBody UserSignUpWithOTPParams params)
            throws OtpException, ParseException;

    @GetMapping("/find/{userId}")
    ResponseEntity<User> getUser(@PathVariable("userId") Long userId) throws NotFoundException;
}

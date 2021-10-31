package hcmute.manage.club.uteclubs.controller;

import hcmute.manage.club.uteclubs.exception.*;
import hcmute.manage.club.uteclubs.framework.UserAPI;
import hcmute.manage.club.uteclubs.framework.dto.user.UserSignUpWithOTPParams;
import hcmute.manage.club.uteclubs.framework.dto.user.UserSignUpWithoutOTPParams;
import hcmute.manage.club.uteclubs.model.User;
import hcmute.manage.club.uteclubs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
public class UserController implements UserAPI {
    private final UserService userService;

    @Override
    public ResponseEntity<User> validateInfoAndGenerateOtp(UserSignUpWithoutOTPParams params)
            throws PasswordsDoNotMatchException, ConflictWorkFlowException, DateException {
        return ResponseEntity.ok(userService.validateInfoAndGenerateOTP(params));
    }

    @Override
    public ResponseEntity<User> addNewUser(UserSignUpWithOTPParams params)
            throws OtpException, ParseException {
        return ResponseEntity.ok(userService.addNewUser(params));
    }

    @Override
    public ResponseEntity<User> getUser(Long userId) throws NotFoundException {
        return ResponseEntity.ok(userService.getUserById(userId));
    }
}

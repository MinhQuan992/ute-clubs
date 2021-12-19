package hcmute.manage.club.uteclubs.controller;

import hcmute.manage.club.uteclubs.framework.api.UserAPI;
import hcmute.manage.club.uteclubs.framework.dto.club.ClubRegisterParam;
import hcmute.manage.club.uteclubs.framework.dto.club.ClubResponse;
import hcmute.manage.club.uteclubs.framework.dto.user.*;
import hcmute.manage.club.uteclubs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController implements UserAPI {
    private final UserService userService;

    @Override
    public ResponseEntity<UserResponse> getUser(String userId) {
        return ResponseEntity.ok(userService.getUserDTOById(userId));
    }

    @Override
    public ResponseEntity<UserResponse> validateInfoAndGenerateOtp(UserSignUpWithoutOTPParams params) {
        return ResponseEntity.ok(userService.validateInfoAndGenerateOTP(params));
    }

    @Override
    public ResponseEntity<UserResponse> addNewUser(UserSignUpWithOTPParams params) {
        return new ResponseEntity<>(userService.addNewUser(params), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserResponse> updateUserInfo(UserUpdateInfoParams params) {
        return ResponseEntity.ok(userService.updateUserInfo(params));
    }

    @Override
    public ResponseEntity<String> changePassword(UserChangePasswordParams params) {
        return ResponseEntity.ok(userService.changePassword(params));
    }

    @Override
    public ResponseEntity<UserResponse> validateEmail(UserInputEmailParam param) {
        return ResponseEntity.ok(userService.validateEmail(param));
    }

    @Override
    public ResponseEntity<String> resetPassword(UserInputOTPAndNewPassParams params) {
        return ResponseEntity.ok(userService.resetPassword(params));
    }

    @Override
    public ResponseEntity<List<ClubResponse>> getJoinedClubs() {
        return ResponseEntity.ok(userService.getJoinedClubs());
    }

    @Override
    public ResponseEntity<List<ClubResponse>> getNotJoinedClubs() {
        return ResponseEntity.ok(userService.getNotJoinedClubs());
    }

    @Override
    public ResponseEntity<String> registerToClub(ClubRegisterParam param) {
        return new ResponseEntity<>(userService.registerToClub(param), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> cancelRequest(String clubId) {
        return ResponseEntity.ok(userService.cancelRequest(clubId));
    }

    @Override
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(userService.logout(request, response));
    }
}

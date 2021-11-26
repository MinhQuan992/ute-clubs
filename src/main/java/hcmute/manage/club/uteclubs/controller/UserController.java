package hcmute.manage.club.uteclubs.controller;

import hcmute.manage.club.uteclubs.framework.api.UserAPI;
import hcmute.manage.club.uteclubs.framework.dto.club.ClubRegisterOrCancelRequestParam;
import hcmute.manage.club.uteclubs.framework.dto.club.ClubResponse;
import hcmute.manage.club.uteclubs.framework.dto.user.*;
import hcmute.manage.club.uteclubs.service.ClubService;
import hcmute.manage.club.uteclubs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController implements UserAPI {
    private final UserService userService;
    private final ClubService clubService;

    @Override
    public ResponseEntity<UserResponse> getUser(String userId) {
        return ResponseEntity.ok(userService.getUserDTOById(userId));
    }

    @Override
    public ResponseEntity<UserResponse> validateInfoAndGenerateOtp(UserSignUpWithoutOTPParams params) {
        return new ResponseEntity<>(userService.validateInfoAndGenerateOTP(params), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserResponse> addNewUser(UserSignUpWithOTPParams params) {
        return new ResponseEntity<>(userService.addNewUser(params), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserResponse> updateUserInfo(String userId, UserUpdateInfoParams params) {
        return ResponseEntity.ok(userService.updateUserInfo(userId, params));
    }

    @Override
    public ResponseEntity<UserResponse> changePassword(String userId, UserChangePasswordParams params) {
        return ResponseEntity.ok(userService.changePassword(userId, params));
    }

    @Override
    public ResponseEntity<List<ClubResponse>> getJoinedClubs(String userId, Optional<Integer> page) {
        return ResponseEntity.ok(userService.getJoinedClubs(userId));
    }

    @Override
    public ResponseEntity<String> registerToClub(ClubRegisterOrCancelRequestParam param) {
        return new ResponseEntity<>(userService.registerOrCancelRequest(param, true), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> cancelRequest(ClubRegisterOrCancelRequestParam param) {
        return ResponseEntity.ok(userService.registerOrCancelRequest(param, false));
    }
}

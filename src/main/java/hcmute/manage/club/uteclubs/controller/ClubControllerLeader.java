package hcmute.manage.club.uteclubs.controller;

import hcmute.manage.club.uteclubs.framework.api.ClubAPI_Leader;
import hcmute.manage.club.uteclubs.framework.dto.club.ClubAcceptMemberParam;
import hcmute.manage.club.uteclubs.framework.dto.club.ClubAddPersonParams;
import hcmute.manage.club.uteclubs.framework.dto.club.ClubResponse;
import hcmute.manage.club.uteclubs.framework.dto.user.UserResponse;
import hcmute.manage.club.uteclubs.framework.dto.user_club.UserClubResponse;
import hcmute.manage.club.uteclubs.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ClubControllerLeader implements ClubAPI_Leader {
    private final ClubService clubService;

    @Override
    public ResponseEntity<List<ClubResponse>> getManagedClubs() {
        return ResponseEntity.ok(clubService.getManagedClubs());
    }

    @Override
    public ResponseEntity<ClubResponse> getClubInfo(String clubId) {
        return ResponseEntity.ok(clubService.getClubDTOById(clubId, true));
    }

    @Override
    public ResponseEntity<UserClubResponse> addMember(String clubId, ClubAddPersonParams params) {
        return new ResponseEntity<>(clubService.addPersonToClub(clubId, params, false), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Page<UserResponse>> getMemberRequests(String clubId, Optional<Integer> page) {
        return ResponseEntity.ok(clubService.getMemberRequests(clubId, page));
    }

    @Override
    public ResponseEntity<String> acceptMember(String clubId, ClubAcceptMemberParam param) {
        return ResponseEntity.ok(clubService.acceptMember(clubId, param));
    }

    @Override
    public ResponseEntity<String> rejectMember(String clubId, String userId) {
        return ResponseEntity.ok(clubService.rejectMember(clubId, userId));
    }
}

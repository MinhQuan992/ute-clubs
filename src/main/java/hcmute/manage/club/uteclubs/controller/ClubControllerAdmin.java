package hcmute.manage.club.uteclubs.controller;

import hcmute.manage.club.uteclubs.framework.api.ClubAPI_Admin;
import hcmute.manage.club.uteclubs.framework.dto.club.ClubAddOrUpdateInfoParams;
import hcmute.manage.club.uteclubs.framework.dto.club.ClubAddPersonParams;
import hcmute.manage.club.uteclubs.framework.dto.club.ClubResponse;
import hcmute.manage.club.uteclubs.framework.dto.user_club.UserClubResponse;
import hcmute.manage.club.uteclubs.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ClubControllerAdmin implements ClubAPI_Admin {
    private final ClubService clubService;

    @Override
    public ResponseEntity<Page<ClubResponse>> getClubs(Optional<Integer> page) {
        return ResponseEntity.ok(clubService.getClubs(page));
    }

    @Override
    public ResponseEntity<ClubResponse> getClubById(String clubId) {
        return ResponseEntity.ok(clubService.getClubDTOById(clubId, false));
    }

    @Override
    public ResponseEntity<ClubResponse> addNewClub(ClubAddOrUpdateInfoParams params) {
        return new ResponseEntity<>(clubService.addNewClub(params), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserClubResponse> addPersonToClub(String clubId, ClubAddPersonParams params) {
        return new ResponseEntity<>(clubService.addPersonToClub(clubId, params, true), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ClubResponse> updateInfo(String clubId, ClubAddOrUpdateInfoParams params) {
        return ResponseEntity.ok(clubService.updateInfo(clubId, params));
    }
}
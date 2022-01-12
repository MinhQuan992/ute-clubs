package hcmute.manage.club.uteclubs.controller;

import hcmute.manage.club.uteclubs.framework.api.ClubAPI_User;
import hcmute.manage.club.uteclubs.framework.dto.club.ClubResponse;
import hcmute.manage.club.uteclubs.framework.dto.user.UserResponse;
import hcmute.manage.club.uteclubs.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ClubControllerUser implements ClubAPI_User {
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
    public ResponseEntity<List<UserResponse>> getMembersByRole(String clubId, String role) {
        return ResponseEntity.ok(clubService.getMembersByRole(clubId, role));
    }

    @Override
    public ResponseEntity<String> getRoleInClubOfCurrentUser(String clubId) {
        return ResponseEntity.ok(clubService.getRoleInClubOfCurrentUser(clubId));
    }

    @Override
    public ResponseEntity<String> leaveClub(String clubId) {
        return ResponseEntity.ok(clubService.leaveClub(clubId));
    }
}

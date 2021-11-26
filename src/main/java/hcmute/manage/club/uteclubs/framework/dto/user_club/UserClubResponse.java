package hcmute.manage.club.uteclubs.framework.dto.user_club;

import hcmute.manage.club.uteclubs.model.composite_key.UserClubId;
import lombok.Data;

@Data
public class UserClubResponse {
    private UserClubId id;
    private String roleInClub;
    private boolean accepted;
}

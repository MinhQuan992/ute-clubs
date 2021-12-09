package hcmute.manage.club.uteclubs.framework.dto.club;

import lombok.Data;

@Data
public class ClubResponse {
    private Long clubId;
    private String clubName;
    private String affiliatedUnit;
    private String description;
    private String logoUrl;
}

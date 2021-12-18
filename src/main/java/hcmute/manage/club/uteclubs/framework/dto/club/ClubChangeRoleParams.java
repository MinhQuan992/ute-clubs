package hcmute.manage.club.uteclubs.framework.dto.club;

import hcmute.manage.club.uteclubs.framework.common.RegexConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClubChangeRoleParams {
    @NotBlank(message = "The user ID is required")
    @Pattern(regexp = RegexConstant.COMMON_ID_PATTERN, message = "The user ID must contain numeric characters only")
    private String userId;

    @NotBlank(message = "The new role is required")
    @Pattern(regexp = RegexConstant.ROLE_IN_CLUB_PATTERN, message = "The new role must be ROLE_LEADER, ROLE_MODERATOR or ROLE_MEMBER")
    private String newRole;
}

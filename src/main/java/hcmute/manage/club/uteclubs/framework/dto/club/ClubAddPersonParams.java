package hcmute.manage.club.uteclubs.framework.dto.club;

import hcmute.manage.club.uteclubs.framework.common.RegexConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClubAddPersonParams {
    @NotBlank(message = "The student id is required")
    @Pattern(regexp = RegexConstant.COMMON_ID_PATTERN, message = "The student id must contain only numeric characters")
    @Size(min = 8, max = 8, message = "The length of the student id must be 8 characters")
    private String studentId;

    @NotBlank(message = "The role is required")
    @Pattern(regexp = RegexConstant.ROLE_IN_CLUB_PATTERN, message = "The role must be ROLE_LEADER, ROLE_MODERATOR or ROLE_MEMBER")
    private String roleInClub;
}

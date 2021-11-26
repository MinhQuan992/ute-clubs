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
public class ClubAcceptOrRejectMemberParam {
    @NotBlank(message = "The student id is required")
    @Pattern(regexp = RegexConstant.COMMON_ID_PATTERN, message = "The student id must contain only numeric characters")
    @Size(min = 8, max = 8, message = "The length of the student id must be 8 characters")
    private String studentId;
}

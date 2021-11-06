package hcmute.manage.club.uteclubs.framework.dto.user;

import hcmute.manage.club.uteclubs.framework.common.RegexConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateInfoParams {
    @Size(max = 50, message = "The length of the full name must not be greater than 50 characters")
    private String fullName;

    @Pattern(regexp = RegexConstant.COMMON_ID_PATTERN, message = "The student id must contain only numeric characters")
    @Size(min = 8, max = 8, message = "The length of the student id must be 8 characters")
    private String studentId;

    @Pattern(regexp = RegexConstant.GENDER_PATTERN, message = "The gender must be male or female")
    private String gender;

    @Pattern(regexp = RegexConstant.DATE_PATTERN, message = "The date must be in format YYYY-mm-dd")
    private String dob;

    private String faculty;

    @Size(max = 50, message = "The length of the major's name must not be greater than 50 characters")
    private String major;
}

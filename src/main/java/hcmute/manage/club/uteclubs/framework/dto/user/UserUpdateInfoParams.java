package hcmute.manage.club.uteclubs.framework.dto.user;

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
public class UserUpdateInfoParams {
    @NotBlank(message = "The full name is required")
    @Size(max = 50, message = "The length of the full name must not be greater than 50 characters")
    private String fullName;

    @NotBlank(message = "The gender is required")
    @Pattern(regexp = RegexConstant.GENDER_PATTERN, message = "The gender must be male or female")
    private String gender;

    @NotBlank(message = "The date of birth is required")
    @Pattern(regexp = RegexConstant.DATE_PATTERN, message = "The date must be in format yyyy-MM-dd and must be valid")
    private String dob;

    @NotBlank(message = "The avatar URL is required")
    @Pattern(regexp = RegexConstant.URL_PATTERN, message = "The avatar URL must be valid")
    private String avatarUrl;
}

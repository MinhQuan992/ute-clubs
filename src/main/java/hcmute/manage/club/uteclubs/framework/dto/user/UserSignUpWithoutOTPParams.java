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
public class UserSignUpWithoutOTPParams {
    @NotBlank(message = "The full name is required")
    @Size(max = 50, message = "The length of the full name must not be greater than 50 characters")
    private String fullName;

    @NotBlank(message = "The student ID is required")
    @Pattern(regexp = RegexConstant.COMMON_ID_PATTERN, message = "The student ID must contain numeric characters only")
    @Size(min = 8, max = 8, message = "The length of the student ID must be 8 characters")
    private String studentId;

    @NotBlank(message = "The gender is required")
    @Pattern(regexp = RegexConstant.GENDER_PATTERN, message = "The gender must be male or female")
    private String gender;

    @NotBlank(message = "The date of birth is required")
    @Pattern(regexp = RegexConstant.DATE_PATTERN, message = "The date must be in format yyyy-MM-dd and must be valid")
    private String dob;

    @NotBlank(message = "The faculty is required")
    private String faculty;

    @NotBlank(message = "The major is required")
    @Size(max = 50, message = "The length of the major's name must not be greater than 50 characters")
    private String major;

    @NotBlank(message = "The email is required")
    @Pattern(regexp = RegexConstant.EMAIL_PATTERN, message = "The email must be in right format")
    private String email;

    @NotBlank(message = "The username is required")
    @Pattern(regexp = RegexConstant.USERNAME_PATTERN,
            message = "The username must contain only lowercase letters, uppercase letters and numbers")
    @Size(min = 5, max = 20, message = "The length of the username must be between 5 and 20 characters")
    private String username;

    @NotBlank(message = "The password is required")
    @Pattern(regexp = RegexConstant.PASSWORD_PATTERN,
            message = "The password must contain at least one lowercase letter, one uppercase letter and one number")
    @Size(min = 8, max = 20, message = "The length of the password must be between 8 and 20 characters")
    private String password;

    @NotBlank(message = "The confirmed password is required")
    private String confirmedPassword;

    @NotBlank(message = "The avatar URL is required")
    @Pattern(regexp = RegexConstant.URL_PATTERN, message = "The avatar URL must be valid")
    private String avatarUrl;
}

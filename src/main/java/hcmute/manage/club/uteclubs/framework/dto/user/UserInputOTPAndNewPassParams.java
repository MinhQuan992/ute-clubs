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
public class UserInputOTPAndNewPassParams {
    @NotBlank(message = "The username is required")
    @Pattern(regexp = RegexConstant.USERNAME_PATTERN,
            message = "The username must contain only lowercase letters, uppercase letters and numbers")
    @Size(min = 5, max = 20, message = "The length of the username must be between 5 and 20 characters")
    private String username;

    @NotBlank(message = "The OTP is required")
    @Pattern(regexp = RegexConstant.OTP_PATTERN, message = "The OTP must contain numeric characters only")
    @Size(min = 6, max = 6, message = "The length of the OTP must be 6 characters")
    private String otp;

    @NotBlank(message = "The password is required")
    @Pattern(regexp = RegexConstant.PASSWORD_PATTERN,
            message = "The password must contain at least one lowercase letter, one uppercase letter and one number")
    @Size(min = 8, max = 20, message = "The length of the password must be between 8 and 20 characters")
    private String newPassword;

    @NotBlank(message = "The confirmed password is required")
    private String confirmedPassword;
}

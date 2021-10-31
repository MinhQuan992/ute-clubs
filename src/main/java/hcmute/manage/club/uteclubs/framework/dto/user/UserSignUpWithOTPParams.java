package hcmute.manage.club.uteclubs.framework.dto.user;

import hcmute.manage.club.uteclubs.framework.common.RegexConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpWithOTPParams {
    private String fullName;
    private String studentId;
    private String gender;
    private String dob;
    private String faculty;
    private String major;
    private String email;
    private String username;
    private String password;

    @NotNull(message = "The OTP is required")
    @Pattern(regexp = RegexConstant.OTP_PATTERN,
            message = "The OTP must contain only numeric characters")
    @Size(min = 6, max = 6, message = "The length of the OTP must be 6 characters")
    private String otp;
}

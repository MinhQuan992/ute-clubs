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
public class UserChangePasswordParams {
    @NotBlank(message = "The old password is required")
    private String oldPassword;

    @NotBlank(message = "The new password is required")
    @Pattern(regexp = RegexConstant.PASSWORD_PATTERN,
            message = "The password must contain at least one lowercase letter, one uppercase letter and one number")
    @Size(min = 8, max = 20, message = "The length of the password must be between 8 and 20 characters")
    private String newPassword;

    @NotBlank(message = "The confirmed password is required")
    private String confirmedPassword;
}

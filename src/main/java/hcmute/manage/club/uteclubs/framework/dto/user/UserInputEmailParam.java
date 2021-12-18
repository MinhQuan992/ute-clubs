package hcmute.manage.club.uteclubs.framework.dto.user;

import hcmute.manage.club.uteclubs.framework.common.RegexConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInputEmailParam {
    @NotBlank(message = "The email is required")
    @Pattern(regexp = RegexConstant.EMAIL_PATTERN, message = "The email must be in right format")
    private String email;
}

package hcmute.manage.club.uteclubs.framework.dto.club;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static hcmute.manage.club.uteclubs.framework.common.RegexConstant.COMMON_ID_PATTERN;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClubRegisterParam {
    @NotBlank(message = "The club ID is required")
    @Pattern(regexp = COMMON_ID_PATTERN, message = "The club ID must contain numeric characters only")
    private String clubId;
}

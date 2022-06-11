package hcmute.manage.club.uteclubs.framework.dto.event;

import static hcmute.manage.club.uteclubs.framework.common.RegexConstant.COMMON_ID_PATTERN;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRegisterParam {
  @NotBlank(message = "The event ID is required")
  @Pattern(regexp = COMMON_ID_PATTERN, message = "The event ID must contain numeric characters only")
  private String eventId;
}

package hcmute.manage.club.uteclubs.framework.dto.event;

import hcmute.manage.club.uteclubs.framework.common.RegexConstant;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventSearchByClubAndStatusParams {
  @Pattern(
      regexp = RegexConstant.COMMON_ID_PATTERN,
      message = "The club ID must contain numeric characters only")
  @NotBlank(message = "The clubId is required")
  private String clubId;

  @Pattern(
      regexp = RegexConstant.EVENT_STATUS_PATTERN,
      message = "The status must be past, ongoing or upcoming")
  @NotBlank(message = "The status is required")
  private String status;
}

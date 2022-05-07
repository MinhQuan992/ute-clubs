package hcmute.manage.club.uteclubs.framework.dto.event;

import hcmute.manage.club.uteclubs.framework.common.RegexConstant;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventCreateParams extends EventUpdateParams {
  @NotBlank(message = "The clubId is required")
  @Pattern(
      regexp = RegexConstant.COMMON_ID_PATTERN,
      message = "The club ID must contain numeric characters only")
  private String clubId;
}

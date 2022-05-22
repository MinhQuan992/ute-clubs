package hcmute.manage.club.uteclubs.framework.dto.event;

import hcmute.manage.club.uteclubs.framework.common.RegexConstant;
import hcmute.manage.club.uteclubs.framework.validator.DateTimeConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventSearchParams {
  @NotBlank(message = "The name is required")
  private String name;

  @DateTimeConstraint(message = "Wrong datetime format for startTime")
  private String startTime;

  @DateTimeConstraint(message = "Wrong datetime format for endTime")
  private String endTime;

  @Pattern(
      regexp = RegexConstant.COMMON_ID_PATTERN,
      message = "The club ID must contain numeric characters only")
  private String clubId;
}

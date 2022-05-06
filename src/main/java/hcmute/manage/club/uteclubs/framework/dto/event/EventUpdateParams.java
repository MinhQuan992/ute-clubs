package hcmute.manage.club.uteclubs.framework.dto.event;

import hcmute.manage.club.uteclubs.framework.common.RegexConstant;
import hcmute.manage.club.uteclubs.framework.validator.DateTimeConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventUpdateParams {
  @NotBlank(message = "The name is required")
  private String name;

  @NotBlank(message = "The description is required")
  private String description;

  @NotBlank(message = "The imageUrl is required")
  @Pattern(regexp = RegexConstant.URL_PATTERN, message = "The logo URL must be valid")
  private String imageUrl;

  @NotBlank(message = "The startTime is required")
  @DateTimeConstraint(message = "The startTime must be in format yyyy-MM-dd HH:mm")
  private String startTime;

  @NotBlank(message = "The endTime is required")
  @DateTimeConstraint(message = "The endTime must be in format yyyy-MM-dd HH:mm")
  private String endTime;

  @Min(value = 1, message = "The maximumParticipants must be greater than 1")
  private Integer maximumParticipants;
}

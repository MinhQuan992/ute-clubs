package hcmute.manage.club.uteclubs.framework.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class UserEventResponse extends UserResponse {
  private boolean joined;
}

package hcmute.manage.club.uteclubs.model.composite_key;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserEventId implements Serializable {
  @Column
  private Long userId;

  @Column
  private Long eventId;
}

package hcmute.manage.club.uteclubs.model;

import hcmute.manage.club.uteclubs.model.composite_key.UserEventId;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserEvent {
  @EmbeddedId
  private UserEventId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("userId")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("eventId")
  private Event event;

  @Column(nullable = false)
  private boolean joined;

  public UserEvent(User user, Event event, boolean joined) {
    this.user = user;
    this.event = event;
    this.joined = joined;
    this.id = new UserEventId(user.getUserId(), event.getId());
  }
}

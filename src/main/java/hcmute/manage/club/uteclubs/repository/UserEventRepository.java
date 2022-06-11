package hcmute.manage.club.uteclubs.repository;

import hcmute.manage.club.uteclubs.model.Event;
import hcmute.manage.club.uteclubs.model.User;
import hcmute.manage.club.uteclubs.model.UserEvent;
import hcmute.manage.club.uteclubs.model.composite_key.UserEventId;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEventRepository extends JpaRepository<UserEvent, UserEventId> {
  Optional<UserEvent> findByUserAndEvent(User user, Event event);
  List<UserEvent> findAllByEvent(Event event);
}

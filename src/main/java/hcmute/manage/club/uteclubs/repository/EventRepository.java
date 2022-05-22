package hcmute.manage.club.uteclubs.repository;

import hcmute.manage.club.uteclubs.model.Club;
import hcmute.manage.club.uteclubs.model.Event;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
  Page<Event> findAllByIsActiveIsTrue(Pageable pageable);
  List<Event> findAllByNameContainsAndIsActiveIsTrue(String name);
  List<Event> findAllByClubEqualsAndIsActiveIsTrue(Club club);
}

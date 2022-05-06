package hcmute.manage.club.uteclubs.repository;

import hcmute.manage.club.uteclubs.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
  Page<Event> findAllByIsActiveIsTrue(Pageable pageable);
}

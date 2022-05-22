package hcmute.manage.club.uteclubs.framework.api;

import hcmute.manage.club.uteclubs.framework.common.RegexConstant;
import hcmute.manage.club.uteclubs.framework.dto.event.EventCreateParams;
import hcmute.manage.club.uteclubs.framework.dto.event.EventSearchByClubAndStatusParams;
import hcmute.manage.club.uteclubs.framework.dto.event.EventSearchParams;
import hcmute.manage.club.uteclubs.framework.dto.event.EventUpdateParams;
import hcmute.manage.club.uteclubs.framework.dto.event.EventResponse;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/events")
@Validated
public interface EventAPI {
  @GetMapping
  ResponseEntity<Page<EventResponse>> getEvents(@RequestParam Optional<Integer> page);

  @PostMapping
  ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventCreateParams params);

  @PutMapping("/{eventId}")
  ResponseEntity<EventResponse> updateEvent(
      @PathVariable("eventId")
          @NotBlank(message = "The event ID is required")
          @Pattern(
              regexp = RegexConstant.COMMON_ID_PATTERN,
              message = "The event ID must contain numeric characters only")
          String eventId,
      @Valid @RequestBody EventUpdateParams params);

  @DeleteMapping("/{eventId}")
  ResponseEntity<EventResponse> deleteEvent(
      @PathVariable("eventId")
          @NotBlank(message = "The event ID is required")
          @Pattern(
              regexp = RegexConstant.COMMON_ID_PATTERN,
              message = "The event ID must contain numeric characters only")
          String eventId);

  @PostMapping("/search")
  ResponseEntity<List<EventResponse>> searchEvents(@Valid @RequestBody EventSearchParams params);

  @PostMapping("/search-by-club-and-status")
  ResponseEntity<List<EventResponse>> searchEventsByClubAndStatus(
      @Valid @RequestBody EventSearchByClubAndStatusParams params);
}

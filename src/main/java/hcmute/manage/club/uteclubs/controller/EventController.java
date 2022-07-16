package hcmute.manage.club.uteclubs.controller;

import hcmute.manage.club.uteclubs.framework.api.EventAPI;
import hcmute.manage.club.uteclubs.framework.dto.event.EventCreateParams;
import hcmute.manage.club.uteclubs.framework.dto.event.EventRegisterParam;
import hcmute.manage.club.uteclubs.framework.dto.event.EventResponse;
import hcmute.manage.club.uteclubs.framework.dto.event.EventRollCallParams;
import hcmute.manage.club.uteclubs.framework.dto.event.EventSearchByClubAndStatusParams;
import hcmute.manage.club.uteclubs.framework.dto.event.EventSearchParams;
import hcmute.manage.club.uteclubs.framework.dto.event.EventUpdateParams;
import hcmute.manage.club.uteclubs.framework.dto.user.UserEventResponse;
import hcmute.manage.club.uteclubs.service.EventService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventController implements EventAPI {
  private final EventService eventService;

  @Override
  public ResponseEntity<Page<EventResponse>> getEvents(Optional<Integer> page) {
    return ResponseEntity.ok(eventService.getEvents(page));
  }

  @Override
  public ResponseEntity<EventResponse> createEvent(EventCreateParams params) {
    return new ResponseEntity<>(eventService.createEvent(params), HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<EventResponse> updateEvent(String eventId, EventUpdateParams params) {
    return ResponseEntity.ok(eventService.updateEvent(eventId, params));
  }

  @Override
  public ResponseEntity<EventResponse> deleteEvent(String eventId) {
    return ResponseEntity.ok(eventService.deleteEvent(eventId));
  }

  @Override
  public ResponseEntity<List<EventResponse>> searchEvents(EventSearchParams params) {
    return ResponseEntity.ok(eventService.searchEvents(params));
  }

  @Override
  public ResponseEntity<List<EventResponse>> searchEventsByClubAndStatus(
      EventSearchByClubAndStatusParams params) {
    return ResponseEntity.ok(eventService.searchEventsByClubAndStatus(params));
  }

  @Override
  public ResponseEntity<String> registerToEvent(EventRegisterParam param) {
    return ResponseEntity.ok(eventService.registerToEvent(param));
  }

  @Override
  public ResponseEntity<String> cancelEventRegistration(String eventId) {
    return ResponseEntity.ok(eventService.cancelEventRegistration(eventId));
  }

  @Override
  public ResponseEntity<List<EventResponse>> getRegisteredEventsForUser() {
    return ResponseEntity.ok(eventService.getRegisteredEventsForUser());
  }

  @Override
  public ResponseEntity<List<UserEventResponse>> getParticipantsOfEvent(String eventId) {
    return ResponseEntity.ok(eventService.getParticipantsOfEvent(eventId));
  }

  @Override
  public ResponseEntity<List<UserEventResponse>> getParticipantsOfEvent(String eventId, String query) {
    return ResponseEntity.ok(eventService.searchParticipants(eventId, query));
  }

  @Override
  public ResponseEntity<UserEventResponse> rollCall(EventRollCallParams params) {
    return ResponseEntity.ok(eventService.rollCall(params));
  }
}

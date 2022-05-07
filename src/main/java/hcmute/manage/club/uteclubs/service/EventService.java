package hcmute.manage.club.uteclubs.service;

import hcmute.manage.club.uteclubs.exception.AccessTokenException;
import hcmute.manage.club.uteclubs.exception.InvalidRequestException;
import hcmute.manage.club.uteclubs.exception.NoContentException;
import hcmute.manage.club.uteclubs.exception.NotFoundException;
import hcmute.manage.club.uteclubs.exception.PermissionException;
import hcmute.manage.club.uteclubs.framework.common.CommonConstant;
import hcmute.manage.club.uteclubs.framework.common.ExceptionMessageConstant;
import hcmute.manage.club.uteclubs.framework.dto.event.EventCreateParams;
import hcmute.manage.club.uteclubs.framework.dto.event.EventResponse;
import hcmute.manage.club.uteclubs.framework.dto.event.EventUpdateParams;
import hcmute.manage.club.uteclubs.mapper.EventMapper;
import hcmute.manage.club.uteclubs.model.Club;
import hcmute.manage.club.uteclubs.model.Event;
import hcmute.manage.club.uteclubs.model.User;
import hcmute.manage.club.uteclubs.model.UserClub;
import hcmute.manage.club.uteclubs.repository.ClubRepository;
import hcmute.manage.club.uteclubs.repository.EventRepository;
import hcmute.manage.club.uteclubs.repository.UserClubRepository;
import hcmute.manage.club.uteclubs.repository.UserRepository;
import hcmute.manage.club.uteclubs.utility.UserUtility;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {
  private final EventRepository eventRepository;
  private final ClubRepository clubRepository;
  private final UserRepository userRepository;
  private final UserClubRepository userClubRepository;

  private static final String START_TIME_KEY = "startTime";
  private static final String END_TIME_KEY = "endTime";

  public EventResponse createEvent(EventCreateParams params) {
    Map<String, LocalDateTime> timeMap = getTime(params.getStartTime(), params.getEndTime());
    Club club =
        clubRepository
            .findById(Long.parseLong(params.getClubId()))
            .orElseThrow(() -> new NotFoundException(ExceptionMessageConstant.CLUB_NOT_FOUND));
    validateLeaderModPermissions(club);
    Event event =
        Event.builder()
            .name(params.getName())
            .description(params.getDescription())
            .imageUrl(params.getImageUrl())
            .startTime(timeMap.get(START_TIME_KEY))
            .endTime(timeMap.get(END_TIME_KEY))
            .maximumParticipants(params.getMaximumParticipants())
            .isActive(true)
            .build();
    club.addEvent(event);

    return EventMapper.INSTANCE.eventToEventDTO(eventRepository.save(event));
  }

  public EventResponse updateEvent(String eventId, EventUpdateParams params) {
    Map<String, LocalDateTime> timeMap = getTime(params.getStartTime(), params.getEndTime());
    Event event = getEventById(eventId);

    validateLeaderModPermissions(event.getClub());
    if (params.getMaximumParticipants() < event.getMaximumParticipants()) {
      throw new InvalidRequestException(
          "The new value of maximumParticipants must not be smaller than the old one");
    }

    event.setName(params.getName());
    event.setDescription(params.getDescription());
    event.setImageUrl(params.getImageUrl());
    event.setStartTime(timeMap.get(START_TIME_KEY));
    event.setEndTime(timeMap.get(END_TIME_KEY));
    event.setMaximumParticipants(params.getMaximumParticipants());

    return EventMapper.INSTANCE.eventToEventDTO(eventRepository.save(event));
  }

  private Map<String, LocalDateTime> getTime(String startTimeString, String endTimeString) {
    DateTimeFormatter dateTimeFormatter =
        DateTimeFormatter.ofPattern(CommonConstant.DATE_TIME_PATTERN);
    LocalDateTime startTime = LocalDateTime.parse(startTimeString, dateTimeFormatter);
    LocalDateTime endTime = LocalDateTime.parse(endTimeString, dateTimeFormatter);

    if (!endTime.isAfter(startTime)) {
      throw new InvalidRequestException("The endTime must be after the startTime");
    }

    Map<String, LocalDateTime> timeMap = new HashMap<>();
    timeMap.put(START_TIME_KEY, startTime);
    timeMap.put(END_TIME_KEY, endTime);

    return timeMap;
  }

  private User getCurrentUser() {
    String currentUsername = UserUtility.getCurrentUsername();
    return userRepository
        .findUserByUsername(currentUsername)
        .orElseThrow(
            () ->
                new AccessTokenException(ExceptionMessageConstant.INVALID_OR_MISSED_ACCESS_TOKEN));
  }

  private void validateLeaderModPermissions(Club club) {
    User user = getCurrentUser();
    UserClub userClub =
        userClubRepository
            .findUserClubByUserAndClub(user, club)
            .orElseThrow(() -> new PermissionException("You are not in this club"));

    if (userClub.getRoleInClub().equals("ROLE_MEMBER")) {
      throw new PermissionException("You are not allowed to do this action");
    }
  }

  private Event getEventById(String eventId) {
    Event event =
        eventRepository
            .findById(Long.parseLong(eventId))
            .orElseThrow(() -> new NotFoundException(ExceptionMessageConstant.EVENT_NOT_FOUND));
    if (!event.isActive()) {
      throw new PermissionException(ExceptionMessageConstant.INVALID_EVENT);
    }
    return event;
  }

  public EventResponse deleteEvent(String eventId) {
    Event event = getEventById(eventId);
    validateLeaderModPermissions(event.getClub());
    event.setActive(false);
    return EventMapper.INSTANCE.eventToEventDTO(eventRepository.save(event));
  }

  public Page<EventResponse> getEvents(Optional<Integer> page) {
    Page<Event> result = eventRepository.findAllByIsActiveIsTrue(PageRequest.of(page.orElse(0), 10));
    if (result.isEmpty()) {
      throw new NoContentException();
    }
    return result.map(EventMapper.INSTANCE::eventToEventDTO);
  }
}

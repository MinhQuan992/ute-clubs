package hcmute.manage.club.uteclubs.service;

import hcmute.manage.club.uteclubs.exception.AccessTokenException;
import hcmute.manage.club.uteclubs.exception.InvalidRequestException;
import hcmute.manage.club.uteclubs.exception.NoContentException;
import hcmute.manage.club.uteclubs.exception.NotFoundException;
import hcmute.manage.club.uteclubs.exception.PermissionException;
import hcmute.manage.club.uteclubs.framework.common.CommonConstant;
import hcmute.manage.club.uteclubs.framework.common.ExceptionMessageConstant;
import hcmute.manage.club.uteclubs.framework.dto.event.EventCreateParams;
import hcmute.manage.club.uteclubs.framework.dto.event.EventRegisterParam;
import hcmute.manage.club.uteclubs.framework.dto.event.EventResponse;
import hcmute.manage.club.uteclubs.framework.dto.event.EventRollCallParams;
import hcmute.manage.club.uteclubs.framework.dto.event.EventSearchByClubAndStatusParams;
import hcmute.manage.club.uteclubs.framework.dto.event.EventSearchParams;
import hcmute.manage.club.uteclubs.framework.dto.event.EventUpdateParams;
import hcmute.manage.club.uteclubs.framework.dto.user.UserEventResponse;
import hcmute.manage.club.uteclubs.mapper.EventMapper;
import hcmute.manage.club.uteclubs.mapper.UserEventMapper;
import hcmute.manage.club.uteclubs.model.Club;
import hcmute.manage.club.uteclubs.model.Event;
import hcmute.manage.club.uteclubs.model.User;
import hcmute.manage.club.uteclubs.model.UserClub;
import hcmute.manage.club.uteclubs.model.UserEvent;
import hcmute.manage.club.uteclubs.repository.ClubRepository;
import hcmute.manage.club.uteclubs.repository.EventRepository;
import hcmute.manage.club.uteclubs.repository.UserClubRepository;
import hcmute.manage.club.uteclubs.repository.UserEventRepository;
import hcmute.manage.club.uteclubs.repository.UserRepository;
import hcmute.manage.club.uteclubs.utility.UserUtility;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
  private final UserEventRepository userEventRepository;
  private final UserEventMapper userEventMapper;

  private static final String START_TIME_KEY = "startTime";
  private static final String END_TIME_KEY = "endTime";
  private static final Integer NUMBER_OF_SECONDS_IN_TWO_DAYS = 60 * 60 * 24 * 2;

  public Boolean getEditPermission(String eventId) {
    Event event = getEventById(eventId);
    User user = getCurrentUser();
    Optional<UserClub> userClubOptional =
        userClubRepository.findUserClubByUserAndClub(user, event.getClub());
    if (userClubOptional.isEmpty()) {
      return Boolean.FALSE;
    }

    if (userClubOptional.get().getRoleInClub().equals("ROLE_MEMBER")) {
      return Boolean.FALSE;
    }

    return Boolean.TRUE;
  }

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
    Page<Event> result =
        eventRepository.findAllByIsActiveIsTrue(PageRequest.of(page.orElse(0), 10));
    if (result.isEmpty()) {
      throw new NoContentException();
    }
    return result.map(EventMapper.INSTANCE::eventToEventDTO);
  }

  public List<EventResponse> searchEvents(EventSearchParams params) {
    if ((StringUtils.isNotEmpty(params.getStartTime()) && StringUtils.isEmpty(params.getEndTime()))
        || (StringUtils.isNotEmpty(params.getEndTime())
            && StringUtils.isEmpty(params.getStartTime()))) {
      throw new InvalidRequestException("Start time and end time must be provided together");
    }

    List<Event> events = eventRepository.findAllByNameContainsAndIsActiveIsTrue(params.getName());

    if (StringUtils.isNotEmpty(params.getClubId())) {
      List<Event> eventsFilteredByClub = filterByClub(events, params.getClubId());
      events.removeIf(event -> !eventsFilteredByClub.contains(event));
    }

    if (StringUtils.isNotEmpty(params.getStartTime())
        && StringUtils.isNotEmpty(params.getEndTime())) {
      List<Event> eventFilteredByTime =
          filterByTime(events, params.getStartTime(), params.getEndTime());
      events.removeIf(event -> !eventFilteredByTime.contains(event));
    }

    if (events.isEmpty()) {
      throw new NoContentException();
    }

    return EventMapper.INSTANCE.listEventToListEventDTO(events);
  }

  private List<Event> filterByClub(List<Event> events, String clubId) {
    Club club =
        clubRepository
            .findById(Long.parseLong(clubId))
            .orElseThrow(() -> new NotFoundException(ExceptionMessageConstant.CLUB_NOT_FOUND));

    return events.stream().filter(event -> event.getClub() == club).collect(Collectors.toList());
  }

  private List<Event> filterByTime(List<Event> events, String startTime, String endTime) {
    Map<String, LocalDateTime> timeMap = getTime(startTime, endTime);
    return events.stream()
        .filter(
            event -> isSuitableTime(event, timeMap.get(START_TIME_KEY), timeMap.get(END_TIME_KEY)))
        .collect(Collectors.toList());
  }

  private boolean isSuitableTime(Event event, LocalDateTime startTime, LocalDateTime endTime) {
    return (event.getStartTime().isEqual(startTime) || event.getStartTime().isAfter(startTime))
        && (event.getEndTime().isBefore(endTime) || event.getEndTime().isEqual(endTime));
  }

  public List<EventResponse> searchEventsByClubAndStatus(EventSearchByClubAndStatusParams params) {
    Club club =
        clubRepository
            .findById(Long.parseLong(params.getClubId()))
            .orElseThrow(() -> new NotFoundException(ExceptionMessageConstant.CLUB_NOT_FOUND));

    List<Event> events = eventRepository.findAllByClubEqualsAndIsActiveIsTrue(club);
    LocalDateTime now = LocalDateTime.now();
    List<Event> filteredEvents = switch (params.getStatus()) {
      case "past" -> events.stream().filter(event -> event.getEndTime().isBefore(now)).collect(
          Collectors.toList());
      case "ongoing" -> events.stream()
          .filter(event -> (event.getStartTime().isBefore(now) || event.getStartTime().isEqual(now)) && (event.getEndTime().isAfter(now) || event.getEndTime().equals(now)))
          .collect(
              Collectors.toList());
      default -> events.stream().filter(event -> event.getStartTime().isAfter(now)).collect(
          Collectors.toList());
    };

    if (filteredEvents.isEmpty()) {
      throw new NoContentException();
    }

    return EventMapper.INSTANCE.listEventToListEventDTO(filteredEvents);
  }

  public String registerToEvent(EventRegisterParam param) {
    User currentUser = getCurrentUser();
    Event event = getEventById(param.getEventId());

    Optional<UserEvent> userEvent = userEventRepository.findByUserAndEvent(currentUser, event);
    if (userEvent.isPresent()) {
      throw new InvalidRequestException("You have already registered to this event");
    }

    LocalDateTime now = LocalDateTime.now();
    if (!now.isBefore(event.getStartTime())) {
      throw new InvalidRequestException("Cannot register, the event was started");
    }

    if (event.getParticipants().size() == event.getMaximumParticipants()) {
      throw new InvalidRequestException("Cannot register, the event had enough participants");
    }

    currentUser.addRegisteredEvent(new UserEvent(currentUser, event, false));
    userRepository.save(currentUser);
    return "You have successfully registered to this event";
  }

  public String cancelEventRegistration(String eventId) {
    User currentUser = getCurrentUser();
    Event event = getEventById(eventId);

    Optional<UserEvent> userEvent = userEventRepository.findByUserAndEvent(currentUser, event);
    if (userEvent.isEmpty()) {
      throw new InvalidRequestException("You haven't registered to this event");
    }

    LocalDateTime now = LocalDateTime.now();
    if (!now.isBefore(event.getStartTime())) {
      throw new InvalidRequestException("Cannot cancel, the event was started");
    }

    long differenceSeconds = ChronoUnit.SECONDS.between(now, event.getStartTime());
    if (differenceSeconds < NUMBER_OF_SECONDS_IN_TWO_DAYS) {
      throw new InvalidRequestException("Cannot cancel from this time");
    }

    currentUser.removeRegisteredEvent(userEvent.get());
    userRepository.save(currentUser);
    return "Your registration has been cancelled successfully";
  }

  public List<EventResponse> getRegisteredEventsForUser() {
    User user = getCurrentUser();
    List<Event> registeredEvents = user.getRegisteredEvents()
        .stream()
        .map(UserEvent::getEvent)
        .collect(Collectors.toList());
    registeredEvents.removeIf(event -> !event.isActive());

    if (registeredEvents.isEmpty()) {
      throw new NoContentException();
    }

    return EventMapper.INSTANCE.listEventToListEventDTO(registeredEvents);
  }

  public List<UserEventResponse> getParticipantsOfEvent(String eventId) {
    Event event = getEventById(eventId);
    validateLeaderModPermissions(event.getClub());
    List<UserEvent> participants = userEventRepository.findAllByEvent(event);
    if (participants.isEmpty()) {
      throw new NoContentException();
    }
    return userEventMapper.listUserEventToListUserEventDTO(participants);
  }

  public List<UserEventResponse> searchParticipants(String eventId, String query) {
    if (StringUtils.isBlank(query)) {
      throw new InvalidRequestException("The query is required");
    }
    List<UserEventResponse> participants = getParticipantsOfEvent(eventId);
    List<UserEventResponse> filteredParticipants = participants.stream()
        .filter(participant -> participant.getFullName().contains(query) ||
            participant.getStudentId().contains(query))
        .toList();
    if (filteredParticipants.isEmpty()) {
      throw new NoContentException();
    }
    return filteredParticipants;
  }

  public UserEventResponse rollCall(EventRollCallParams params) {
    Event event = getEventById(params.getEventId());
    validateLeaderModPermissions(event.getClub());
    User user = userRepository.findById(Long.parseLong(params.getUserId()))
        .orElseThrow(() -> new NotFoundException(ExceptionMessageConstant.USER_NOT_FOUND));

    UserEvent userEvent = userEventRepository.findByUserAndEvent(user, event)
        .orElseThrow(() -> new InvalidRequestException("This user hasn't registered to this event"));

    userEvent.setJoined(params.getJoined());
    return userEventMapper.userEventToUserEventDTO(userEventRepository.save(userEvent));
  }
}

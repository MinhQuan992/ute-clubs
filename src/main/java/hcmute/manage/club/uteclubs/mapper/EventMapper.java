package hcmute.manage.club.uteclubs.mapper;

import hcmute.manage.club.uteclubs.framework.dto.event.EventResponse;
import hcmute.manage.club.uteclubs.model.Event;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EventMapper {
  EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

  @Mapping(target = "clubId", source = "club.clubId")
  EventResponse eventToEventDTO(Event event);
  List<EventResponse> listEventToListEventDTO(List<Event> events);
}

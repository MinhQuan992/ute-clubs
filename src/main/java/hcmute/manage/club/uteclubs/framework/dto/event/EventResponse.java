package hcmute.manage.club.uteclubs.framework.dto.event;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class EventResponse {
  private Long id;
  private String name;
  private String description;
  private String imageUrl;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private int maximumParticipants;
  private Long clubId;
}

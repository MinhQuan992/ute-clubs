package hcmute.manage.club.uteclubs.mapper;

import hcmute.manage.club.uteclubs.framework.dto.user.UserEventResponse;
import hcmute.manage.club.uteclubs.model.User;
import hcmute.manage.club.uteclubs.model.UserEvent;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class UserEventMapper {
  public UserEventResponse userEventToUserEventDTO(UserEvent userEvent) {
    User user = userEvent.getUser();
    return UserEventResponse.builder()
        .userId(user.getUserId())
        .fullName(user.getFullName())
        .studentId(user.getStudentId())
        .gender(user.getGender())
        .dob(user.getDob())
        .faculty(user.getFaculty())
        .major(user.getMajor())
        .email(user.getEmail())
        .username(user.getUsername())
        .avatarUrl(user.getAvatarUrl())
        .joined(userEvent.isJoined())
        .build();
  }

  public List<UserEventResponse> listUserEventToListUserEventDTO(List<UserEvent> userEvents) {
    return userEvents.stream().map(this::userEventToUserEventDTO).collect(Collectors.toList());
  }
}

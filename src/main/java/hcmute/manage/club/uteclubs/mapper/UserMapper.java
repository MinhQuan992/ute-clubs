package hcmute.manage.club.uteclubs.mapper;

import hcmute.manage.club.uteclubs.framework.dto.user.UserResponse;
import hcmute.manage.club.uteclubs.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponse userToUserDTO(User user);
    List<UserResponse> listUserToListUserDTO(List<User> userList);
}

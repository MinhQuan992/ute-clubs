package hcmute.manage.club.uteclubs.mapper;

import hcmute.manage.club.uteclubs.framework.dto.user_club.UserClubResponse;
import hcmute.manage.club.uteclubs.model.UserClub;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserClubMapper {
    UserClubMapper INSTANCE = Mappers.getMapper(UserClubMapper.class);

    UserClubResponse userClubToUserClubDTO(UserClub userClub);
}

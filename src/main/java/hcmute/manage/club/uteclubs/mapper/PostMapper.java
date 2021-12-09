package hcmute.manage.club.uteclubs.mapper;

import hcmute.manage.club.uteclubs.framework.dto.post.PostResponse;
import hcmute.manage.club.uteclubs.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mappings({
            @Mapping(target = "authorUsername", source = "author.username"),
            @Mapping(target = "authorFullName", source = "author.fullName"),
            @Mapping(target = "clubId", source = "club.clubId")
    })
    PostResponse postToPostDTO(Post post);

    List<PostResponse> listPostToListPostDTO(List<Post> posts);
}

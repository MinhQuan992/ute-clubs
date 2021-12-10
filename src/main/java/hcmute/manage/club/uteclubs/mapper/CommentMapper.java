package hcmute.manage.club.uteclubs.mapper;

import hcmute.manage.club.uteclubs.framework.dto.comment.CommentResponse;
import hcmute.manage.club.uteclubs.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mappings({
            @Mapping(target = "authorUsername", source = "author.username"),
            @Mapping(target = "authorFullName", source = "author.fullName"),
            @Mapping(target = "authorAvtUrl", source = "author.avatarUrl"),
            @Mapping(target = "postId", source = "post.postId")
    })
    CommentResponse commentToCommentDTO(Comment comment);

    List<CommentResponse> listCommentToListCommentDTO(List<Comment> comments);
}

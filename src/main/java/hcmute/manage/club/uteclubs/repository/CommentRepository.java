package hcmute.manage.club.uteclubs.repository;

import hcmute.manage.club.uteclubs.model.Comment;
import hcmute.manage.club.uteclubs.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsByPost(Post post);
}

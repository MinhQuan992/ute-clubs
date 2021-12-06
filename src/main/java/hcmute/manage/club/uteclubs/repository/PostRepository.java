package hcmute.manage.club.uteclubs.repository;

import hcmute.manage.club.uteclubs.model.Club;
import hcmute.manage.club.uteclubs.model.Post;
import hcmute.manage.club.uteclubs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findPostsByClub(Club club);
    List<Post> findPostsByClubAndAuthor(Club club, User author);
    List<Post> findPostsByClubAndCreatedDateBefore(Club club, Date createdDate);
    List<Post> findPostsByClubAndCreatedDateAfter(Club club, Date createdDate);
}

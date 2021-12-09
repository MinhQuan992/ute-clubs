package hcmute.manage.club.uteclubs.repository;

import hcmute.manage.club.uteclubs.model.Club;
import hcmute.manage.club.uteclubs.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findPostsByClub(Club club);

    @Query("SELECT post FROM Post post " +
            "WHERE post.club = :club AND post.author.username LIKE CONCAT('%', :username, '%')")
    List<Post> getPostsByClubAndAuthorUsername(@Param("club") Club club, @Param("username") String username);

    @Query("SELECT post FROM Post post " +
            "WHERE post.club = :club AND post.author.fullName LIKE CONCAT('%', :fullName, '%')")
    List<Post> getPostByClubAndAuthorFullName(@Param("club") Club club, @Param("fullName") String fullName);

    List<Post> findPostsByClubAndCreatedDateBefore(Club club, Date createdDate);
    List<Post> findPostsByClubAndCreatedDateAfter(Club club, Date createdDate);
}

package hcmute.manage.club.uteclubs.service;

import hcmute.manage.club.uteclubs.exception.NoContentException;
import hcmute.manage.club.uteclubs.exception.NotFoundException;
import hcmute.manage.club.uteclubs.exception.PermissionException;
import hcmute.manage.club.uteclubs.framework.dto.comment.CommentCreateParams;
import hcmute.manage.club.uteclubs.framework.dto.comment.CommentResponse;
import hcmute.manage.club.uteclubs.mapper.CommentMapper;
import hcmute.manage.club.uteclubs.model.*;
import hcmute.manage.club.uteclubs.repository.CommentRepository;
import hcmute.manage.club.uteclubs.repository.PostRepository;
import hcmute.manage.club.uteclubs.repository.UserClubRepository;
import hcmute.manage.club.uteclubs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static hcmute.manage.club.uteclubs.framework.common.ExceptionMessageConstant.*;
import static hcmute.manage.club.uteclubs.utility.UserUtility.getCurrentUsername;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserClubRepository userClubRepository;
    private final CommentRepository commentRepository;

    public List<CommentResponse> getAllComments(String postId) {
        Post post = getPostById(postId);
        Club club = post.getClub();
        User user = getCurrentUser();
        validateMemberPermission(user, club);

        List<Comment> comments = commentRepository.findCommentsByPost(post);
        if (comments.isEmpty()) {
            throw new NoContentException();
        }
        return CommentMapper.INSTANCE.listCommentToListCommentDTO(comments);
    }

    public CommentResponse createComment(CommentCreateParams params) {
        Post post = getPostById(params.getPostId());
        Club club = post.getClub();
        User user = getCurrentUser();
        validateMemberPermission(user, club);

        Comment comment = new Comment();
        comment.setContent(params.getContent());
        comment.setCreatedDate(new Date());
        comment.setAuthor(user);
        comment.setPost(post);

        return CommentMapper.INSTANCE.commentToCommentDTO(commentRepository.save(comment));
    }

    public String deleteComment(String commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(Long.parseLong(commentId));
        if (commentOptional.isEmpty()) {
            throw new NotFoundException(COMMENT_NOT_FOUND);
        }

        Comment comment = commentOptional.get();
        Post post = comment.getPost();
        Club club = post.getClub();
        User user = getCurrentUser();
        validateLeaderModPermissions(user, club);

        commentRepository.delete(comment);
        return "This comment has been deleted successfully";
    }

    private Post getPostById(String postId) {
        Optional<Post> postOptional = postRepository.findById(Long.parseLong(postId));
        if (postOptional.isEmpty()) {
            throw new NotFoundException(POST_NOT_FOUND);
        }
        return postOptional.get();
    }

    private User getCurrentUser() {
        String currentUsername = getCurrentUsername();
        return userRepository.findUserByUsername(currentUsername).get();
    }

    private void validateMemberPermission(User user, Club club) {
        Optional<UserClub> userClub = userClubRepository.findUserClubByUserAndClub(user, club);
        boolean isClubMember = userClub.isPresent() && userClub.get().isAccepted();
        if (!isClubMember) {
            throw new PermissionException(NOT_MEMBER);
        }
    }

    private void validateLeaderModPermissions(User user, Club club) {
        Optional<UserClub> userClub = userClubRepository.findUserClubByUserAndClub(user, club);
        boolean isLeaderOrMod = userClub.isPresent() && !userClub.get().getRoleInClub().equals("ROLE_MEMBER");
        if (!isLeaderOrMod) {
            throw new PermissionException("You are not allowed to delete this comment");
        }
    }
}

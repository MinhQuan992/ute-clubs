package hcmute.manage.club.uteclubs.service;

import hcmute.manage.club.uteclubs.exception.NoContentException;
import hcmute.manage.club.uteclubs.exception.NotFoundException;
import hcmute.manage.club.uteclubs.exception.PermissionException;
import hcmute.manage.club.uteclubs.framework.dto.post.PostCreateParams;
import hcmute.manage.club.uteclubs.framework.dto.post.PostResponse;
import hcmute.manage.club.uteclubs.framework.dto.post.PostSearchParams;
import hcmute.manage.club.uteclubs.mapper.PostMapper;
import hcmute.manage.club.uteclubs.model.*;
import hcmute.manage.club.uteclubs.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static hcmute.manage.club.uteclubs.framework.common.ExceptionMessageConstant.*;
import static hcmute.manage.club.uteclubs.utility.DateUtilities.convertToUTC;
import static hcmute.manage.club.uteclubs.utility.UserUtility.getCurrentUsername;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final UserClubRepository userClubRepository;
    private final CommentRepository commentRepository;

    public List<PostResponse> getAllPosts(String clubId) {
        Club club = getClubById(clubId);
        User currentUser = getCurrentUser();
        validateMemberPermission(currentUser, club);

        List<Post> posts = postRepository.findPostsByClub(club);
        if (posts.isEmpty()) {
            throw new NoContentException();
        }
        return PostMapper.INSTANCE.listPostToListPostDTO(posts);
    }

    public PostResponse createPost(PostCreateParams params) {
        Club club = getClubById(params.getClubId());
        User currentUser = getCurrentUser();
        validateMemberPermission(currentUser, club);

        Post post = new Post();
        post.setContent(params.getContent());
        post.setImageUrl(params.getImageUrl());
        post.setCreatedDate(new Date());
        post.setAuthor(currentUser);
        post.setClub(club);

        return PostMapper.INSTANCE.postToPostDTO(postRepository.save(post));
    }

    public String deletePost(String postId) {
        Optional<Post> postOptional = postRepository.findById(Long.parseLong(postId));
        if (postOptional.isEmpty()) {
            throw new NotFoundException(POST_NOT_FOUND);
        }

        Post post = postOptional.get();
        User currentUser = getCurrentUser();
        validateLeaderModPermissions(currentUser, post.getClub());

        List<Comment> comments = commentRepository.findCommentsByPost(post);
        commentRepository.deleteAll(comments);
        postRepository.delete(post);
        return "This post has been deleted successfully";
    }

    public List<PostResponse> searchPost(PostSearchParams params) throws ParseException {
        Club club = getClubById(params.getClubId());
        User currentUser = getCurrentUser();
        validateMemberPermission(currentUser, club);

        List<Post> postsInClub = postRepository.findPostsByClub(club);
        if (postsInClub.isEmpty()) {
            throw new NoContentException();
        }

        List<Post> postListOne = new ArrayList<>();
        List<Post> postListTwo = new ArrayList<>();

        String searchQuery = params.getSearchQuery();
        if (!StringUtils.isEmpty(searchQuery)) {
            postListOne = postRepository.getPostsByClubAndAuthorUsername(club, searchQuery);
            if (postListOne.isEmpty()) {
                postListOne = postRepository.getPostByClubAndAuthorFullName(club, searchQuery);
            }
        }

        String dateQuery = params.getDateQuery();
        if (!StringUtils.isEmpty(dateQuery)) {
            String prefix = dateQuery.substring(0, 2);
            String dateInString = dateQuery.substring(2);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date localDate = simpleDateFormat.parse(dateInString);
            Date utcDate = convertToUTC(localDate);

            switch (prefix) {
                case "lt" -> postListTwo = postRepository.findPostsByClubAndCreatedDateBefore(club, utcDate);
                case "gt" -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(utcDate);
                    calendar.add(Calendar.DATE, 1);
                    utcDate = calendar.getTime();
                    postListTwo = postRepository.findPostsByClubAndCreatedDateAfter(club, utcDate);
                }
                default -> {
                    postListTwo = postRepository.findPostsByClubAndCreatedDateAfter(club, utcDate);
                    Date newDate;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(utcDate);
                    calendar.add(Calendar.DATE, 1);
                    newDate = calendar.getTime();
                    List<Post> postsAfterNewDate = postRepository.findPostsByClubAndCreatedDateAfter(club, newDate);
                    postListTwo.removeAll(postsAfterNewDate);
                }
            }
        }

        if (postListOne.isEmpty() && postListTwo.isEmpty()) {
            throw new NoContentException();
        }

        if (postListOne.isEmpty() && StringUtils.isEmpty(searchQuery)) {
            return PostMapper.INSTANCE.listPostToListPostDTO(postListTwo);
        }

        if (postListTwo.isEmpty() && StringUtils.isEmpty(dateQuery)) {
            return PostMapper.INSTANCE.listPostToListPostDTO(postListOne);
        }

        List<Post> finalResult = postListOne.stream()
                .distinct()
                .filter(postListTwo::contains)
                .collect(Collectors.toList());
        if (finalResult.isEmpty()) {
            throw new NoContentException();
        }
        return PostMapper.INSTANCE.listPostToListPostDTO(finalResult);
    }

    private Club getClubById(String clubId) {
        Optional<Club> clubOptional = clubRepository.findById(Long.parseLong(clubId));
        if (clubOptional.isEmpty()) {
            throw new NotFoundException(CLUB_NOT_FOUND);
        }
        return clubOptional.get();
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
            throw new PermissionException("You are not allowed to delete this post");
        }
    }
}

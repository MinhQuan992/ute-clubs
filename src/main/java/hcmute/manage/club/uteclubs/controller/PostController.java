package hcmute.manage.club.uteclubs.controller;

import hcmute.manage.club.uteclubs.framework.api.PostAPI;
import hcmute.manage.club.uteclubs.framework.dto.post.PostCreateParams;
import hcmute.manage.club.uteclubs.framework.dto.post.PostResponse;
import hcmute.manage.club.uteclubs.framework.dto.post.PostSearchParams;
import hcmute.manage.club.uteclubs.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController implements PostAPI {
    private final PostService postService;

    @Override
    public ResponseEntity<List<PostResponse>> getAllByClub(String clubId) {
        return ResponseEntity.ok(postService.getAllPosts(clubId));
    }

    @Override
    public ResponseEntity<PostResponse> createPost(PostCreateParams params) {
        return new ResponseEntity<>(postService.createPost(params), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> deletePost(String postId) {
        return ResponseEntity.ok(postService.deletePost(postId));
    }

    @Override
    public ResponseEntity<List<PostResponse>> searchPosts(PostSearchParams params) throws ParseException {
        return ResponseEntity.ok(postService.searchPost(params));
    }
}

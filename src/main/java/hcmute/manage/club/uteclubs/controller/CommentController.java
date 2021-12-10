package hcmute.manage.club.uteclubs.controller;

import hcmute.manage.club.uteclubs.framework.api.CommentAPI;
import hcmute.manage.club.uteclubs.framework.dto.comment.CommentCreateParams;
import hcmute.manage.club.uteclubs.framework.dto.comment.CommentResponse;
import hcmute.manage.club.uteclubs.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController implements CommentAPI {
    private final CommentService commentService;

    @Override
    public ResponseEntity<List<CommentResponse>> getAllByPost(String postId) {
        return ResponseEntity.ok(commentService.getAllComments(postId));
    }

    @Override
    public ResponseEntity<CommentResponse> createComment(CommentCreateParams params) {
        return ResponseEntity.ok(commentService.createComment(params));
    }

    @Override
    public ResponseEntity<String> deleteComment(String commentId) {
        return ResponseEntity.ok(commentService.deleteComment(commentId));
    }
}

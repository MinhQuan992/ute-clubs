package hcmute.manage.club.uteclubs.framework.api;

import hcmute.manage.club.uteclubs.framework.common.RegexConstant;
import hcmute.manage.club.uteclubs.framework.dto.comment.CommentCreateParams;
import hcmute.manage.club.uteclubs.framework.dto.comment.CommentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@RequestMapping("/comments")
@Validated
public interface CommentAPI {
    @GetMapping("/get-by-post/{postId}")
    ResponseEntity<List<CommentResponse>> getAllByPost(
            @PathVariable("postId")
            @NotBlank(message = "The post ID is required")
            @Pattern(regexp = RegexConstant.COMMON_ID_PATTERN, message = "The post ID must contain numeric characters only")
                    String postId
    );

    @PostMapping
    ResponseEntity<CommentResponse> createComment(@Valid @RequestBody CommentCreateParams params);

    @DeleteMapping("/{commentId}")
    ResponseEntity<String> deleteComment(
            @PathVariable("commentId")
            @NotBlank(message = "The comment ID is required")
            @Pattern(regexp = RegexConstant.COMMON_ID_PATTERN, message = "The comment ID must contain numeric characters only")
                    String commentId
    );
}

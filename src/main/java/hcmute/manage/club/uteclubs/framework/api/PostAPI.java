package hcmute.manage.club.uteclubs.framework.api;

import hcmute.manage.club.uteclubs.framework.common.RegexConstant;
import hcmute.manage.club.uteclubs.framework.dto.post.PostCreateParams;
import hcmute.manage.club.uteclubs.framework.dto.post.PostResponse;
import hcmute.manage.club.uteclubs.framework.dto.post.PostSearchParams;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.text.ParseException;
import java.util.List;

@RequestMapping("/posts")
@Validated
public interface PostAPI {
    @GetMapping("/get-by-club/{clubId}")
    ResponseEntity<List<PostResponse>> getAllByClubs(
            @PathVariable("clubId")
            @NotBlank(message = "The club ID is required")
            @Pattern(regexp = RegexConstant.COMMON_ID_PATTERN, message = "The club ID must contain numeric characters only")
                    String clubId
    );

    @PostMapping
    ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostCreateParams params);

    @DeleteMapping("/{postId}")
    ResponseEntity<String> deletePost(
            @PathVariable("postId")
            @NotBlank(message = "The post ID is required")
            @Pattern(regexp = RegexConstant.COMMON_ID_PATTERN, message = "The post ID must contain numeric characters only")
                    String postId
    );

    @PostMapping("/search")
    ResponseEntity<List<PostResponse>> searchPosts(@Valid @RequestBody PostSearchParams params) throws ParseException;
}

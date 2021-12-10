package hcmute.manage.club.uteclubs.framework.dto.comment;

import hcmute.manage.club.uteclubs.framework.common.RegexConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateParams {
    @NotBlank(message = "The post ID is required")
    @Pattern(regexp = RegexConstant.COMMON_ID_PATTERN, message = "The post ID must contain numeric characters only")
    private String postId;

    @NotBlank(message = "The content is required")
    @Size(min = 5, message = "The length of the content must be at least 5 characters")
    private String content;
}

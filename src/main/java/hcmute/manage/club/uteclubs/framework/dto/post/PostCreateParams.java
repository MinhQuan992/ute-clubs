package hcmute.manage.club.uteclubs.framework.dto.post;

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
public class PostCreateParams {
    @NotBlank(message = "The club ID is required")
    @Pattern(regexp = RegexConstant.COMMON_ID_PATTERN, message = "The club ID must contain numeric characters only")
    private String clubId;

    @NotBlank(message = "The content is required")
    @Size(min = 10, message = "The length of the content must be at least 10 characters")
    private String content;

    @Pattern(regexp = RegexConstant.URL_PATTERN, message = "The image URL must be valid")
    private String imageUrl;
}

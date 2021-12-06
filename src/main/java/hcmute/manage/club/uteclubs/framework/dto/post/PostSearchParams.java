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
public class PostSearchParams {
    @NotBlank(message = "The club ID is required")
    @Pattern(regexp = RegexConstant.COMMON_ID_PATTERN, message = "The club ID must contain numeric characters only")
    private String clubId;

    @Size(max = 20, message = "The length of the query must not be greater than 20 characters")
    private String searchQuery;

    @Pattern(regexp = RegexConstant.DATE_WITH_PREFIX_PATTERN,
            message = "The date query must be in format <prefix>YYYY-mm-dd and must be valid. The prefix must be one of these prefixes: lt, eq, gt")
    private String dateQuery;
}

package hcmute.manage.club.uteclubs.framework.dto.post;

import lombok.Data;

import java.util.Date;

@Data
public class PostResponse {
    private Long postId;
    private String content;
    private String imageUrl;
    private Date createdDate;
    private String authorUsername;
    private String authorFullName;
    private Long clubId;
}

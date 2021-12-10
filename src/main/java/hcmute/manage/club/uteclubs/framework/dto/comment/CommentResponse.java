package hcmute.manage.club.uteclubs.framework.dto.comment;

import lombok.Data;

import java.util.Date;

@Data
public class CommentResponse {
    private Long commentId;
    private String content;
    private Date createdDate;
    private String authorUsername;
    private String authorFullName;
    private String authorAvtUrl;
    private Long postId;
}

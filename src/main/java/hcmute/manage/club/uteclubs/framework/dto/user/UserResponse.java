package hcmute.manage.club.uteclubs.framework.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long userId;
    private String fullName;
    private String studentId;
    private String gender;
    private Date dob;
    private String faculty;
    private String major;
    private String email;
    private String username;
    private String avatarUrl;
}

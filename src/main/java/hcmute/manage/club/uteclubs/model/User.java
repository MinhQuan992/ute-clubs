package hcmute.manage.club.uteclubs.model;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(length = 50, nullable = false)
    private String fullName;

    @Column(length = 20, nullable = false, unique = true)
    private String studentId;

    @Column(length = 6, nullable = false)
    private String gender;

    @Column(nullable = false)
    private Date dob;

    @Column(length = 50, nullable = false)
    private String faculty;

    @Column(length = 50, nullable = false)
    private String major;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 20, nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> userRoles;
}

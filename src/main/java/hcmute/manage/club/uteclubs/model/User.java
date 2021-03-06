package hcmute.manage.club.uteclubs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(length = 50, nullable = false)
    private String fullName;

    @Column(length = 8, nullable = false, unique = true)
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

    @Column(length = 10, nullable = false)
    private String role;

    @Column
    private String avatarUrl;

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<UserClub> participatedClubs;

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<UserEvent> registeredEvents;

    @OneToMany(mappedBy = "author")
    private List<Post> posts;

    @OneToMany(mappedBy = "author")
    private List<Comment> comments;

    public void addRegisteredEvent(UserEvent event) {
        this.registeredEvents.add(event);
    }

    public void removeRegisteredEvent(UserEvent event) {
        event.setUser(null);
        this.registeredEvents.remove(event);
    }
}

package hcmute.manage.club.uteclubs.model;

import hcmute.manage.club.uteclubs.model.composite_key.UserClubId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class UserClub {
    @EmbeddedId
    private UserClubId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("clubId")
    private Club club;

    @Column(length = 14, nullable = false)
    private String roleInClub;

    @Column(nullable = false)
    private boolean accepted;

    public UserClub() {

    }

    public UserClub(User user, Club club, String roleInClub, boolean accepted) {
        this.user = user;
        this.club = club;
        this.roleInClub = roleInClub;
        this.accepted = accepted;
        this.id = new UserClubId(user.getUserId(), club.getClubId());
    }
}

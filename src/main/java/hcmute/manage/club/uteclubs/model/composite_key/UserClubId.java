package hcmute.manage.club.uteclubs.model.composite_key;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserClubId implements Serializable {
    @Column
    private Long userId;

    @Column
    private Long clubId;

    public UserClubId() {

    }

    public UserClubId(Long userId, Long clubId) {
        this.userId = userId;
        this.clubId = clubId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserClubId that = (UserClubId) o;
        return userId.equals(that.userId) && clubId.equals(that.clubId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, clubId);
    }
}

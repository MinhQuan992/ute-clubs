package hcmute.manage.club.uteclubs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clubId;

    @Column(nullable = false)
    private String clubName;

    @Column(nullable = false)
    private String affiliatedUnit;

    @Column
    @Type(type = "text")
    private String description;

    @Column
    private String logoUrl;

    @OneToMany(
            mappedBy = "club",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<UserClub> members;

    @OneToMany(mappedBy = "club")
    private List<Post> posts;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> events;

    public void addEvent(Event event) {
        this.events.add(event);
        event.setClub(this);
    }

    public void removeEvent(Event event) {
        event.setClub(null);
        this.events.remove(event);
    }
}

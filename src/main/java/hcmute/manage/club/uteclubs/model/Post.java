package hcmute.manage.club.uteclubs.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    @Type(type = "text")
    private String content;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    private Date createdDate;

    @ManyToOne
    private User author;

    @ManyToOne
    private Club club;
}

package hcmute.manage.club.uteclubs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    @Type(type = "text")
    private String content;

    @Column(nullable = false)
    private Date createdDate;

    @ManyToOne
    private User author;

    @ManyToOne
    private Post post;
}

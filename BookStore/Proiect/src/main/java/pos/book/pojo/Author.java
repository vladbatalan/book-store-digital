package pos.book.pojo;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity(name="author")
@Table(name="author")
public class Author extends RepresentationModel<Author> implements Serializable {

    @Id
    @GeneratedValue
    @Column(name="id_author")
    private Integer idAuthor;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;


}

package pos.book.pojo;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import pos.book.pojo.id.BookAuthorId;

import javax.persistence.*;
import java.io.Serializable;


@Data
@Entity(name="book_author")
@IdClass(BookAuthorId.class)
@Table(name="book_author")
public class BookAuthor extends RepresentationModel<BookAuthor> implements Serializable {

    @Id
    @Column(name="isbn")
    private String isbn;

    @Id
    @Column(name="id_author")
    private Integer idAuthor;

    @GeneratedValue
    @Column(name="list_index")
    private Integer listIndex;


}

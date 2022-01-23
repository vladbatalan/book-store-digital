package pos.book.model.pojo.erd;


import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity(name = "book")
@Table(name = "book")
public class Book extends RepresentationModel<Book> implements Serializable {

    @Id
    @Column(name = "isbn", unique = true, nullable = false)
    private String isbn;

    @Column(name = "title", unique = true, nullable = false)
    private String title;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "publishing_year")
    private Integer publishingYear;

    @Column(name = "category")
    private String category;

    @Column(name = "quantity", columnDefinition = "integer default 0")
    private Integer quantity;

}

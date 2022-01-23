package pos.book.model.pojo.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import pos.book.model.pojo.erd.Book;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class BookNoVerbose  extends RepresentationModel<BookNoVerbose>  implements Serializable {
    private String isbn;
    private String title;
    private String category;

    public BookNoVerbose(Book book){
        isbn = book.getIsbn();
        title = book.getTitle();
        category = book.getCategory();
    }
}

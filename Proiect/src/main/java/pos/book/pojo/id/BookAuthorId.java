package pos.book.pojo.id;

import lombok.Data;

import java.io.Serializable;

@Data
public class BookAuthorId implements Serializable {
    private String isbn;
    private Integer idAuthor;

    public BookAuthorId() {

    }

    public BookAuthorId(String isbn, Integer idAuthor) {
        this.isbn = isbn;
        this.idAuthor = idAuthor;
    }
}

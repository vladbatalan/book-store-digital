package pos.book.model.pojo.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import pos.book.model.pojo.erd.Book;

import java.io.Serializable;


@Data
public class BookMinimal implements Serializable {
    private String isbn;
    private Integer quantity;

    public BookMinimal() {}
    public BookMinimal(String isbn, Integer quantity) {
        this.isbn = isbn;
        this.quantity = quantity;
    }

    public BookMinimal(Book readValue) {
        isbn = readValue.getIsbn();
        quantity = readValue.getQuantity();
    }
}
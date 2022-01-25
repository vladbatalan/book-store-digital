package paw.command.command.model.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BookMinimal implements Serializable {
    private String isbn;
    private Integer quantity;

    public BookMinimal() {}

    public BookMinimal(Book readValue) {
        isbn = readValue.getIsbn();
        quantity = readValue.getQuantity();
    }
}

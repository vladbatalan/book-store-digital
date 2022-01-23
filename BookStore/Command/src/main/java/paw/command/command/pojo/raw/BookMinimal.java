package paw.command.command.pojo.raw;

import lombok.Data;

import java.io.Serializable;

@Data
public class BookMinimal implements Serializable {
    private String isbn;
    private Integer quantity;
}

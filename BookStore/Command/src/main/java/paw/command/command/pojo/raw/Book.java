package paw.command.command.pojo.raw;


import lombok.Data;

import java.io.Serializable;

@Data
public class Book implements Serializable {

    private String isbn;

    private String title;

    private String publisher;

    private Integer publishingYear;

    private String category;

}

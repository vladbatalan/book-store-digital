package paw.command.command.model.pojo.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class Book  extends RepresentationModel<Book> implements Serializable{

    private String isbn;

    private String title;

    private String publisher;

    private Integer publishingYear;

    private String category;

    private Integer quantity;

    @JsonIgnore
    private String _links;
}

package paw.command.command.model.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class Author implements Serializable {

    private Integer idAuthor;

    private String firstName;

    private String lastName;


}

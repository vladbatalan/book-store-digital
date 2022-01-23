package paw.command.command.pojo.raw;

import lombok.Data;

import java.io.Serializable;

@Data
public class Author implements Serializable {

    private Integer idAuthor;

    private String firstName;

    private String lastName;


}

package pos.book.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pos.book.pojo.Author;

import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Integer>, JpaSpecificationExecutor<Author> {
    List<Author> findAll();
    Author findAuthorByFirstNameAndLastName(String firstName, String lastName);

    @Query(
            value = "SELECT * FROM author a WHERE " +
                    "UPPER(CONCAT(a.last_name, \" \", a.first_name)) LIKE CONCAT(\"%\",UPPER(?1), \"%\") OR " +
                    "UPPER(CONCAT(a.first_name, \" \", a.last_name)) LIKE CONCAT(\"%\",UPPER(?1), \"%\")",
            nativeQuery = true
    )
    List<Author> findByNameLike(String name);

    @Query(
            value="SELECT * FROM author a WHERE " +
                    "UPPER(CONCAT(a.last_name, \" \", a.first_name)) = UPPER(?1) OR " +
                    "UPPER(CONCAT(a.first_name, \" \", a.last_name)) = UPPER(?1)",
            nativeQuery = true
    )
    List<Author> findAuthorByName(String name);


}


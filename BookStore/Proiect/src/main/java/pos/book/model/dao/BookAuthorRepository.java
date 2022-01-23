package pos.book.model.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pos.book.model.pojo.erd.BookAuthor;
import pos.book.model.pojo.erd.id.BookAuthorId;

import javax.transaction.Transactional;
import java.util.List;

public interface BookAuthorRepository extends CrudRepository<BookAuthor, BookAuthorId> {
    List<BookAuthor> findAllByIsbn(String isbn);

    @Query(value="SELECT COUNT(*) + 1 FROM book_author ab GROUP BY ab.isbn HAVING ab.isbn = ?1", nativeQuery = true)
    Integer getNextIndex(String isbn);

    BookAuthor findByIsbnAndIdAuthor(String isbn, Integer idAuthor);

    @Transactional
    long deleteByIsbnAndIdAuthor(String isbn, Integer idAuthor);

    @Transactional
    long deleteAllByIsbn(String isbn);

}

package pos.book.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pos.book.pojo.Author;
import pos.book.pojo.BookAuthor;
import pos.book.pojo.id.BookAuthorId;

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

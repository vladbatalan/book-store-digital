package pos.book.model.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import pos.book.model.pojo.erd.Book;

import java.util.List;

public interface BookRepository extends PagingAndSortingRepository<Book, String> {

    Book findByTitle(String title);

    List<Book> findByPublishingYear(Integer publishingYear);
    Page<Book> findByPublishingYear(Integer publishingYear, Pageable pageable);

    List<Book> findByCategory(String category);
    Page<Book> findByCategory(String category, Pageable pageable);

    List<Book> findByCategoryAndPublishingYear(String category, Integer publishingYear);
    Page<Book> findByCategoryAndPublishingYear(String category, Integer publishingYear, Pageable pageable);

}

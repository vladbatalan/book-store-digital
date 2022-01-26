package pos.book.service;


import pos.book.model.pojo.dto.BookMinimal;
import pos.book.model.pojo.dto.BookNoVerbose;
import pos.book.model.pojo.erd.Book;
import pos.book.model.pojo.erd.BookAuthor;

import java.util.List;

public interface BookService {

    List<Book> getBooksByCategoryOrYear(String category, Integer publishingYear, Integer page, Integer itemsPerPage);
    List<Book> getAllBooks(Integer page, Integer itemsPerPage);

    Book getBook(String isbn);
    Book deleteBook(String isbn);
    Book addBook(Book book);
    Book updateBook(Book book);


    List<BookAuthor> getBookAuthors(String isbn);
    BookAuthor addAuthor(String isbn, Integer idAuthor);
    long deleteAuthor(String isbn, Integer idAuthor);
    long deleteAllAuthors(String isbn);

    boolean validateBooksAndUpdateQuantity(List<BookMinimal> bookList);
}


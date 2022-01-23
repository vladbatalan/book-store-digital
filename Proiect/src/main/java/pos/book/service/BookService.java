package pos.book.service;


import pos.book.pojo.Author;
import pos.book.pojo.Book;
import pos.book.pojo.BookAuthor;

import java.util.List;

public interface BookService {

    List<Book> getAllBooks();
    List<Book> getBooksByCategoryOrYear(String category, Integer publishingYear);


    List<Book> getAllBooks(Integer page, Integer itemsPerPage);
    List<Book> getBooksByCategoryOrYear(String category, Integer publishingYear, Integer page, Integer itemsPerPage);

    Book getBook(String isbn);
    Book deleteBook(String isbn);
    Book addBook(Book book);
    Book updateBook(Book book);


    List<BookAuthor> getBookAuthors(String isbn);
    BookAuthor addAuthor(String isbn, Integer idAuthor);
    long deleteAuthor(String isbn, Integer idAuthor);
    long deleteAllAuthors(String isbn);
}

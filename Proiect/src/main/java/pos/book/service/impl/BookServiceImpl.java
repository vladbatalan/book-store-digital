package pos.book.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pos.book.dao.BookAuthorRepository;
import pos.book.dao.BookRepository;
import pos.book.pojo.Book;
import pos.book.pojo.BookAuthor;
import pos.book.service.BookService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;
    private BookAuthorRepository bookAuthorRepository;

    public BookServiceImpl(BookRepository bookRepository, BookAuthorRepository bookAuthorRepository) {
        this.bookRepository = bookRepository;
        this.bookAuthorRepository = bookAuthorRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        return (List<Book>) bookRepository.findAll();
    }

    @Override
    public List<Book> getBooksByCategoryOrYear(String category, Integer publishingYear) {

        if (category != null && publishingYear != null)
            return bookRepository.findByCategoryAndPublishingYear(category, publishingYear);
        if (category != null)
            return bookRepository.findByCategory(category);
        if (publishingYear != null)
            return bookRepository.findByPublishingYear(publishingYear);
        return null;
    }

    @Override
    public List<Book> getAllBooks(Integer page, Integer itemsPerPage) {
        Pageable pageable = PageRequest.of(page, itemsPerPage);
        return bookRepository.findAll(pageable).getContent();
    }

    @Override
    public List<Book> getBooksByCategoryOrYear(String category, Integer publishingYear, Integer page, Integer itemsPerPage) {
        Pageable pageable = PageRequest.of(page, itemsPerPage);
        if (category != null && publishingYear != null)
            return bookRepository.findByCategoryAndPublishingYear(category, publishingYear, pageable).getContent();
        if (category != null)
            return bookRepository.findByCategory(category, pageable).getContent();
        if (publishingYear != null)
            return bookRepository.findByPublishingYear(publishingYear, pageable).getContent();
        return null;
    }

    @Override
    public Book getBook(String isbn) {
        Optional<Book> book = bookRepository.findById(isbn);
        return book.orElse(null);
    }

    @Override
    public Book deleteBook(String isbn) {

        Optional<Book> book = bookRepository.findById(isbn);
        if (book.isPresent()) {

            bookRepository.delete(book.get());
            return book.get();
        }
        return null;
    }

    @Override
    public Book addBook(Book book) {

        Optional<Book> otherBookOptional = bookRepository.findById(book.getIsbn());

        // nu exista carte
        if (otherBookOptional.isPresent()) {
            return null;
        }

        // se cauta sa nu fie acelasi titlu
        Book otherBook = bookRepository.findByTitle(book.getTitle());

        if (otherBook != null) {
            return null;
        }

        // se adauga cartea
        bookRepository.save(book);
        return book;
    }

    @Override
    public Book updateBook(Book book) {
        Optional<Book> otherBookOptional = bookRepository.findById(book.getIsbn());

        // Trebuie sa existe cartea
        if (otherBookOptional.isEmpty()) {
            return null;
        }

        Book otherBook = otherBookOptional.get();

        // Verifica daca titlul e diferit sa nu mai existe un alt titlu la fel
        if (otherBook.getTitle() != null && !Objects.equals(book.getTitle(), otherBook.getTitle())) {
            Book sameTitle = bookRepository.findByTitle(book.getTitle());

            // Exista
            if (sameTitle != null)
                return null;
        }


        // Schimbam elementele care sunt diferite
        if (book.getTitle() != null)
            otherBook.setTitle(book.getTitle());

        if (book.getPublisher() != null)
            otherBook.setPublisher(book.getPublisher());

        if (book.getPublishingYear() != null)
            otherBook.setPublishingYear(book.getPublishingYear());

        if (book.getCategory() != null)
            otherBook.setCategory(book.getCategory());

        // Update
        bookRepository.save(otherBook);
        return book;
    }

    @Override
    public List<BookAuthor> getBookAuthors(String isbn) {
        return bookAuthorRepository.findAllByIsbn(isbn);
    }

    @Override
    public BookAuthor addAuthor(String isbn, Integer idAuthor) {

        // Get book from repository
        Optional<Book> bookOptional = bookRepository.findById(isbn);
        BookAuthor bookAuthorExists = bookAuthorRepository.findByIsbnAndIdAuthor(isbn, idAuthor);

        // If it doesn't exist
        if (bookOptional.isEmpty() || bookAuthorExists != null)
            return null;

        Book book = bookOptional.get();

        // Get the index of the author
        Integer nextListIndex = bookAuthorRepository.getNextIndex(book.getIsbn());

        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setIdAuthor(idAuthor);
        bookAuthor.setIsbn(isbn);
        bookAuthor.setListIndex(nextListIndex);

        return bookAuthorRepository.save(bookAuthor);
    }

    @Override
    public long deleteAuthor(String isbn, Integer idAuthor) {
        long deleted = bookAuthorRepository.deleteByIsbnAndIdAuthor(isbn, idAuthor);

        // Change the indexes of remaining authors
        if (deleted != 0) {
            authorReindex(isbn);
        }

        return deleted;
    }

    @Override
    public long deleteAllAuthors(String isbn) {
        return bookAuthorRepository.deleteAllByIsbn(isbn);
    }

    private void authorReindex(String isbn) {
        List<BookAuthor> bookAuthor = getBookAuthors(isbn);

        // Iterate and change listIndex
        for (int index = 0; index < bookAuthor.size(); index++) {
            bookAuthor.get(index).setListIndex(index + 1);
        }

        bookAuthorRepository.saveAll(bookAuthor);
    }

}

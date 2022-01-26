package pos.book.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pos.book.model.dao.BookAuthorRepository;
import pos.book.model.dao.BookRepository;
import pos.book.model.pojo.dto.BookMinimal;
import pos.book.model.pojo.erd.Book;
import pos.book.model.pojo.erd.BookAuthor;
import pos.book.model.pojo.exception.HttpResponseException;
import pos.book.service.BookService;
import pos.book.utils.PageUtils;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookAuthorRepository bookAuthorRepository;

    public BookServiceImpl(BookRepository bookRepository, BookAuthorRepository bookAuthorRepository) {
        this.bookRepository = bookRepository;
        this.bookAuthorRepository = bookAuthorRepository;
    }

    @Override
    public List<Book> getAllBooks(Integer page, Integer itemsPerPage) {
        Pageable pageable = PageUtils.getPageOf(page, itemsPerPage);
        return bookRepository.findAll(pageable).getContent();
    }

    @Override
    public List<Book> getBooksByCategoryOrYear(String category, Integer publishingYear, Integer page, Integer itemsPerPage) {
        Pageable pageable = PageUtils.getPageOf(page, itemsPerPage);
        if (category != null && publishingYear != null)
            return bookRepository.findByCategoryAndPublishingYear(category, publishingYear, pageable).getContent();
        if (category != null)
            return bookRepository.findByCategory(category, pageable).getContent();
        if (publishingYear != null)
            return bookRepository.findByPublishingYear(publishingYear, pageable).getContent();
        return bookRepository.findAll(pageable).getContent();
    }

    @Override
    public Book getBook(String isbn) {
        Optional<Book> book = bookRepository.findById(isbn);

        if(book.isEmpty())
            throw new HttpResponseException("Book with isbn does not exist.", HttpStatus.NOT_FOUND);

        return book.get();
    }

    @Override
    public Book deleteBook(String isbn) {

        Optional<Book> book = bookRepository.findById(isbn);
        if (book.isPresent()) {
            bookRepository.delete(book.get());
            return book.get();
        }
        throw new HttpResponseException("Book with isbn does not exist.", HttpStatus.NOT_FOUND);
    }

    @Override
    public Book addBook(Book book) {

        Optional<Book> otherBookOptional = bookRepository.findById(book.getIsbn());

        // nu exista carte
        if (otherBookOptional.isPresent())
            throw new HttpResponseException("Book already exists.", HttpStatus.NOT_ACCEPTABLE);

        // se cauta sa nu fie acelasi titlu
        Book otherBook = bookRepository.findByTitle(book.getTitle());

        if (otherBook != null)
            throw new HttpResponseException("A book with this title already exists.", HttpStatus.CONFLICT);

        // se adauga cartea
        bookRepository.save(book);
        return book;
    }

    @Override
    public Book updateBook(Book book) {
        Optional<Book> otherBookOptional = bookRepository.findById(book.getIsbn());

        // Trebuie sa existe cartea
        if (otherBookOptional.isEmpty())
            throw new HttpResponseException("Book with given isbn does not exist.", HttpStatus.NOT_FOUND);

        Book toBeChanged = otherBookOptional.get();

        // Verifica daca titlul e diferit sa nu mai existe un alt titlu la fel
        if (toBeChanged.getTitle() != null && !book.getTitle().equals(toBeChanged.getTitle())) {
            Book sameTitle = bookRepository.findByTitle(book.getTitle());

            // Exista o carte cu acelasi titlu
            if (sameTitle != null)
                throw new HttpResponseException("A book with this title already exists.", HttpStatus.CONFLICT);

            toBeChanged.setTitle(book.getTitle());
        }

        if (book.getPublisher() != null && !book.getPublisher().equals(toBeChanged.getPublisher()))
            toBeChanged.setPublisher(book.getPublisher());

        if (book.getPublishingYear() != null && !book.getPublishingYear().equals(toBeChanged.getPublishingYear()))
            toBeChanged.setPublishingYear(book.getPublishingYear());

        if (book.getCategory() != null && !book.getCategory().equals(toBeChanged.getCategory()))
            toBeChanged.setCategory(book.getCategory());

        if(book.getQuantity() != null) {
            // Check positive quantity
            if(book.getQuantity() < 0)
                throw new HttpResponseException("Quantity must be a positive number.", HttpStatus.NOT_ACCEPTABLE);

            toBeChanged.setQuantity(book.getQuantity());
        }

        bookRepository.save(toBeChanged);
        return toBeChanged;
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
        if (bookOptional.isEmpty())
            throw new HttpResponseException("Book does not exist.", HttpStatus.NOT_FOUND);

        if(bookAuthorExists != null)
            throw new HttpResponseException("Book already has this author.", HttpStatus.CONFLICT);


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
        Optional<Book> book = bookRepository.findById(isbn);

        if(book.isEmpty())
            throw new HttpResponseException("Book does not exist.", HttpStatus.NOT_FOUND);

        long deleted = bookAuthorRepository.deleteByIsbnAndIdAuthor(isbn, idAuthor);

        if(deleted == 0)
            throw new HttpResponseException("Book has no author with given id.", HttpStatus.NOT_FOUND);

        // Change the indexes of remaining authors
        authorReindex(isbn);
        return deleted;
    }

    @Override
    public long deleteAllAuthors(String isbn) {
        return bookAuthorRepository.deleteAllByIsbn(isbn);
    }

    @Override
    public boolean validateBooksAndUpdateQuantity(List<BookMinimal> bookList) {

        // Indicates if there are any books with over stock quantity
        StringBuilder errorString = new StringBuilder();

        for(BookMinimal book : bookList) {
            // Get book from book service
            Optional<Book> bookExists = bookRepository.findById(book.getIsbn());
            if (bookExists.isEmpty())
                throw new HttpResponseException("Book with isbn = " + book.getIsbn() + " does not exist.",
                        HttpStatus.NOT_FOUND);

            // Get the book values
            Book existing = bookExists.get();

            // Check the quantity
            if(book.getQuantity() > existing.getQuantity()){
                // Error, add to stringbuilder
                errorString
                        .append("Not enough books in stock for: ")
                        .append(book.getIsbn())
                        .append("\n");
            }
        }

        // Check if all the items are ok
        if(!errorString.isEmpty())
            throw new HttpResponseException(errorString.toString(), HttpStatus.CONFLICT);

        // All the items are ok, proceed with update
        for(BookMinimal book : bookList) {

            // Get the book
            Optional<Book> toBeUpdatedOptional = bookRepository.findById(book.getIsbn());
            if (toBeUpdatedOptional.isEmpty())
                throw new HttpResponseException("Book with isbn = " + book.getIsbn() + " does not exist.",
                        HttpStatus.NOT_FOUND);

            // Change quantity
            Book toBeUpdated = toBeUpdatedOptional.get();
            toBeUpdated.setQuantity(toBeUpdated.getQuantity() - book.getQuantity());

            // Update
            Book saved = bookRepository.save(toBeUpdated);
        }
        // Return the success of the operation
        return true;
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

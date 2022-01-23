package pos.book.controller;

import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pos.book.pojo.Book;
import pos.book.pojo.BookAuthor;
import pos.book.service.BookService;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;


@Controller
@CrossOrigin(maxAge = 3500, origins = "http://localhost:3000")
@RequestMapping(
        path = "/api/bookcollection/books"
)
public class BookController {

    @Autowired
    private BookService bookService;


    /*
    Get a book from DB.
     */
    @GetMapping(
            path = "{ISBN}",
            produces = "application/json"
    )
    public @ResponseBody
    ResponseEntity<Object> getBook(
            @PathVariable("ISBN") String isbn,
            @RequestParam(required = false, defaultValue = "true") boolean verbose
    ) throws ParseException {
        Book book = bookService.getBook(isbn);
        if (book == null)
            return status(HttpStatus.NOT_FOUND).body(null);

        if (!verbose) {
            String verboseString = String.format(
                    "{" +
                            "\"isbn\": \"%s\"," +
                            "\"title\": \"%s\"," +
                            "\"category\": \"%s\"" +
                            "}",
                    book.getIsbn(),
                    book.getTitle(),
                    book.getCategory()
            );
            return ok().body(verboseString);
        }

        // Adds the Hateos Link to the book
        appendLinksToBook(book);

        return ok().body(book);
    }


    /*

     */
    @RequestMapping(path = "{ISBN}", method = RequestMethod.HEAD)
    public ResponseEntity<Object> bookExists(@PathVariable("ISBN") String isbn) throws ParseException {
        return this.getBook(isbn, true);
    }

    /*
    Add a book to DB
     */
    @PostMapping(path = "")
    public @ResponseBody
    ResponseEntity<Object> addNewBook(@RequestBody Book book) throws ParseException {
        Book createdBook = bookService.addBook(book);

        if (createdBook == null)
            return status(HttpStatus.CONFLICT).body(null);

        // Adds the Hateos Link to the book
        appendLinksToBook(createdBook);

        return status(HttpStatus.CREATED).body(createdBook);
    }

    /*
    Get all books from DB
     */
    @GetMapping(path = "")
    public @ResponseBody
    ResponseEntity<Iterable<Book>> getAllBooks(
            @RequestParam(required = false, defaultValue = "") String genre,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer page,
            @RequestParam(
                    name = "items_per_page",
                    required = false,
                    defaultValue = "5"
            ) Integer itemsPerPage
    ) throws ParseException {

        List<Book> bookList;

        if (page == null) {

            if (genre.isEmpty() && year == null) {
                bookList = bookService.getAllBooks();
            } else{
                if(genre.isEmpty()) genre = null;
                bookList = bookService.getBooksByCategoryOrYear(genre, year);
            }
        } else {

            if (genre.isEmpty() && year == null) {
                bookList = bookService.getAllBooks(page, itemsPerPage);
            } else {
                if(genre.isEmpty()) genre = null;
                bookList = bookService.getBooksByCategoryOrYear(genre, year, page, itemsPerPage);
            }
        }

        if (bookList.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        for(Book book : bookList){
            // Adds the Hateos Link to the book
            appendLinksToBook(book);

        }
        return ok().body(bookList);
    }

    /*
    Update a book from DB
     */
    @PutMapping(path = "/")
    public @ResponseBody
    ResponseEntity<Book> updateBook(@RequestBody Book book) throws ParseException {
        Book updatedBook = bookService.updateBook(book);

        HttpStatus status = HttpStatus.NO_CONTENT;

        if (updatedBook == null)
            status = HttpStatus.CONFLICT;

        else {
            // Adds the Hateos Link to the book
            appendLinksToBook(updatedBook);
        }
        return status(status).body(updatedBook);
    }

    /*
    Change quantity of book
     */
    @PatchMapping(path = "/{ISBN}/change-stock/{QUANTITY}")
    public @ResponseBody ResponseEntity<Book> updateBookSetQuantity(
            @PathVariable("ISBN") String isbn,
            @PathVariable("QUANTITY") Integer quantity
    ) throws ParseException {
        // Get book if exist
        Book toBeChangedBook = bookService.getBook(isbn);

        if(toBeChangedBook == null){
            return status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        toBeChangedBook.setQuantity(quantity);

        Book updatedBook = bookService.updateBook(toBeChangedBook);

        if(updatedBook == null){
            return status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        // HATEOS link
        appendLinksToBook(updatedBook);
        return status(HttpStatus.CREATED).body(updatedBook);
    }


    /*
    Delete a book from DB.
     */
    @DeleteMapping(path = "{ISBN}")
    public @ResponseBody
    ResponseEntity<Book> deleteBook(@PathVariable("ISBN") String isbn) {

        Book deleted = bookService.deleteBook(isbn);
        if (deleted == null) {
            return status(HttpStatus.NOT_FOUND).body(null);
        }


        return ok().body(deleted);
    }

    /*
    Get all authors of a book from DB
    */
    @GetMapping(path = "/{ISBN}/authors")
    public @ResponseBody
    ResponseEntity<Iterable<BookAuthor>> getAllAuthorsOfBook(@PathVariable("ISBN") String isbn) throws ParseException {

        List<BookAuthor> relations = bookService.getBookAuthors(isbn);

        for(BookAuthor bookAuthor : relations) {
            // Adds the Hateos Link to bookAuthor
            appendLinksToBookAuthor(bookAuthor);
        }


        return ok().body(relations);
    }

    /*
    Add new author to book on DB.
    */
    @PostMapping(path = "{ISBN}/authors/{ID}")
    public @ResponseBody
    ResponseEntity<BookAuthor> addAuthorToBook(
            @PathVariable("ISBN") String isbn,
            @PathVariable("ID") Integer idAuthor
    ) throws ParseException {
        BookAuthor bookAuthor = bookService.addAuthor(isbn, idAuthor);
        HttpStatus status = HttpStatus.CREATED;

        if (bookAuthor == null)
            status = HttpStatus.NOT_ACCEPTABLE;

        else {
            appendLinksToBookAuthor(bookAuthor);
        }

        return status(status).body(bookAuthor);
    }

    /*
    Delete author for book DB.
    */
    @DeleteMapping(path = "{ISBN}/authors/{ID}")
    public @ResponseBody
    ResponseEntity<String> deleteAuthorBook(
            @PathVariable("ISBN") String isbn,
            @PathVariable("ID") Integer idAuthor
    ) {
        long deleted = bookService.deleteAuthor(isbn, idAuthor);
        HttpStatus status = HttpStatus.NO_CONTENT;

        String response = "Deleted successfully!";
        if (deleted == 0) {
            status = HttpStatus.NOT_FOUND;
            response = "Object could not be deleted!";
        }

        return status(status).body(response);
    }

    /*
   Delete author for book DB.
   */
    @DeleteMapping(path = "{ISBN}/authors/")
    public @ResponseBody
    ResponseEntity<String> deleteAllAuthors(
            @PathVariable("ISBN") String isbn
    ) {
        long deleted = bookService.deleteAllAuthors(isbn);
        HttpStatus status = HttpStatus.NO_CONTENT;

        String response = deleted + " authors deleted successfully!";
        if (deleted == 0) {
            status = HttpStatus.NOT_FOUND;
            response = "Object could not be deleted!";
        }

        return status(status).body(response);
    }

    private void appendLinksToBook(Book book) throws ParseException {
        Link selfLink = linkTo(methodOn(BookController.class)
                .getBook(book.getIsbn(),true))
                .withSelfRel();
        book.add(selfLink);
    }

    private void appendLinksToBookAuthor(BookAuthor bookAuthor) throws ParseException {
        // Adds the Hateos Link to bookAuthor
        Link selfLink = linkTo(methodOn(BookController.class)
                .getAllAuthorsOfBook(bookAuthor.getIsbn())).withSelfRel();
        bookAuthor.add(selfLink);

        // Adds the Hateos Link to the book
        Link bookRelLink = linkTo(methodOn(BookController.class)
                .getBook(bookAuthor.getIsbn(), true)).withRel("books");
        bookAuthor.add(bookRelLink);

        // Adds the Hateos Link to the author
        Link authorRelLink = linkTo(methodOn(AuthorController.class)
                .getAuthor(bookAuthor.getIdAuthor())).withRel("authors");
        bookAuthor.add(authorRelLink);
    }
}

package pos.book.controller;

import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pos.book.model.pojo.dto.BookNoVerbose;
import pos.book.model.pojo.erd.Author;
import pos.book.model.pojo.erd.Book;
import pos.book.model.pojo.erd.BookAuthor;
import pos.book.model.pojo.exception.HttpResponseException;
import pos.book.service.AuthorService;
import pos.book.service.BookService;
import pos.book.utils.HateosUtils;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private AuthorService authorService;


    /**
     * Method responsible for getting books from database
     *
     * @param isbn    The id of the requested book
     * @param verbose Specifies if the format should be verboise or not
     * @return A response entity with the requested book or an error
     */
    @GetMapping(
            path = "{ISBN}",
            produces = "application/json"
    )
    public @ResponseBody
    ResponseEntity<?> getBook(
            @PathVariable("ISBN") String isbn,
            @RequestParam(required = false, defaultValue = "true") boolean verbose
    ) {
        try {
            Book book = bookService.getBook(isbn);

            // Return no verbose form
            if (!verbose) {
                BookNoVerbose noVerb = new BookNoVerbose(book);
                HateosUtils.appendLinksToBookNotVerboise(noVerb);
                return ok().body(noVerb);
            }
            // Adds the Hateos Link to the book
            HateosUtils.appendLinksToBook(book);

            return ok().body(book);
        } catch (HttpResponseException e) {
            return status(e.getStatus()).body(e.getMessage());
        }
    }


    /**
     * Method responsible for checking if a book exists
     *
     * @param isbn The id of the book
     * @return An empty body with 200 OK if book exists or 404 NOT FOUND if it doesn't exist
     */
    @RequestMapping(path = "{ISBN}", method = RequestMethod.HEAD)
    public ResponseEntity<?> bookExists(@PathVariable("ISBN") String isbn) {
        return this.getBook(isbn, true);
    }

    /**
     * Method for adding a new book in the database
     *
     * @param book The body of the added book
     * @return The createg book or an error if it is not possible
     */
    @PostMapping(path = "")
    public @ResponseBody
    ResponseEntity<?> addNewBook(@RequestBody Book book) {
        try {
            Book createdBook = bookService.addBook(book);

            // Adds the Hateos Link to the book
            HateosUtils.appendLinksToBook(createdBook);

            return status(HttpStatus.CREATED).body(createdBook);
        } catch (HttpResponseException e) {
            return status(e.getStatus()).body(e.getMessage());
        }
    }

    /**
     * Method responsible for getting all books from database
     *
     * @param genre        The genre of the book
     * @param year         The year of the book
     * @param page         The number of the requested page
     * @param itemsPerPage Items per page
     * @return A list of requested books or an error message
     */
    @GetMapping(path = "")
    public @ResponseBody
    ResponseEntity<?> getAllBooks(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer page,
            @RequestParam(
                    name = "items_per_page",
                    required = false,
                    defaultValue = "5"
            ) Integer itemsPerPage
    ) {

        try {
            List<Book> bookList = bookService.getBooksByCategoryOrYear(genre, year, page, itemsPerPage);

            for (Book book : bookList) {
                // Adds the Hateos Link to the book
                HateosUtils.appendLinksToBook(book);
            }
            return ok().body(bookList);
        } catch (HttpResponseException e) {
            return status(e.getStatus()).body(e.getMessage());
        }
    }

    /**
     * Method responsible for updating a book
     *
     * @param book The book to be updated
     * @return The changed book or an error message
     */
    @PutMapping(path = "/")
    public @ResponseBody
    ResponseEntity<?> updateBook(@RequestBody Book book) {
        try {
            Book updatedBook = bookService.updateBook(book);

            // Adds the Hateos Link to the book
            HateosUtils.appendLinksToBook(updatedBook);

            return ok().body(updatedBook);
        } catch (HttpResponseException e) {
            return status(e.getStatus()).body(e.getMessage());
        }
    }

    /**
     * Method responsible for changing the quantity of a book
     *
     * @param isbn     The id of tthe book
     * @param quantity The new quantity of the book
     * @return The changed book or an error message
     */
    @PatchMapping(path = "/{ISBN}/change-stock/{QUANTITY}")
    public @ResponseBody
    ResponseEntity<?> updateBookSetQuantity(
            @PathVariable("ISBN") String isbn,
            @PathVariable("QUANTITY") Integer quantity
    ) {
        try {
            // Get book if exist
            Book toBeChangedBook = bookService.getBook(isbn);

            toBeChangedBook.setQuantity(quantity);

            Book updatedBook = bookService.updateBook(toBeChangedBook);

            // HATEOS link
            HateosUtils.appendLinksToBook(updatedBook);

            return status(HttpStatus.CREATED).body(updatedBook);
        } catch (HttpResponseException e) {
            return status(e.getStatus()).body(e.getMessage());
        }
    }

    /**
     * Method responsible for deleting a book from database
     *
     * @param isbn The id of the book
     * @return The deleted book or an error message
     */
    @DeleteMapping(path = "{ISBN}")
    public @ResponseBody
    ResponseEntity<?> deleteBook(@PathVariable("ISBN") String isbn) {
        try {
            Book deleted = bookService.deleteBook(isbn);
            HateosUtils.appendLinksToBook(deleted);
            return ok().body(deleted);
        } catch (HttpResponseException e) {
            return status(e.getStatus()).body(e.getMessage());
        }
    }

    /**
     * Method responsible for getting all the authors of a book
     *
     * @param isbn The id of the book
     * @return A list with the authors of a book
     */
    @GetMapping(path = "/{ISBN}/authors")
    public @ResponseBody
    ResponseEntity<?> getAllAuthorsOfBook(@PathVariable("ISBN") String isbn) {

        try {
            List<BookAuthor> relations = bookService.getBookAuthors(isbn);
            List<Author> authors = relations.stream()
                    .map(bookAuthor -> {
                        Author fromDb = authorService.getAuthor(bookAuthor.getIdAuthor());

                        // Adds the Hateos Link to Author
                        HateosUtils.appendLinksToAuthor(fromDb);

                        // Returns the object
                        return fromDb;
                    })
                    .collect(Collectors.toList());

            return ok().body(authors);
        } catch (HttpResponseException e) {
            return status(e.getStatus()).body(e.getMessage());
        }
    }

    /**
     * Method responsible for adding a new author to book
     * @param isbn The id of the book
     * @param idAuthor The id of the author
     * @return A book with all it's authors or an error message
     */
    @PostMapping(path = "{ISBN}/authors/{ID}")
    public @ResponseBody
    ResponseEntity<?> addAuthorToBook(
            @PathVariable("ISBN") String isbn,
            @PathVariable("ID") Integer idAuthor
    ){
        try {
            bookService.addAuthor(isbn, idAuthor);
            List<Author> authors = bookService.getBookAuthors(isbn).stream()
                    .map(it -> {
                        Author fromDb = authorService.getAuthor(it.getIdAuthor());

                        // Adds the Hateos Link to Author
                        HateosUtils.appendLinksToAuthor(fromDb);

                        // Returns the object
                        return fromDb;
                    })
                    .collect(Collectors.toList());


            return ok().body(authors);
        } catch (HttpResponseException e) {
            return status(e.getStatus()).body(e.getMessage());
        }
    }

    /**
     * Method responsible for deleting an author of a book
     * @param isbn The id of the book
     * @param idAuthor The id of the author
     * @return The list of the remaining authors of the book or an error message
     */
    @DeleteMapping(path = "{ISBN}/authors/{ID}")
    public @ResponseBody
    ResponseEntity<?> deleteAuthorBook(
            @PathVariable("ISBN") String isbn,
            @PathVariable("ID") Integer idAuthor
    ) {
        try {
            bookService.deleteAuthor(isbn, idAuthor);

            List<Author> authors = bookService.getBookAuthors(isbn).stream()
                    .map(it -> {
                        Author fromDb = authorService.getAuthor(it.getIdAuthor());

                        // Adds the Hateos Link to Author
                        HateosUtils.appendLinksToAuthor(fromDb);

                        // Returns the object
                        return fromDb;
                    })
                    .collect(Collectors.toList());
            return ok(authors);
        } catch (HttpResponseException e) {
            return status(e.getStatus()).body(e.getMessage());
        }
    }

    /**
     * Method responsible for deleting all authors of a book
     * @param isbn The id of the book
     * @return The book deleted
     */
    @DeleteMapping(path = "{ISBN}/authors/")
    public @ResponseBody
    ResponseEntity<?> deleteAllAuthors(
            @PathVariable("ISBN") String isbn
    ) {
        try {
            long deleted = bookService.deleteAllAuthors(isbn);
            Book book = bookService.getBook(isbn);
            return ok().body(book);
        } catch (HttpResponseException e) {
            return status(e.getStatus()).body(e.getMessage());
        }

    }
}

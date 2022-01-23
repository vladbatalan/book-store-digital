package pos.book.utils;

import org.springframework.hateoas.Link;
import pos.book.controller.AuthorController;
import pos.book.controller.BookController;
import pos.book.model.pojo.dto.BookNoVerbose;
import pos.book.model.pojo.erd.Author;
import pos.book.model.pojo.erd.Book;
import pos.book.model.pojo.erd.BookAuthor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class HateosUtils {

    public static void appendLinksToBook(Book book) {
        Link selfLink = linkTo(methodOn(BookController.class)
                .getBook(book.getIsbn(), true))
                .withSelfRel();
        book.add(selfLink);
    }

    public static void appendLinksToBookNotVerboise(BookNoVerbose book) {
        Link selfLink = linkTo(methodOn(BookController.class)
                .getBook(book.getIsbn(), false))
                .withSelfRel();
        book.add(selfLink);
    }

    public static void appendLinksToBookAuthor(BookAuthor bookAuthor) {
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

    public static void appendLinksToAuthor(Author author){
        Link selfLink = linkTo(methodOn(AuthorController.class)
                .getAuthor(author.getIdAuthor())).withSelfRel();
        author.add(selfLink);
    }
}

package pos.book.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pos.book.model.pojo.erd.Author;
import pos.book.service.AuthorService;

import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;


@Controller
@RequestMapping(path = "/api/bookcollection/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    /*
    Get an author from DB.
    */
    @GetMapping(path = "{ID}")
    public @ResponseBody
    ResponseEntity<Author> getAuthor(@PathVariable("ID") Integer id) {
        Author author = authorService.getAuthor(id);
        if (author == null)
            return status(HttpStatus.NOT_FOUND).body(null);

        // Adds the Hateos Link to Author
        Link selfLink = linkTo(methodOn(AuthorController.class)
                .getAuthor(author.getIdAuthor())).withSelfRel();
        author.add(selfLink);

        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    /*
    Add an author to DB
     */
    @PostMapping(path = "")
    public @ResponseBody
    ResponseEntity<Object> addNewAuthor(@RequestBody Author author) {
        Author createdAuthor = authorService.addAuthor(author);

        if (createdAuthor != null) {
            // Adds the Hateos Link to Author
            Link selfLink = linkTo(methodOn(AuthorController.class)
                    .getAuthor(createdAuthor.getIdAuthor())).withSelfRel();
            createdAuthor.add(selfLink);
        }

        return createdAuthor == null ?
                status(HttpStatus.CONFLICT).body(null) :
                status(HttpStatus.CREATED).body(createdAuthor);

    }

    /*
    Get all authors from DB
     */
    @GetMapping(path = "")
    public @ResponseBody
    ResponseEntity<Iterable<Author>> getAllAuthors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false, defaultValue = "") String match
    ) {

        // This returns a JSON or XML with the users
        List<Author> authorList;

        if (name == null) authorList = authorService.getAllAuthors();
        else if (Objects.equals(match, "exact")) authorList = authorService.getAuthorByName(name);
        else authorList = authorService.getAuthorByNameLike(name);

        for (Author author : authorList) {

            // Adds the Hateos Link to Author
            Link selfLink = linkTo(methodOn(AuthorController.class)
                    .getAuthor(author.getIdAuthor())).withSelfRel();
            author.add(selfLink);
        }

        return authorList.isEmpty() ?
                status(HttpStatus.NOT_FOUND).body(null) :
                ResponseEntity.ok().body(authorList);
    }

    /*
    Update an author from DB
     */
    @PutMapping(path = "/")
    public @ResponseBody
    ResponseEntity<Author> updateAuthors(
            @RequestBody Author author
    ) {
        Author updatedAuthor = authorService.updateAuthor(author);

        HttpStatus status = updatedAuthor == null ?
                HttpStatus.CONFLICT :
                HttpStatus.NO_CONTENT;

        // Adds the Hateos Link to Author
        if(updatedAuthor != null) {
            Link selfLink = linkTo(methodOn(AuthorController.class)
                    .getAuthor(updatedAuthor.getIdAuthor())).withSelfRel();
            updatedAuthor.add(selfLink);
        }

        return status(status).body(updatedAuthor);
    }

    /*
      Delete an author from DB.
    */
    @DeleteMapping(path = "{ID}")
    public @ResponseBody
    ResponseEntity<Author> deleteBook(@PathVariable("ID") Integer id) {

        Author deleteBook = authorService.deleteAuthor(id);
        return deleteBook == null ?
                status(HttpStatus.NOT_FOUND).body(null) :
                ok().body(deleteBook);

    }


}

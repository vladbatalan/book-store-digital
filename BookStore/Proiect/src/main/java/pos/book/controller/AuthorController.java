package pos.book.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pos.book.model.pojo.erd.Author;
import pos.book.model.pojo.exception.HttpResponseException;
import pos.book.service.AuthorService;
import pos.book.utils.HateosUtils;

import java.util.List;
import java.util.Objects;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;


@Controller
@RequestMapping(path = "/api/bookcollection/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    /**
     * Method responsible for getting an author from db
     *
     * @param id the id of the author
     * @return An author
     */
    @GetMapping(path = "{ID}")
    public @ResponseBody
    ResponseEntity<?> getAuthor(@PathVariable("ID") Integer id) {
        try {
            Author author = authorService.getAuthor(id);

            // Adds the Hateos Link to Author
            HateosUtils.appendLinksToAuthor(author);

            return ok(author);

        } catch (HttpResponseException e) {
            return status(e.getStatus()).body(e.getMessage());
        }
    }

    /*
    Add an author to DB
     */
    @PostMapping(path = "")
    public @ResponseBody
    ResponseEntity<Object> addNewAuthor(@RequestBody Author author) {
        try {
            Author createdAuthor = authorService.addAuthor(author);

            if (createdAuthor != null) {
                // Adds the Hateos Link to Author
                HateosUtils.appendLinksToAuthor(createdAuthor);
            }

            return createdAuthor == null ?
                    status(HttpStatus.CONFLICT).body(null) :
                    status(HttpStatus.CREATED).body(createdAuthor);
        } catch (HttpResponseException e) {
            return status(e.getStatus()).body(e.getMessage());
        }
    }

    /*
    Get all authors from DB
     */
    @GetMapping(path = "")
    public @ResponseBody
    ResponseEntity<?> getAllAuthors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false, defaultValue = "") String match
    ) {
        try {
            // This returns a JSON or XML with the users
            List<Author> authorList;

            if (name == null) authorList = authorService.getAllAuthors();
            else if (Objects.equals(match, "exact")) authorList = authorService.getAuthorByName(name);
            else authorList = authorService.getAuthorByNameLike(name);

            for (Author author : authorList) {

                // Adds the Hateos Link to Author
                HateosUtils.appendLinksToAuthor(author);
            }

            return ok().body(authorList);
        } catch (HttpResponseException e) {
            return status(e.getStatus()).body(e.getMessage());
        }
    }

    /*
    Update an author from DB
     */
    @PutMapping(path = "/")
    public @ResponseBody
    ResponseEntity<?> updateAuthors(
            @RequestBody Author author
    ) {
        try {
            Author updatedAuthor = authorService.updateAuthor(author);

            HateosUtils.appendLinksToAuthor(updatedAuthor);

            return ok().body(updatedAuthor);

        } catch (HttpResponseException e) {
            return status(e.getStatus()).body(e.getMessage());
        }
    }

    /**
     * Method responsible for deleting an author
     *
     * @param id the id of the author
     * @return The deleted author
     */
    @DeleteMapping(path = "{ID}")
    public @ResponseBody
    ResponseEntity<?> deleteBook(@PathVariable("ID") Integer id) {
        try {
            Author deleteBook = authorService.deleteAuthor(id);
            return ok().body(deleteBook);
        } catch (HttpResponseException e) {
            return status(e.getStatus()).body(e.getMessage());
        }
    }


}

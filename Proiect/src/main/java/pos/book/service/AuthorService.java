package pos.book.service;


import pos.book.pojo.Author;
import pos.book.pojo.Book;

import java.util.List;

public interface AuthorService {
    List<Author> getAllAuthors();
    Author getAuthor(Integer idAuthor);
    Author deleteAuthor(Integer idAuthor);
    Author addAuthor(Author author);
    Author updateAuthor(Author author);

    List<Author> getAuthorByNameLike(String name);
    List<Author> getAuthorByName(String name);
}

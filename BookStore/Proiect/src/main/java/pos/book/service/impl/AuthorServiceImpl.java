package pos.book.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pos.book.model.dao.AuthorRepository;
import pos.book.model.dao.BookAuthorRepository;
import pos.book.model.pojo.erd.Author;
import pos.book.model.pojo.exception.HttpResponseException;
import pos.book.service.AuthorService;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BookAuthorRepository bookAuthorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository, BookAuthorRepository bookAuthorRepository) {
        this.authorRepository = authorRepository;
        this.bookAuthorRepository = bookAuthorRepository;
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Author getAuthor(Integer idAuthor) {
        Optional<Author> author = authorRepository.findById(idAuthor);

        if(author.isEmpty())
            throw new HttpResponseException("Author does not exist.", HttpStatus.NOT_FOUND);

        return author.get();
    }

    @Override
    public Author deleteAuthor(Integer idAuthor) {

        Optional<Author> author = authorRepository.findById(idAuthor);
        if (author.isEmpty())
            throw new HttpResponseException("Author does not exist.", HttpStatus.NOT_FOUND);

        // Go to all the books of the author and delete this authorus sa pap slanina cuc
        bookAuthorRepository.deleteAllByIdAuthor(idAuthor);

        authorRepository.delete(author.get());
        return author.get();
    }

    @Override
    public Author addAuthor(Author author) {

        // Id is not important
        author.setIdAuthor(null);

        // se cauta sa nu fie acelasi nume si prenume
        Author otherAuthor = authorRepository.findAuthorByFirstNameAndLastName(
                author.getFirstName(), author.getLastName());

        if (otherAuthor != null)
            throw new HttpResponseException("An author with same name exists.", HttpStatus.CONFLICT);

        // Se adauga autor
        return authorRepository.save(author);
    }

    @Override
    public Author updateAuthor(Author author) {
        Optional<Author> otherAuthorOptional = authorRepository.findById(author.getIdAuthor());

        // Trebuie sa existe cartea
        if (otherAuthorOptional.isEmpty())
            throw new HttpResponseException("Author does not exist.", HttpStatus.NOT_FOUND);

        Author otherAuthor = otherAuthorOptional.get();

        boolean changedName = false;

        // Schimbam elementele care sunt diferite
        if (author.getLastName() != null && !author.getLastName().equals(otherAuthor.getLastName())) {
            otherAuthor.setLastName(author.getLastName());
            changedName = true;
        }

        if (author.getFirstName() != null && !author.getFirstName().equals(otherAuthor.getFirstName())) {
            otherAuthor.setFirstName(author.getFirstName());
            changedName = true;
        }

        // Search for other author with the same name
        if(changedName) {
            Author sameNameOther = authorRepository.findAuthorByFirstNameAndLastName(
                    otherAuthor.getFirstName(), otherAuthor.getLastName());

            if (sameNameOther != null)
                throw new HttpResponseException("An author with this name already exists.", HttpStatus.CONFLICT);
        }

        // Update
        return authorRepository.save(otherAuthor);
    }

    @Override
    public List<Author> getAuthorByNameLike(String name) {
        return authorRepository.findByNameLike(name);
    }

    @Override
    public List<Author> getAuthorByName(String name) {
        return authorRepository.findAuthorByName(name);
    }
}

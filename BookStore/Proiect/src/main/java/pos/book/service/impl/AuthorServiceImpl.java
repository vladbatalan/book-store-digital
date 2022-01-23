package pos.book.service.impl;

import org.springframework.stereotype.Service;
import pos.book.model.dao.AuthorRepository;
import pos.book.model.pojo.erd.Author;
import pos.book.service.AuthorService;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Author getAuthor(Integer idAuthor) {
        Optional<Author> author = authorRepository.findById(idAuthor);
        return author.orElse(null);
    }

    @Override
    public Author deleteAuthor(Integer idAuthor) {

        Optional<Author> author = authorRepository.findById(idAuthor);
        if (author.isPresent()) {

            authorRepository.delete(author.get());
            return author.get();
        }
        return null;
    }

    @Override
    public Author addAuthor(Author author) {
        if(author.getIdAuthor() != null) {
            Optional<Author> otherAuthorOptional = authorRepository.findById(author.getIdAuthor());

            // nu exista carte
            if (otherAuthorOptional.isPresent()) {
                return null;
            }
        }

        // se cauta sa nu fie acelasi nume si prenume
        Author otherAuthor = authorRepository.findAuthorByFirstNameAndLastName(
                author.getFirstName(), author.getLastName());

        if (otherAuthor != null) {
            return null;
        }

        // se adauga cartea
        authorRepository.save(author);

        return author;
    }

    @Override
    public Author updateAuthor(Author author) {
        Optional<Author> otherAuthorOptional = authorRepository.findById(author.getIdAuthor());

        // Trebuie sa existe cartea
        if (otherAuthorOptional.isEmpty()) {
            return null;
        }

        Author otherAuthor = otherAuthorOptional.get();


        // Schimbam elementele care sunt diferite
        if (author.getLastName() != null)
            otherAuthor.setLastName(author.getLastName());

        if (author.getFirstName() != null)
            otherAuthor.setFirstName(author.getFirstName());

        // Search for other author with the same name
        Author sameNameOther = authorRepository.findAuthorByFirstNameAndLastName(
                otherAuthor.getFirstName(), otherAuthor.getLastName());

        if (sameNameOther == null) {
            // Update
            authorRepository.save(otherAuthor);
            return author;
        }

        return null;
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

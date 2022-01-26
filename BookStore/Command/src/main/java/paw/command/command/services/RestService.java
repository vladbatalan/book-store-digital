package paw.command.command.services;

import paw.command.command.model.pojo.dto.Book;
import paw.command.command.model.pojo.dto.BookMinimal;

import java.util.List;

public interface RestService {
    BookMinimal bookMinimalExists(String isbn);
    Book bookExists(String isbn);
    List<?> validateItemsOfCommand(List<BookMinimal> list);
}

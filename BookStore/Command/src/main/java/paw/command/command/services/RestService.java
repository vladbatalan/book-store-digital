package paw.command.command.services;

import paw.command.command.model.pojo.dto.Book;
import paw.command.command.model.pojo.dto.BookMinimal;

public interface RestService {
    BookMinimal bookExists(String isbn);
}

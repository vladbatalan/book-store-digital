package paw.command.command.services;

import paw.command.command.model.pojo.dto.Book;
import paw.command.command.model.pojo.dto.BookMinimal;
import paw.command.command.model.pojo.dto.OrderRequest;

import java.util.List;

public interface RestService {
    BookMinimal bookMinimalExists(String isbn);
    Book bookExists(String isbn);
    OrderRequest validateItemsOfCommand(OrderRequest list);
}

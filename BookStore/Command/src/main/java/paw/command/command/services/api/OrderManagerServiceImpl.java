package paw.command.command.services.api;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import paw.command.command.model.exception.HttpResponseException;
import paw.command.command.model.pojo.dto.Book;
import paw.command.command.model.pojo.dto.BookMinimal;
import paw.command.command.model.pojo.dto.OrderRequest;
import paw.command.command.model.pojo.erd.Order;
import paw.command.command.services.OrderManagerService;
import paw.command.command.services.OrderService;
import paw.command.command.services.RestService;

import java.util.Date;

@Service
public class OrderManagerServiceImpl implements OrderManagerService {

    private final OrderService orderService;
    private final RestService restService;

    public OrderManagerServiceImpl(OrderService orderService, RestService restService) {
        this.orderService = orderService;
        this.restService = restService;
    }

    @Override
    public Order addOrderToClient(String clientId, OrderRequest orderRequest) {

        //TODO: Reduce duplicated items

        //TODO: Validate the books
        for(BookMinimal bookRequest : orderRequest.getItems()){

            // Get book from book service
            BookMinimal bookExists = restService.bookExists(bookRequest.getIsbn());
            if(bookExists == null)
                throw new HttpResponseException("The books from the order are invalid.", HttpStatus.NOT_ACCEPTABLE);

            // Validate quantity
            if(bookExists.getQuantity() < bookRequest.getQuantity())
                throw new HttpResponseException("There are not enough books in the store.", HttpStatus.CONFLICT);
        }

        // Create the order
        Order order = new Order();

        // Set the date to current time
        order.setDate(new Date(System.currentTimeMillis()));

        // Order set Items
        order.setItems(orderRequest.getItems());

        // Order set status
        order.setStatus("placed");

        // Add the date to database
        Order result = orderService.addOrderToClient(order, clientId);

        if(result == null )
            throw new HttpResponseException("The new order could not be saved", HttpStatus.CONFLICT);

        return result;
    }
}

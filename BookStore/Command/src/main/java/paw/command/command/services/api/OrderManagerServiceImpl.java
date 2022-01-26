package paw.command.command.services.api;

import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import paw.command.command.model.exception.HttpResponseException;
import paw.command.command.model.pojo.dto.BookMinimal;
import paw.command.command.model.pojo.dto.OrderRequest;
import paw.command.command.model.pojo.erd.Order;
import paw.command.command.services.OrderManagerService;
import paw.command.command.services.OrderService;
import paw.command.command.services.RestService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        Map<String, Integer> orderItems = new HashMap<>();

        // Populate the map
        for(BookMinimal bMin : orderRequest.getItems()){
            // If not in the map
            if(!orderItems.containsKey(bMin.getIsbn())){
                orderItems.put(bMin.getIsbn(), bMin.getQuantity());
            }
            // Else update quantity
            else{
                orderItems.replace(bMin.getIsbn(), orderItems.get(bMin.getIsbn()),
                        orderItems.get(bMin.getIsbn()) + bMin.getQuantity());
            }
        }

        // Reconstruct the items from order request
        orderRequest.getItems().clear();
        for(String key : orderItems.keySet()){
            orderRequest.getItems().add(new BookMinimal(key, orderItems.get(key)));
        }

        for(BookMinimal bookRequest : orderRequest.getItems()){

            // Get book from book service
            BookMinimal bookExists = restService.bookMinimalExists(bookRequest.getIsbn());
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
        order.setStatus("initializata");

        // Add the date to database
        Order result = orderService.addOrderToClient(order, clientId);

        if(result == null )
            throw new HttpResponseException("The new order could not be saved", HttpStatus.CONFLICT);

        return result;
    }

    @Override
    public Order activeOrderToClient(String clientId, String orderId){

        // Throws all exceptions if it does not work
        Order order = orderService.getOrderOfClientById(clientId, orderId);

        // Check if the order is in "initializata" state
        if(!order.getStatus().equals("initializata"))
            throw new HttpResponseException("The order must have \"initializata\" status.", HttpStatus.CONFLICT);

        // Validate the items
        restService.validateItemsOfCommand(order.getItems());

        // If the update was successful, change the state of the command
        order.setStatus("activa");
        Order updated = orderService.updateOrder(order, clientId);

        if(updated == null)
            throw new HttpResponseException("Big problem: The items in the bookcolection were " +
                    "validated for the command, but the order could not be changed into \"activa\".",
                    HttpStatus.INTERNAL_SERVER_ERROR);

        return updated;
    }
}

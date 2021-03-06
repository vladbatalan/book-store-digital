package paw.command.command.services.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import paw.command.command.model.dao.ClientRepository;
import paw.command.command.model.dao.HelperRepository;
import paw.command.command.model.exception.HttpResponseException;
import paw.command.command.model.pojo.dto.BookMinimal;
import paw.command.command.model.pojo.erd.Helper;
import paw.command.command.model.pojo.erd.Order;
import paw.command.command.services.OrderService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final ClientRepository clientRepository;
    private final HelperRepository helperRepository;
    private final String[] ALLOWED_ORDER_STATUSES = new String[]{
            "initializata",
            "activa",
            "finalizata"
    };


    public OrderServiceImpl(ClientRepository orderRepository, HelperRepository helperRepository) {
        this.clientRepository = orderRepository;
        this.helperRepository = helperRepository;
    }


    @Override
    public List<Order> getAllOrders() {

        List<Helper> allClients = helperRepository.findAll();
        List<Order> allOrders = new ArrayList<>();

        for(Helper helper : allClients){
            clientRepository.setCollectionName(helper.getClientId());
            List<Order> clientOrders = clientRepository.findAll();
            allOrders.addAll(clientOrders);
        }

        return allOrders;
    }

    @Override
    public List<Order> getAllOrdersOfClient(String clientId) {
        clientRepository.setCollectionName(clientId);
        return clientRepository.findAll();
    }

    @Override
    public Order getOrderById(String orderId) {
        // Get helper all inserts
        List<Helper> helper = helperRepository.findAll();

        // Search for client that has order
        for(Helper clientHelper : helper){
            if(clientHelper.getOrderList().contains(orderId))
            {
                // Found the order
                // Get the order
                clientRepository.setCollectionName(clientHelper.getClientId());
                Optional<Order> found = clientRepository.findById(orderId);

                if(found.isEmpty())
                    throw new HttpResponseException("Item found in helper but not in client collection.",
                            HttpStatus.INTERNAL_SERVER_ERROR);

                return found.get();
            }
        }

        throw new HttpResponseException("Order does not exist.", HttpStatus.NOT_FOUND);
    }

    @Override
    public Order getOrderOfClientById(String clientId, String orderId) {
        clientRepository.setCollectionName(clientId);
        // Search the order
        Optional<Order> found = clientRepository.findById(orderId);

        if(found.isEmpty())
            throw new HttpResponseException("Order does not exist.", HttpStatus.NOT_FOUND);

        return found.get();
    }

    @Override
    public Order addOrderToClient(Order order, String clientId) {
        // Get to helper
        Optional<Helper> clientHelper = helperRepository.findById(clientId);

        // We assume that the order was previously validated by OrderManagerService
        clientRepository.setCollectionName(clientId);
        Order saved = clientRepository.save(order);

        // If does not exist, client does not exist, create client
        // TODO: Verify existence of client to user service
        if(clientHelper.isEmpty()){
            // Create new helper
            Helper newClient = new Helper();
            newClient.setClientId(clientId);
            newClient.setOrderList(new ArrayList<>());

            // Add the new command to it
            newClient.getOrderList().add(saved.getOrderId());

            // Save the helper in mongo
            helperRepository.save(newClient);
        }

        // The client exist, add the order to it's orderlist
        else{
            Helper clientList = clientHelper.get();

            // The id is unique so there are no duplicate orders
            clientList.getOrderList().add(saved.getOrderId());

            // Update the client list
            helperRepository.save(clientList);
        }

        return saved;
    }

    @Override
    public Order deleteOrderFromClient(String orderId, String clientId) {

        // Check if client exists in helper
        Optional<Helper> clientHelper = helperRepository.findById(clientId);

        if(clientHelper.isEmpty())
            throw new HttpResponseException("Client does not exist.", HttpStatus.NOT_FOUND);

        Helper clientOrderList = clientHelper.get();

        // Check if order exists in this list
        if(!clientOrderList.getOrderList().contains(orderId))
            throw new HttpResponseException("Helper: Order does not exist.", HttpStatus.NOT_FOUND);
        
        // Get the order
        clientRepository.setCollectionName(clientId);
        Optional<Order> order = clientRepository.findByOrderId(orderId);

        if(order.isEmpty())
            throw new HttpResponseException("Order does not exist.", HttpStatus.NOT_FOUND);

        clientRepository.delete(order.get());
        
        // Delete from helper
        clientOrderList.getOrderList().remove(orderId);
        // Update helper
        helperRepository.save(clientOrderList);

        // Return the order
        return order.get();
    }



    @Override
    public Order updateOrder(Order order, String clientId) {

        Order toBeUpdated;
        if(clientId == null)
            // Get the order by the id
            toBeUpdated = getOrderById(order.getOrderId());
        else
            toBeUpdated = getOrderOfClientById(clientId, order.getOrderId());

        if (toBeUpdated == null)
            throw new HttpResponseException("Order does not exist.", HttpStatus.NOT_FOUND);

        // Check the status
        if(!order.getStatus().equals(toBeUpdated.getStatus()))
        {
            if(order.getStatus() == null)
                order.setStatus(toBeUpdated.getStatus());
            // Check to be in the permited words
            if(!Arrays.asList(ALLOWED_ORDER_STATUSES).contains(order.getStatus()))
                throw new HttpResponseException("Invalid status of order.", HttpStatus.NOT_ACCEPTABLE);
        }

        //TODO: Check the list item

        clientRepository.save(order);
        return order;
    }

    @Override
    public Order updateOrderAddItem(String orderId, BookMinimal bookMinimal) {
        Optional<Order> toBeUpdated = clientRepository.findById(orderId);
        if (toBeUpdated.isEmpty())
            throw new HttpResponseException("Order does not exist.", HttpStatus.NOT_FOUND);

        Order order = toBeUpdated.get();
        if (!order.getItems().contains(bookMinimal))
            order.getItems().add(bookMinimal);

        clientRepository.save(order);
        return order;
    }

    @Override
    public Order updateOrderRemoveItem(String orderId, String isbn) {
        Optional<Order> toBeUpdated = clientRepository.findById(orderId);
        if (toBeUpdated.isEmpty())
            throw new HttpResponseException("Order does not exist.", HttpStatus.NOT_FOUND);

        Order order = toBeUpdated.get();

        BookMinimal toBeRemoved = null;
        for (BookMinimal bookMinimal : order.getItems()) {
            if (Objects.equals(bookMinimal.getIsbn(), isbn)) {
                toBeRemoved = bookMinimal;
            }
        }

        if (toBeRemoved == null)
            throw new HttpResponseException("Item could not be found in order.", HttpStatus.NOT_FOUND);

        order.getItems().remove(toBeRemoved);
        clientRepository.save(order);
        return order;
    }


}

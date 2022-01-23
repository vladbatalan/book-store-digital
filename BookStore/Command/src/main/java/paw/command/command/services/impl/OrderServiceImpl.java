package paw.command.command.services.impl;

import org.springframework.stereotype.Service;
import paw.command.command.dao.ClientRepository;
import paw.command.command.pojo.raw.BookMinimal;
import paw.command.command.pojo.raw.Order;
import paw.command.command.services.OrderService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private ClientRepository clientRepository;

    public OrderServiceImpl(ClientRepository orderRepository) {
        this.clientRepository = orderRepository;
    }


    @Override
    public List<Order> getAllOrders() {
        return clientRepository.findAll();
    }

    @Override
    public List<Order> getAllOrdersOfClient(String clientId) {

        clientRepository.setCollectionName(clientId);
        return clientRepository.findAll();
    }

    @Override
    public Order addOrderToClient(Order order, String clientId) {
        clientRepository.setCollectionName(clientId);
        clientRepository.save(order);
        return order;
    }

    @Override
    public Order deleteOrderFromClient(String orderId, String clientId) {
        // Check if order exists
        clientRepository.setCollectionName(clientId);
        Optional<Order> order = clientRepository.findByOrderId(orderId);

        if (order.isPresent()) {

            // Delete order
            clientRepository.delete(order.get());

            // Return the order
            return order.get();
        }

        // There is no order
        return null;
    }



    @Override
    public Order updateOrder(Order order) {
        Optional<Order> toBeUpdated = clientRepository.findById(order.getOrderId());

        if (toBeUpdated.isEmpty())
            return null;

        //TODO: Validate order
        clientRepository.save(order);
        return order;
    }

    @Override
    public Order updateOrderAddItem(String orderId, BookMinimal bookMinimal) {
        Optional<Order> toBeUpdated = clientRepository.findById(orderId);
        if (toBeUpdated.isEmpty())
            return null;

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
            return null;

        Order order = toBeUpdated.get();

        BookMinimal toBeRemoved = null;
        for (BookMinimal bookMinimal : order.getItems()) {
            if (Objects.equals(bookMinimal.getIsbn(), isbn)) {
                toBeRemoved = bookMinimal;
            }
        }

        if (toBeRemoved == null)
            return null;

        order.getItems().remove(toBeRemoved);

        clientRepository.save(order);
        return order;
    }


}

package paw.command.command.services;

import paw.command.command.model.pojo.dto.BookMinimal;
import paw.command.command.model.pojo.erd.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();
    List<Order> getAllOrdersOfClient(String clientId);
    Order getOrderById(String orderId);
    Order getOrderOfClientById(String clientId, String orderId);

    Order addOrderToClient(Order order, String clientId);
    Order deleteOrderFromClient(String orderId, String clientId);

    Order updateOrder(Order order, String clientId);

    Order updateOrderAddItem(String orderId, BookMinimal bookMinimal);
    Order updateOrderRemoveItem(String orderId, String isbn);
}

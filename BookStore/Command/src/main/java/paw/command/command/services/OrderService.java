package paw.command.command.services;

import paw.command.command.pojo.raw.BookMinimal;
import paw.command.command.pojo.raw.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();
    List<Order> getAllOrdersOfClient(String clientId);
    Order addOrderToClient(Order order, String clientId);
    Order deleteOrderFromClient(String orderId, String clientId);

    Order updateOrder(Order order);

    Order updateOrderAddItem(String orderId, BookMinimal bookMinimal);
    Order updateOrderRemoveItem(String orderId, String isbn);
}

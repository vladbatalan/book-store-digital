package paw.command.command.services;

import paw.command.command.model.pojo.dto.OrderRequest;
import paw.command.command.model.pojo.erd.Order;

public interface OrderManagerService {
    Order addOrderToClient(String clientId, OrderRequest order);
}

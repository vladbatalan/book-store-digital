package paw.command.command.model.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import paw.command.command.model.pojo.erd.Order;

import java.util.Optional;

public interface ClientRepository extends MongoRepository<Order, String>, ClientRepositoryCustom {
    Optional<Order> findByOrderId(String orderId);
}

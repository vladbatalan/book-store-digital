package paw.command.command.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import paw.command.command.pojo.raw.Order;

import java.util.Optional;

public interface ClientRepository extends MongoRepository<Order, String>, ClientRepositoryCustom {
    Optional<Order> findByOrderId(String orderId);
}

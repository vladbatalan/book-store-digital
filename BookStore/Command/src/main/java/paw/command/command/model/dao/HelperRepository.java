package paw.command.command.model.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import paw.command.command.model.pojo.erd.Helper;

public interface HelperRepository extends MongoRepository<Helper, String> {
}

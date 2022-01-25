package paw.command.command.model.pojo.erd;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import paw.command.command.model.pojo.dto.BookMinimal;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "#{@clientRepository.getCollectionName()}")
public class Order implements Serializable {

    @MongoId
    private String orderId;

    private Date date;

    private List<BookMinimal> items;

    private String status;

}

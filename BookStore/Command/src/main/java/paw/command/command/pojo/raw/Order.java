package paw.command.command.pojo.raw;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import paw.command.command.pojo.raw.BookMinimal;

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

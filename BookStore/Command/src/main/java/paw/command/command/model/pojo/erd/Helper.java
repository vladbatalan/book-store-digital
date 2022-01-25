package paw.command.command.model.pojo.erd;


import lombok.Data;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import paw.command.command.model.pojo.dto.BookMinimal;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "helper")
public class Helper implements Serializable {

    @Id
    private String clientId;

    private List<String> orderList;
}

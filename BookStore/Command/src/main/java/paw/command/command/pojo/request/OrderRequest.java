package paw.command.command.pojo.request;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import paw.command.command.pojo.raw.BookMinimal;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class OrderRequest implements Serializable {
    private List<BookMinimal> items;
}

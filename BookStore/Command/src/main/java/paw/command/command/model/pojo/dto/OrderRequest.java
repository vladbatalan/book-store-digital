package paw.command.command.model.pojo.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderRequest implements Serializable {
    private List<BookMinimal> items;
}

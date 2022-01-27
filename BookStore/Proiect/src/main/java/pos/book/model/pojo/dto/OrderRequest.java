package pos.book.model.pojo.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderRequest implements Serializable {
    private List<BookMinimal> items;

    public OrderRequest(){
        items = new ArrayList<>();
    }
    public OrderRequest(List<BookMinimal> list)
    {
        items = list;
    }
}

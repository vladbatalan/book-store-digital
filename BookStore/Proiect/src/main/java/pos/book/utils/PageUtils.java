package pos.book.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageUtils {

    public static Pageable getPageOf(Integer page, Integer numberOfElements){
        if (page == null || numberOfElements == null)
            return Pageable.unpaged();

        if (page < 0 || numberOfElements <= 0)
            return Pageable.unpaged();

        return  PageRequest.of(page, numberOfElements);
    }

}

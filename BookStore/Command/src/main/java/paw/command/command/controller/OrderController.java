package paw.command.command.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import paw.command.command.pojo.raw.Book;
import paw.command.command.pojo.request.OrderRequest;
import paw.command.command.pojo.raw.BookMinimal;
import paw.command.command.pojo.raw.Order;
import paw.command.command.services.OrderService;
import paw.command.command.services.RestService;
import paw.command.command.services.exceptions.HttpException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestService restService;

//    @GetMapping(path = "")
//    public @ResponseBody
//    ResponseEntity<List<Order>> getAllOrders() {
//        List<Order> orders = orderService.getAllOrders();
//        return new ResponseEntity<>(orders, HttpStatus.OK);
//    }

    @GetMapping(path = "")
    public @ResponseBody
    ResponseEntity<List<Order>> getAllOrdersByClientId(@RequestParam String clientId) {

        List<Order> commands = orderService.getAllOrdersOfClient(clientId);
        return ResponseEntity.ok(commands);
    }

    @PostMapping(path = "")
    public @ResponseBody
    ResponseEntity<Order> addOrder(
            @RequestParam String clientId,
            @RequestBody OrderRequest orderRequest
    ) {

        //TODO: Validate the books
        boolean isOrderValid = true;
        for(BookMinimal bookRequest : orderRequest.getItems()){

            // Get book from book service
            String bookExists = restService.bookExists(bookRequest.getIsbn());
            if(bookExists == null){
                isOrderValid = false;
            }
            //TODO: Validate quantity
        }

        // If it is valid only store
        if(!isOrderValid){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }

        // Create the order
        Order order = new Order();

        // Set the date to current time
        order.setDate(new Date(System.currentTimeMillis()));

        // Order set Items
        order.setItems(orderRequest.getItems());

        // Order set status
        order.setStatus("placed");

        // Add the date to database
        Order result = orderService.addOrderToClient(order, clientId);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping(path = "/{ORDER_ID}")
    public @ResponseBody
    ResponseEntity<Order> deleteOrder(
            @RequestParam String clientId,
            @PathVariable("ORDER_ID") String orderId
    ) {

        Order order = orderService.deleteOrderFromClient(orderId, clientId);

        if (order != null)
            return ResponseEntity.status(HttpStatus.CREATED).body(order);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
//
//    @PatchMapping(path = "{ORDER_ID}")
//    public @ResponseBody
//    ResponseEntity<Order> updateOrderAddBook(
//            @PathVariable("ORDER_ID") String orderId,
//            @RequestBody Book book)
//    {
//
//        // Add the date to database
//        Order result = orderService.updateOrderAddItem(orderId, book);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(result);
//    }
//
//    @PatchMapping(path = "{ORDER_ID}/{ISBN}")
//    public @ResponseBody
//    ResponseEntity<Order> updateOrderRemoveBook(
//            @PathVariable("ORDER_ID") String orderId,
//            @PathVariable("ISBN") String isbn
//            )
//    {
//
//        // Add the date to database
//        Order result = orderService.updateOrderRemoveItem(orderId, isbn);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(result);
//    }
//
//
//    @PutMapping(path = "")
//    public @ResponseBody
//    ResponseEntity<Order> updateOrder(@RequestBody Order order){
//
//        // Add the date to database
//        Order result = orderService.updateOrder(order);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(result);
//    }

}

package paw.command.command.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import paw.command.command.model.exception.HttpResponseException;
import paw.command.command.model.pojo.dto.OrderRequest;
import paw.command.command.model.pojo.dto.BookMinimal;
import paw.command.command.model.pojo.erd.Helper;
import paw.command.command.model.pojo.erd.Order;
import paw.command.command.services.OrderManagerService;
import paw.command.command.services.OrderService;
import paw.command.command.services.RestService;

import java.util.Date;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@Controller
@RequestMapping(path = "/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderManagerService orderManagerService;

    @Autowired
    private RestService restService;

    @GetMapping(path = "")
    public @ResponseBody
    ResponseEntity<?> getAllOrders(
            @RequestParam(name = "client_id", required = false) String clientId
    ) {
        try {
            if (clientId != null)
                return ok(orderService.getAllOrdersOfClient(clientId));

            return ok(orderService.getAllOrders());
        }
        catch (HttpResponseException e){
            return status(e.getStatus()).body(e.getMessage());
        }
    }

    @PostMapping(path = "")
    public @ResponseBody
    ResponseEntity<?> addOrder(
            @RequestParam String clientId,
            @RequestBody OrderRequest orderRequest
    ) {
        try {
            // Add the order to database
            Order result = orderManagerService.addOrderToClient(clientId, orderRequest);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
        catch (HttpResponseException e){
            return status(e.getStatus()).body(e.getMessage());
        }
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

package paw.command.command.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import paw.command.command.model.exception.HttpResponseException;
import paw.command.command.model.pojo.dto.OrderRequest;
import paw.command.command.model.pojo.erd.Order;
import paw.command.command.services.OrderManagerService;
import paw.command.command.services.OrderService;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@Controller
@RequestMapping(path = "/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderManagerService orderManagerService;


    @GetMapping(path = "")
    public @ResponseBody
    ResponseEntity<?> getAllOrders(
            @RequestParam(name = "client_id", required = false) String clientId
    ) {
        try {

            if (clientId != null)
                return ok(orderService.getAllOrdersOfClient(clientId));

            return ok(orderService.getAllOrders());
        } catch (HttpResponseException e) {
            return status(e.getStatus()).body(e.getMessage());
        }
    }

    @GetMapping(path = "/{ORDER_ID}")
    public @ResponseBody
    ResponseEntity<?> getOrderById(
            @PathVariable(name = "ORDER_ID") String orderId
    ) {
        try {
            return ok(orderService.getOrderById(orderId));
        } catch (HttpResponseException e) {
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
        } catch (HttpResponseException e) {
            return status(e.getStatus()).body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/{ORDER_ID}")
    public @ResponseBody
    ResponseEntity<?> deleteOrder(
            @RequestParam String clientId,
            @PathVariable("ORDER_ID") String orderId
    ) {

        try {
            Order order = orderService.deleteOrderFromClient(orderId, clientId);
            return status(HttpStatus.OK).body(order);
        } catch (HttpResponseException e) {
            return status(e.getStatus()).body(e.getMessage());
        }
    }

    @PostMapping(path = "/{ORDER_ID}/place-order")
    public @ResponseBody
    ResponseEntity<?> placeOrder(
            @RequestParam String clientId,
            @PathVariable("ORDER_ID") String orderId
    ) {

        try {
            Order order = orderManagerService.activeOrderToClient(clientId, orderId);
            return status(HttpStatus.OK).body(order);
        } catch (HttpResponseException e) {
            return status(e.getStatus()).body(e.getMessage());
        }
    }

    @PutMapping(path = "")
    public @ResponseBody
    ResponseEntity<?> updateOrder(
            @RequestBody Order order,
            @RequestParam(name = "clientId", required = false) String clientId
    ){

        try {
            // Add the date to database
            Order result = orderService.updateOrder(order, clientId);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }catch(HttpResponseException e){
            return status(e.getStatus()).body(e.getMessage());
        }
    }

}

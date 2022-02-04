package com.javainuse.controller;

import com.javainuse.model.dto.UserNoPass;
import com.javainuse.model.erd.User;
import com.javainuse.model.exception.HttpResponseException;
import com.javainuse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/identity-provider")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping()
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        try {
            return new ResponseEntity<>(userService.saveUser(user), HttpStatus.OK);
        }
        catch(HttpResponseException e){
            return status(e.getHttpStatus()).body(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<List<UserNoPass>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserNoPass> usersNoPass = users.stream().map(UserNoPass::new).collect(Collectors.toList());
        return new ResponseEntity<>(usersNoPass, HttpStatus.OK);
    }

    @GetMapping("/{CLIENT_ID}")
    public ResponseEntity<?> getUserById(@PathVariable("CLIENT_ID") String clientId) {
        try {
            User user = userService.getUserById(clientId);
            return ok().body(new UserNoPass(user));
        } catch (HttpResponseException e) {
            return status(e.getHttpStatus()).body(e.getMessage());
        }
    }
//    @PutMapping
//    public @ResponseBody
//    ResponseEntity<?> updateUser(@RequestBody UserNoPass userNoPass) {
//        try {
//            UserUpdate updateUser = new UserUpdate(userNoPass);
//            // No change of password
//            User updated = userService.updateUser(updateUser);
//            return ok().body(updated);
//        }
//        catch (HttpResponseException e){
//            return status(e.getHttpStatus()).body(e.getMessage());
//        }
//    }

}

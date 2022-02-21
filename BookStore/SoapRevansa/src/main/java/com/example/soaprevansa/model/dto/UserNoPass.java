package com.example.soaprevansa.model.dto;


import com.example.soaprevansa.model.erd.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserNoPass implements Serializable {
    private String clientId;

    private String username;

    private String rol;

    public UserNoPass(User user) {
        clientId = user.getClientId();
        username = user.getUsername();
        rol = user.getRol();
    }

    public UserNoPass() {

    }
}

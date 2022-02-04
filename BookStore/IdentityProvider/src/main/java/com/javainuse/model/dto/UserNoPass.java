package com.javainuse.model.dto;


import com.javainuse.model.erd.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserNoPass implements Serializable {
    private String clientId;

    private String username;

    private String rol;

    public UserNoPass(User user){
        clientId = user.getClientId();
        username = user.getUsername();
        rol = user.getRol();
    }

    public UserNoPass(){

    }
}

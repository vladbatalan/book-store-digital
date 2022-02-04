package com.javainuse.model.dto;


import com.javainuse.model.erd.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserUpdate implements Serializable {

    private String clientId;
    private String username;
    private String password;
    private String new_password;
    private String rol;

    public UserUpdate(User user){
        clientId = user.getClientId();
        new_password = null;
        password = user.getPassword();
        username = user.getUsername();
        rol = user.getRol();
    }


    public UserUpdate(UserNoPass user){
        clientId = user.getClientId();
        password = null;
        new_password = null;
        username = user.getUsername();
        rol = user.getRol();
    }

    public UserUpdate(){

    }
}

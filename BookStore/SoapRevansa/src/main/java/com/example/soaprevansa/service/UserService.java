package com.example.soaprevansa.service;

import com.example.soaprevansa.model.dto.UserUpdate;
import com.example.soaprevansa.model.erd.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    List<User> getAllUsers();

    User getUserById(String userId);

    User getUserByUsername(String username);

    // UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    User updateUser(UserUpdate userUpdate);
}

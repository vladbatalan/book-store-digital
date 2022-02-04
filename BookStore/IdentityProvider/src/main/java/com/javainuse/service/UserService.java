package com.javainuse.service;

import com.javainuse.model.dto.UserUpdate;
import com.javainuse.model.erd.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    List<User> getAllUsers();

    User getUserById(String userId);

    User getUserByUsername(String username);

    // UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    User updateUser(UserUpdate userUpdate);
}

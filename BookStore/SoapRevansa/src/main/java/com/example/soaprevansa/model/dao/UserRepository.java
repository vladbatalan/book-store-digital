package com.example.soaprevansa.model.dao;

import com.example.soaprevansa.model.erd.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
    User findUserByUsername(String username);
}

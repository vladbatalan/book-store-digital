package com.javainuse.model.dao;

import com.javainuse.model.erd.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
    User findUserByUsername(String username);
}

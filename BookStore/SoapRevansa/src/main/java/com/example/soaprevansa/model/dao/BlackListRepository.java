package com.example.soaprevansa.model.dao;


import com.example.soaprevansa.model.erd.BlackList;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BlackListRepository extends CrudRepository<BlackList, String> {
    Optional<BlackList> findBlackListByToken(String token);
}

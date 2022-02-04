package com.javainuse.model.dao;

import com.javainuse.model.erd.BlackList;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BlackListRepository extends CrudRepository<BlackList, String> {
    Optional<BlackList> findBlackListByToken(String token);
}

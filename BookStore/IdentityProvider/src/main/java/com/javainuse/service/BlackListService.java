package com.javainuse.service;


import com.javainuse.model.erd.BlackList;

public interface BlackListService {
    Boolean save(String token);

    BlackList getToken(String token);

    Boolean TokenExists(String token);
}

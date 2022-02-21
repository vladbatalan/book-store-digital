package com.example.soaprevansa.service;


import com.example.soaprevansa.model.erd.BlackList;

public interface BlackListService {
    Boolean save(String token);

    BlackList getToken(String token);

    Boolean TokenExists(String token);
}

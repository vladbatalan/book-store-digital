package com.example.soaprevansa.service.impl;


import com.example.soaprevansa.model.dao.BlackListRepository;
import com.example.soaprevansa.model.erd.BlackList;
import com.example.soaprevansa.service.BlackListService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BlackListServiceImpl implements BlackListService {
    private final BlackListRepository blackListRepository;

    public BlackListServiceImpl(BlackListRepository blackListRepository) {
        this.blackListRepository = blackListRepository;
    }

    @Override
    public Boolean save(String token) {
        BlackList blackList = new BlackList();
        blackList.setToken(token);
        try {
            blackListRepository.save(blackList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public BlackList getToken(String token) {
        return null;
    }

    @Override
    public Boolean TokenExists(String token) {
        Optional<BlackList> blackListOptional = blackListRepository.findById(token);
        // System.out.println(blackListOptional.isPresent());
        return blackListOptional.isPresent();
    }
}

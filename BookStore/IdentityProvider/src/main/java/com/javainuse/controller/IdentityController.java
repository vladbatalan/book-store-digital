package com.javainuse.controller;

import com.javainuse.config.security.JwtTokenUtil;
import com.javainuse.model.dto.UserFromUserDetails;
import com.javainuse.model.erd.User;
import com.javainuse.service.BlackListService;
import com.javainuse.service.UserService;
import javainuse.com.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


@Endpoint
public class IdentityController {
    private static final String NAMESPACE_URI = "http://com.javainuse/provider";



    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private BlackListService blackListService;

    @Autowired
    private UserService userService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addRequest")
    @ResponsePayload
    public AddResponse add(@RequestPayload AddRequest input){
        AddResponse result = new AddResponse();

        result.setResult(input.getParam1().add(input.getParam2()));

        return result;
    }

}

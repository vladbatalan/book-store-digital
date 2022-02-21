package com.example.soaprevansa.stateless.endpoints;

import com.example.soaprevansa.model.dto.UserFromUserDetails;
import com.example.soaprevansa.model.dto.UserNoPass;
import com.example.soaprevansa.model.erd.User;
import com.example.soaprevansa.security.JwtTokenUtil;
import com.example.soaprevansa.service.BlackListService;
import com.example.soaprevansa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import stateless.soaprevansa.example.com.calculator.*;


@Endpoint
public class CalculatorEndpoint {
    private static final String NAMESPACE_URI = "http://com.example.soaprevansa.stateless/Calculator";

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

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "validateTokenRequest")
    @ResponsePayload
    public ValidateTokenResponse validate(@RequestPayload ValidateTokenRequest input){
        ValidateTokenResponse result = new ValidateTokenResponse();

        String username = jwtTokenUtil.getUsernameFromToken(input.getToken());
        User user = userService.getUserByUsername(username);
        if(user == null){
            return null;
        }

        try {
            boolean valid = jwtTokenUtil.validateToken(input.getToken(), new UserFromUserDetails(user));

            // Return value
            if (!valid)
                return null;

            result.setRol(user.getRol());
            result.setClientId(user.getClientId());
            result.setUsername(username);
        }
        catch (RuntimeException e){
            return null;
        }



        return result;
    }
}

package com.javainuse.model.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class HttpResponseException extends RuntimeException{
    private HttpStatus httpStatus;
    private String message;
    private Object returnedObject;

    public HttpResponseException(String message, HttpStatus httpStatus, Object returnedObject){
        this.httpStatus = httpStatus;
        this.message = message;
        this.returnedObject = returnedObject;
    }
    public HttpResponseException(String message, HttpStatus httpStatus){
        this.httpStatus = httpStatus;
        this.message = message;
        this.returnedObject = null;
    }

    @Override
    public String toString() {
        return "HttpResponseException{" +
                "httpStatus=" + httpStatus +
                ", message='" + message + '\'' +
                ", returnedObject=" + returnedObject +
                '}';
    }
}

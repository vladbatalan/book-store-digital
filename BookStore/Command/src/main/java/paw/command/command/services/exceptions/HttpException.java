package paw.command.command.services.exceptions;

import org.springframework.http.HttpStatus;

public class HttpException extends Exception
{
    private HttpStatus httpStatus;
    public HttpException(HttpStatus httpStatus){
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}

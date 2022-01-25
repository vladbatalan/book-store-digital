package paw.command.command.model.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class HttpResponseException extends RuntimeException {

    private HttpStatus status;

    public HttpResponseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}

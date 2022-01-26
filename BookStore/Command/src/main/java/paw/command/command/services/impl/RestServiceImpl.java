package paw.command.command.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import paw.command.command.model.exception.HttpResponseException;
import paw.command.command.model.pojo.dto.Book;
import paw.command.command.model.pojo.dto.BookMinimal;
import paw.command.command.services.RestService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestServiceImpl implements RestService {

    private final WebClient.Builder webClientBuilder;

    private final String abcServiceUrl = "http://localhost:8081/api/bookcollection";

    private final ObjectMapper objectMapper;

    public RestServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;

        //create ObjectMapper instance
        objectMapper = new ObjectMapper();
    }

    @Override
    public BookMinimal bookMinimalExists(String isbn) {
        Book book = getBook(isbn);
        return book!=null? new BookMinimal(book) : null;
    }

    @Override
    public Book bookExists(String isbn) {
        return getBook(isbn);
    }

    @Override
    public List<BookMinimal> validateItemsOfCommand(List<BookMinimal> list) {
        // Create url for get action
        String url = abcServiceUrl + "/validate";

        try {
            // Create the web client
            String bookExists = webClientBuilder.build()
                    .post()
                    .uri(url)
                    .body(list, List.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            List<?> genericList = objectMapper.readValue(bookExists, List.class);

            return genericList.stream().map(it -> (BookMinimal)it).collect(Collectors.toList());
        } catch (WebClientResponseException e){
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND))
                return null;
            throw new HttpResponseException(e.getMessage(),
                    e.getStatusCode());
        } catch (JsonProcessingException e) {
            throw new HttpResponseException("Wrong object format received from BookCollection.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Book getBook(String isbn){
        // Create url for get action
        String url = abcServiceUrl + "/books/{ISBN}";

        try {
            // Create the web client
            String bookExists = webClientBuilder.build()
                    .get()
                    .uri(url, isbn)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            // System.out.println(bookExists);

            return objectMapper.readValue(bookExists, Book.class);
        } catch (WebClientResponseException e){
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND))
                return null;
            throw new HttpResponseException(e.getMessage(),
                    e.getStatusCode());
        } catch (JsonProcessingException e) {
            throw new HttpResponseException("Wrong object format received from BookCollection.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

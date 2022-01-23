package paw.command.command.services.impl;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import paw.command.command.services.RestService;
import paw.command.command.services.exceptions.HttpException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
public class RestServiceImpl implements RestService {

    private final WebClient.Builder webClientBuilder;

    private final String abcServiceUrl = "http://localhost:8081/api/bookcollection";

    public RestServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public String bookExists(String isbn) {

        // Create url for get action
        String url = abcServiceUrl + "/books/{ISBN}";
        String bookExists;
        try {
            bookExists = webClientBuilder.build()
                    .get()
                    .uri(url, isbn)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        }
        catch(RuntimeException exception){
            return null;
        }

        return bookExists;
    }
}

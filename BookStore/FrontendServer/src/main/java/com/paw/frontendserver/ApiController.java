package com.paw.frontendserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {
        RequestMethod.GET,
        RequestMethod.DELETE,
        RequestMethod.PATCH,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.HEAD,
        RequestMethod.OPTIONS
})
@RequestMapping(path = "/api/**")
public class ApiController {

    @Autowired
    private WebClient.Builder webClientBuilder;

    private final Integer BACKEND_PORT = 4300;
    private final Integer BOOK_COLLECTION_PORT = 8081;
    private final Integer COMMAND_PORT = 8082;

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<?> servicesGateway(HttpServletRequest httpServletRequest) throws IOException {

        // Extract the uri
        StringBuilder requestURL = new StringBuilder(httpServletRequest.getRequestURI());
        String queryString = httpServletRequest.getQueryString();

        if (queryString != null)
            requestURL.append("?").append(queryString);

        Map<String, String> headerInfo = new HashMap<String, String>();
        Enumeration headerNames = httpServletRequest.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = httpServletRequest.getHeader(key);
            headerInfo.put(key, value);
        }

        // Get the body of the request
        String body = httpServletRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        WebClient.RequestHeadersSpec<?> preHeaders = webClientBuilder.build()
                .method(HttpMethod.valueOf(httpServletRequest.getMethod()))
                .uri(String.format("http://localhost:%d/%s", BACKEND_PORT, requestURL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(body), String.class);

        for (String headerName : headerInfo.keySet()) {
            preHeaders = preHeaders.header(headerName, headerInfo.get(headerName));
        }

        try {
            return preHeaders
                    .retrieve()
                    .onStatus(HttpStatus::isError,
                            clientResponse ->
                                    Mono.error(new HttpResponseException("Error: " + clientResponse.statusCode() +
                                            "\n" + clientResponse.bodyToMono(String.class),
                                            clientResponse.statusCode().value()))
                    )
                    .bodyToMono(String.class)
                    .map(s -> ResponseEntity.ok().body(s))
                    .block();
        }catch (HttpResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }
}

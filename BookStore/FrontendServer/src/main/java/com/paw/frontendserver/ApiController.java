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
    private final Integer IDENTITY_PROVIDER_PORT = 8803;

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<?> servicesGateway(HttpServletRequest httpServletRequest) throws IOException {

        // Extract the uri

        String requestUri = httpServletRequest.getRequestURI();
        StringBuilder requestURL = new StringBuilder(requestUri);
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

        // Set the url to access
        Integer backendPort = null;
        System.out.println(requestUri);
        if(requestUri.startsWith("/api/bookcollection"))
            backendPort = BOOK_COLLECTION_PORT;
        if(requestUri.startsWith("/api/orders"))
            backendPort = COMMAND_PORT;
        if(requestUri.startsWith("/api/identity-provider"))
            backendPort = IDENTITY_PROVIDER_PORT;

        if(backendPort == null)
            throw new HttpResponseException("Service not found", HttpStatus.NOT_FOUND.value());

        // Get the body of the request
        String body = httpServletRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        WebClient.RequestHeadersSpec<?> preHeaders = webClientBuilder.build()
                .method(HttpMethod.valueOf(httpServletRequest.getMethod()))
                .uri(String.format("http://localhost:%d/%s", backendPort, requestURL))
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

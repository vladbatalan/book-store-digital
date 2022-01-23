package paw.command.command.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import paw.command.command.services.OpenApiService;

@Controller
@RequestMapping(path = "/api")
public class OpenApiController {

    @Autowired
    private OpenApiService openApiService;

    @RequestMapping(
            value = "/orders",
            method = RequestMethod.OPTIONS,
            produces = "application/json")
    public @ResponseBody
    ResponseEntity<String> getEntireDocument() {
        return ResponseEntity.ok().body(openApiService.getEntireDocument());
    }
}

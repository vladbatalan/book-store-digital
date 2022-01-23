package pos.book.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pos.book.service.OpenApiService;

@Controller
@RequestMapping(path = "/api")
public class OpenApiController {

    @Autowired
    private OpenApiService openApiService;

    @RequestMapping(
            value="/bookcollection",
            method= RequestMethod.OPTIONS,
            produces = "application/json")
    public @ResponseBody
    ResponseEntity<String> getEntireDocument(){
        return ResponseEntity.ok().body(openApiService.getBookcollectionDoc());
    }


    @RequestMapping(
            value="/bookcollection/books",
            method= RequestMethod.OPTIONS,
            produces = "application/json")
    public @ResponseBody
    ResponseEntity<String> getBookDocument(){
        return ResponseEntity.ok().body(openApiService.getBookDoc());
    }

    @RequestMapping(
            value="/bookcollection/authors",
            method= RequestMethod.OPTIONS,
            produces = "application/json")
    public @ResponseBody
    ResponseEntity<String> getAuthorDocument(){
        return ResponseEntity.ok().body(openApiService.getAuthorDoc());
    }

}

package pos.book.service.impl;

import com.fasterxml.jackson.core.JsonParser;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pos.book.service.OpenApiService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class OpenApiServiceImpl implements OpenApiService {

    @Value("${springdoc.api-docs.path}")
    private String apiDocPath;

    @Value("${server.port}")
    private String serverPort;

    @Override
    public String getBookcollectionDoc() {
        try{
            return getOpenApiDoc();
        }
        catch (Exception ex){
            return ex.getMessage();
        }
    }

    @Override
    public String getBookDoc() {
        try{
            String lookFor = "/api/bookcollection/books/";
            String document = getOpenApiDoc();

            return getSelectedPathsFromDoc(document, lookFor);

        }
        catch (Exception ex){
            return ex.getMessage();
        }
    }

    @Override
    public String getAuthorDoc() {
        try{
            String lookFor = "/api/bookcollection/authors/";
            String document = getOpenApiDoc();

            return getSelectedPathsFromDoc(document, lookFor);
        }
        catch (Exception ex){
            return ex.getMessage();
        }
    }

    private String getSelectedPathsFromDoc(String document, String lookFor) throws ParseException {
        // Create JSONObject based on the document
        JSONParser parser = new JSONParser();
        JSONObject wholeDoc = (JSONObject) parser.parse(document);

        // get only /api/bookcollection/books paths
        if(wholeDoc.containsKey("paths")){
            JSONObject paths = (JSONObject) wholeDoc.get("paths");
            List<String> toBeRemoved = new ArrayList<>();

            for(String key: paths.keySet()){
                if(!key.startsWith(lookFor)){
                    toBeRemoved.add(key);
                }
            }

            for(String deleteKey : toBeRemoved){
                paths.remove(deleteKey);
            }
        }

        return wholeDoc.toString();
    }

    private String getOpenApiDoc() throws IOException {

        String urlLink = "http://" + "localhost:" + serverPort + apiDocPath;
        URL url = new URL(urlLink);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // Options
        con.setRequestMethod("GET");
        con.setInstanceFollowRedirects(true);
        int status = con.getResponseCode();

        if(status == 200) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            con.disconnect();
            return content.toString();
        }
        con.disconnect();
        return null;
    }
}

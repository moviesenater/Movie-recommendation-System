import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Scanner;

public class http {
    public static void main(String[] args) throws Exception {
        Scanner s = new Scanner(System.in);
        String action = s.next();
        String lang = s.next();
        s.close();
        HttpRequest request = HttpRequest.newBuilder(new URI("https://ott-details.p.rapidapi.com/advancedsearch?start_year=2020&end_year=2020&min_imdb=6&max_imdb=7.8&genre=" + action + "&language=" + lang + "&type=movie&sort=latest&page=1"))
                .header("X-RapidAPI-Key", "65fd17147fmshc51ddabff33471cp1f7ccfjsn59de4e138bd0")
                .header("X-RapidAPI-Host", "ott-details.p.rapidapi.com")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        String responseBody = response.body();

        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(responseBody);

        // Access the data from the JSON object
        JSONArray results = (JSONArray) json.get("results");

        // Iterate over the results array and access individual objects
        for (int i = 0; i < results.size(); i++) {
            JSONObject movie = (JSONObject) results.get(i);

            // Access specific properties of each movie object
            String title = (String) movie.get("title");
            //long year = (long)movie.get("year");

            // Do something with the movie data
            System.out.println("Title: " + title);
           // System.out.println("Year: " + year);
        }
    }
}
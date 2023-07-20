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
        System.out.print("Enter genre: ");
        String genre = s.nextLine();
        System.out.print("Enter language: ");
        String lang = s.next();
        s.close();
        try{
            HttpRequest request = HttpRequest.newBuilder(new URI("https://ott-details.p.rapidapi.com/advancedsearch?start_year=2020&end_year=2020&min_imdb=6&max_imdb=7.8&genre=" + genre + "&language=" + lang + "&type=movie&sort=latest&page=1"))
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
                JSONArray img = (JSONArray)movie.get("imageurl");
            // Do something with the movie data
                System.out.print("Title: " + title);
                try{
                System.out.print("   IMG: " + (String)img.get(0));
                }
                catch(IndexOutOfBoundsException io){
                System.out.print("  NO IMAGE PRESENT");
                }
                try{
                    Double rating = (Double)movie.get("imdbrating");
                    System.out.println("   Rating: " + rating);
                }
                catch(Exception e){
                    long rating = (long)movie.get("imdbrating");
                    System.out.println("   Rating: " + rating);
                }
            
            }
        }
        catch(Exception e){
            System.out.println("OOPS!!! Error Occured, Have A Look");
        }
    }
}
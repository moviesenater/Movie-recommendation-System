import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class http {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(http::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Movie Search");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create input fields and fetch button
        JTextField genreField = new JTextField(20);
        JTextField languageField = new JTextField(20);
        JButton fetchButton = new JButton("Fetch Movies");

        // Create table to display the movie data
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Title", "Image URL", "Rating"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        fetchButton.addActionListener(e -> {
            String genre = genreField.getText();
            String lang = languageField.getText();
            fetchMovies(genre, lang, tableModel);
        });

        // Create input panel
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Enter genre:"));
        inputPanel.add(genreField);
        inputPanel.add(new JLabel("Enter language:"));
        inputPanel.add(languageField);
        inputPanel.add(fetchButton);

        // Add components to the frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

    public static void fetchMovies(String genre, String lang, DefaultTableModel tableModel) {
        try {
            String apiUrl = "https://ott-details.p.rapidapi.com/advancedsearch?start_year=2020&end_year=2020&min_imdb=6&max_imdb=7.8&genre=" + genre + "&language=" + lang + "&type=movie&sort=latest&page=1";

            HttpRequest request = HttpRequest.newBuilder(new URI(apiUrl))
                    .header("X-RapidAPI-Key", "0c41303e37msh3ca9c5049b62129p1efd89jsndaaf78084778") // Replace with your API key
                    .header("X-RapidAPI-Host", "ott-details.p.rapidapi.com")
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            String responseBody = response.body();
            System.out.println(responseBody); // Print the API response in the console

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(responseBody);
            JSONArray results = (JSONArray) json.get("results");

            // Clear the existing table data
            tableModel.setRowCount(0);

            if (results == null || results.isEmpty()) {
                // Display a message in the GUI when no movies are found
                JOptionPane.showMessageDialog(null, "No movies found for the given genre and language.", "No Movies", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Iterate over the first 10 results and add them to the table
                int limit = Math.min(results.size(), 10); // Limit to the first 10 movies
                for (int i = 0; i < limit; i++) {
                    JSONObject movie = (JSONObject) results.get(i);
                    String title = (String) movie.get("title");
                    JSONArray img = (JSONArray) movie.get("imageurl");

                    Object[] rowData = new Object[3];
                    rowData[0] = title;
                    rowData[1] = (img != null && !img.isEmpty()) ? img.get(0) : "No Image";

                    try {
                        Double rating = (Double) movie.get("imdbrating");
                        rowData[2] = rating;
                    } catch (Exception e) {
                        long rating = (long) movie.get("imdbrating");
                        rowData[2] = rating;
                    }

                    tableModel.addRow(rowData);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}

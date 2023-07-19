package org.java;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class http{
    private static final int MAX_MOVIES_PER_GENRE = 10;
    private static Map<String, List<JCheckBox>> selectedMoviesPerGenre = new HashMap<>();
    private static JFrame frame;
    private static JPanel movieListPanel;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public static void createAndShowGUI() {
        frame = new JFrame("Movie Selection");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField actionField = new JTextField();
        JTextField langField = new JTextField();
        inputPanel.add(new JLabel("Action Genre:"));
        inputPanel.add(actionField);
        inputPanel.add(new JLabel("Language:"));
        inputPanel.add(langField);

        // Button to Fetch Movies
        JButton fetchButton = new JButton("Fetch Movies");
        fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String action = actionField.getText();
                    String lang = langField.getText();
                    fetchMovies(action, lang);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Movie List Panel
        movieListPanel = new JPanel(new GridLayout(0, 1, 10, 10));

        // Add components to the frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(fetchButton, BorderLayout.CENTER);
        frame.add(movieListPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }

    public static void fetchMovies(String action, String lang) throws Exception {
        String apiUrl = "https://ott-details.p.rapidapi.com/advancedsearch?start_year=2020&end_year=2020&min_imdb=6&max_imdb=7.8&genre=" + action + "&language=" + lang + "&type=movie&sort=latest&page=1";
        HttpRequest request = HttpRequest.newBuilder(new URI(apiUrl))
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

        // Clear the movie list panel before adding new movies
        movieListPanel.removeAll();
        selectedMoviesPerGenre.put(action, new ArrayList<>());

        // Iterate over the results array and access individual objects
        int count = 0;
        for (int i = 0; i < results.size() && count < MAX_MOVIES_PER_GENRE; i++) {
            JSONObject movie = (JSONObject) results.get(i);
            String title = (String) movie.get("title");

            JCheckBox checkBox = new JCheckBox(title);
            selectedMoviesPerGenre.get(action).add(checkBox);
            movieListPanel.add(checkBox);
            count++;
        }

        // Refresh the UI
        frame.pack();
    }
}


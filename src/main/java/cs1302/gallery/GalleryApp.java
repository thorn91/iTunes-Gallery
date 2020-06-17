package cs1302.gallery;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Priority;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.net.URL;
import java.net.URLEncoder;
import java.net.MalformedURLException;
import cs1302.gallery.GalleryBar;
import cs1302.gallery.GalleryPictures;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.UnsupportedEncodingException;
import java.io.InputStreamReader;
import javafx.scene.control.ProgressBar;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.util.stream.Collectors;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * An iTunes gallery app that defaults to Jake Paul.
 */
public class GalleryApp extends Application {

    private ActionBar ab;
    private GalleryPictures pictures;
    private ProgressBar pb;
    private KeyFrame keyFrame;
    private Timeline timeline;
    private boolean startRandom = false;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage stage) {
        VBox pane = new VBox();

        // Menu Bar section
        GalleryBar gb = new GalleryBar();
        //pane.setHgrow(gb, Priority.ALWAYS);
        gb.setMaxWidth(Integer.MAX_VALUE);

        // Pictures (20)
        pictures = new GalleryPictures();
        pictures.setMaxWidth(Integer.MAX_VALUE);

        // Action Bar
        ab = new ActionBar();
        ab.setMaxWidth(Integer.MAX_VALUE);

        // Query action
        ab.searchButton.setOnAction(this::startQuery);

        // Progress bar
        HBox loadingBar = new HBox(5);
        pb = new ProgressBar(0);
        loadingBar.getChildren().add(pb);
        loadingBar.setMaxWidth(Integer.MAX_VALUE);
        
        // Add elements in order
        pane.getChildren().addAll(gb, ab, pictures, loadingBar);

        // Set starting images
        startUp();

        // Set progress bar monitor
        watchProgress();

        startRandom();
        // Pause button
        EventHandler<ActionEvent> pauseHandler = e -> {
            if (ab.pauseButton.getText().equals("Pause")) {
                timeline.pause();
                ab.pauseButton.setText("Play");
            } else {
                timeline.play();
                ab.pauseButton.setText("Pause");
            }
            
        };

        ab.pauseButton.setOnAction(pauseHandler);

        // Final touches
        Scene scene = new Scene(pane);
        stage.setMaxWidth(400);
        stage.setMaxHeight(500);
        stage.setResizable(false);
        stage.setTitle("GalleryApp!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    } // start

    /**
     * Starts the randomization process.
     */
    private void startRandom() {
        // Random replacement
        EventHandler<ActionEvent> handler = event -> {
            pictures.updateOnePic();
        };
        keyFrame = new KeyFrame(Duration.seconds(2), handler);
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    /**
     * Monitors the progress bar.  If it is ~1.0 the loading is complete
     * and it should be hidden.
     */
    private void watchProgress() {
        // Random replacement
        EventHandler<ActionEvent> handler = event -> {
            //System.out.println(pb.getProgress());
            if (pb.getProgress() >= 0.9999999999) {
                pb.setProgress(0.0);
                pb.setVisible(false);
            }
        };
        KeyFrame kf = new KeyFrame(Duration.seconds(2), handler);
        Timeline tl = new Timeline();
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.getKeyFrames().add(kf);
        tl.play();
    }

    /**
     * Starts the process of updating the query by sending for
     * a JSON request and sending it.
     * to the {@code GalleryPictures} control module.
     *
     * @param e an action event.
     */
    private void startQuery(ActionEvent e) {
        pb.setVisible(true);
        String base = "https://itunes.apple.com/search?";

        // Get search terms and encode them onto the end of the base
        String[] terms = ab.getText().split(" ");

        try {
            //base += URLEncoder.encode("term=", "UTF-8");
            base += "term=";

            base += URLEncoder.encode(ab.getText(), "UTF-8");
            base += "&media=music";
            //base += URLEncoder.encode("&media=music", "UTF-8");


        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        }

        // Get the artwork list
        ArrayList<JsonElement> artworkUrl100 = handleJson(base);

        // check for insufficient query 
        if (artworkUrl100.size() < 20) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText("Not enough results, so don't search that.");
            alert.showAndWait();
            return;
        }

        // give the list to GalleryDriver to work its magic
        pictures.updatePictures(artworkUrl100, pb);

        // reset progress and allow start random to work
        pb.setProgress(0.0);

        // reset to "Pause" and random
        timeline.play();
        ab.pauseButton.setText("Pause");
    }


    /**
     * Fetches the JSON result from the base Url.  Responsible for
     * clearing nulls and checking the size of the array.
     *
     * @param base a {@code String} representing the formatted URL.
     * @return an {@code ArrayList<JsonElement>} containing image URLs.
     */
    private ArrayList<JsonElement> handleJson(String base) {
        // Get the JSON result
        JsonElement je = getJson(base);

        // If the result is < 20 return, otherwise continue to create the image array
        JsonObject root = je.getAsJsonObject();
        JsonArray results = root.getAsJsonArray("results");

        // Gather the results into a nice little array and return it
        ArrayList<JsonElement> artworkUrl100 = new ArrayList<JsonElement>();
       

        for (int i = 0; i < results.size(); i++) {
            JsonObject result = results.get(i).getAsJsonObject();
            artworkUrl100.add(result.get("artworkUrl100"));
        }

        // clear nulls out
        for (int i = 0; i < artworkUrl100.size(); i++) {
            if (artworkUrl100.get(i) == null) {
                artworkUrl100.remove(i);
            }
        }

        // Eliminate duplicate pictures
        ArrayList<JsonElement> a = artworkUrl100.stream()
             .distinct()
             .collect(Collectors.toCollection(ArrayList::new));

        return a;
    }
    

    /**
     * Gets a Json object back from the API search.
     *
     * @param base the encoded url.
     *
     * @return a json element containing the query result.
     */
    private JsonElement getJson(String base) {
        try {
            URL url = new URL(base);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonElement je = JsonParser.parseReader(reader);

            return je;
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        // default bad return
        return null;

    }

    /**
     * Used when the program first starts to update the cells.
     */
    private void startUp() {
        String base = "https://itunes.apple.com/search?";

        // Default search term
        String term = "Jake Paul";

        try {
            //base += URLEncoder.encode("term=", "UTF-8");
            base += "term=";

            base += URLEncoder.encode(term, "UTF-8");
            base += "&limit=200&media=music";
            //base += URLEncoder.encode("&media=music", "UTF-8");


        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        }

        // Get the artwork list
        ArrayList<JsonElement> artworkUrl100 = handleJson(base);

        // check for insufficient query 
        if (artworkUrl100.size() < 20) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText("Not enough results, so don't search that.");
            alert.showAndWait();
            return;
        }

        // give the list to GalleryDriver to work its magic
        pictures.updatePictures(artworkUrl100, pb);

        // reset progress and allow start random to work
        pb.setProgress(0.0);
        pb.setVisible(false);

    }


} // GalleryApp

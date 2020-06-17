package cs1302.gallery;

import javafx.scene.layout.TilePane;
import javafx.scene.control.ProgressBar;
import cs1302.gallery.Picture;
import java.util.ArrayList;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.util.Random;



/**
 * Represents a collection of picture images.
 *
 * Responsible for query parsing and image setting.
 */
public class GalleryPictures extends TilePane {
    /** Controls the randomization. */
    ArrayList<JsonElement> pictureSet;
    Picture[] pictures;
    String[] pictureUrls;

    /**
     * Constructs a {@code GalleryPictures} object that represents a
     * collection of picture objects.
     */
    public GalleryPictures() {
        super();

        pictureSet = null;

        // General settings
        this.setPrefColumns(4);
        this.setPrefRows(5);

        // Construct picture images and fill the pane
        pictures = new Picture[20];
        pictureUrls = new String[20];
        for (int i = 0; i < 20; i++) {
            pictures[i] = new Picture();
            this.getChildren().add(pictures[i]);
            pictureUrls[i] = "";
        }

        
    }

    /**
     * Used to set a single picture for random replacement.
     */
    public void updateOnePic() {

        Random generator = new Random();
        int i = generator.nextInt(20);

        Runnable r = getRunnable(i);
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.start();

    }

    /**
     * Updates all new pictures with a randomly pulled elements from
     * the {@code pictureSet} of image URLs.
     *
     * @param artworks  an {@code ArrayList<JsonElements>} that
     * contains a list of URLs of images.
     * @param pb        the reference to a {@code ProgressBar}.
     */
    public void updatePictures(ArrayList<JsonElement> artworks,
                               ProgressBar pb) {
        double progress = 0.0;

        // Update the picture set so we have a new random picker
        pictureSet = new ArrayList<JsonElement>(artworks);

        Random generator = new Random();

        for (int i = 0; i < 20; i++) {

            Runnable r = getRunnable(i, pb);
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.start();
        }

    }

    /**
     * Provides a {@code Runnable} to be used to update a picture
     * in order of 1 to 20.
     * @param i  the integer corresponding to the picture to change
     * @param pb a reference to the main {@code ProgressBar}
     *
     * @return a {@code Runnable} used to update a picture and the
     * {@code ProgressBar}.
     */
    private Runnable getRunnable(int i, ProgressBar pb) {

        Runnable r = () -> {
            Random generator = new Random();

            int randNum = generator.nextInt(pictureSet.size());
            String url = pictureSet.get(randNum).toString();
            pictures[i].loadImage(url.substring(1, url.length() - 1), pb);

        };

        return r;
    }

    /**
     * Provides a {@code Runnable} to be used to update a single
     * picture.  As such it is an overloaded version of the main
     * function to not update the progress bar.
     *
     * @param i  the integer corresponding to the picture to change.
     *
     * @return a {@code Runnable} used to update a picture
     */
    private Runnable getRunnable(int i) {

        Runnable r = () -> {
            Random generator = new Random();

            int randNum = generator.nextInt(pictureSet.size());
            String url = pictureSet.get(randNum).toString();

            // Check for duplicate pictures
            if (pictureUrls[i].equals(url.substring(1, url.length() - 1))) {

                // Make sure we don't exceed the bounds of the array
                if (randNum == pictureSet.size()) {
                    randNum -= 1;
                } else {
                    randNum += 1;
                }

                // Add the picture
                pictures[i].loadImage(url.substring(1, url.length() - 1));

            } else {

                // No duplicate no problem
                pictures[i].loadImage(url.substring(1, url.length() - 1));
            }
        };

        return r;
    }



}

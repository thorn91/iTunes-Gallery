package cs1302.gallery;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.control.ProgressBar;

/**
 * Represents a single picture object with getters and setters for the picture.
 */
public class Picture extends VBox {
    
    /** A default image which loads when the application starts. */
    private static final String DEFAULT_IMG = "http://cobweb.cs.uga.edu/~mec/cs1302/gui/default.png";

    /** Default height and width for Images. */
    private static final int DEF_HEIGHT = 100;
    private static final int DEF_WIDTH = 100;

    ImageView imgView;
    Image defaultImage;
    Image newImg;

    /**
     * Constructs a single picture.
     */
    public Picture() {
        super();

        defaultImage = new Image(DEFAULT_IMG, DEF_HEIGHT, DEF_WIDTH, false, false);
        imgView = new ImageView(defaultImage);
        imgView.setPreserveRatio(true);

        this.getChildren().addAll(imgView);

    }

    /**
     * Sets the image to the corresponding picture URL information.
     *
     * @param url  the url to a new image.
     * @param pb   the progressbar
     */
    public void loadImage(String url, ProgressBar pb) {
        try {
            Runnable r = () -> {
                newImg = new Image(url, DEF_HEIGHT, DEF_WIDTH, false, false);
                imgView.setImage(newImg);
                pb.setProgress(pb.getProgress() + 0.05);
            };
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.start();
        } catch (IllegalArgumentException iae) {
            //iae.printStackTrace();
            System.out.println("Supplied URL is invalid.");
        }
    }

    /**
     * Sets the image to the corresponding picture URL information.
     * This method does not update the progress bar and is used for random replacement.
     * 
     * @param url  the url to a new image.
     */
    public void loadImage(String url) {
        try {
            Runnable r = () -> {
                newImg = new Image(url, DEF_HEIGHT, DEF_WIDTH, false, false);
                imgView.setImage(newImg);
            };
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.start();
        } catch (IllegalArgumentException iae) {
            //iae.printStackTrace();
            System.out.println("Supplied URL is invalid.");
        }
    }



    /**
     * Returns the current image.
     *
     * @return {@code Image} current set by that box.
     */
    public Image getImg() {
        return imgView.getImage(); 
    }

}

package cs1302.gallery;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;


/**
 * An action bar of 3 default options: pause, search query, and update images.
 */
public class ActionBar extends HBox {

    /** These are in order of appearance. */
    public Button pauseButton;
    private Label label;
    private TextField searchField;
    public Button searchButton;

    /**
     * Constructs an action bar consisting of the above elements and only the above
     * elements.
     */
    public ActionBar() {
        super(5);
        this.setAlignment(Pos.CENTER);

        pauseButton = new Button("Pause");
        label = new Label("Search Query:");
        searchField = new TextField();
        searchButton = new Button("Update");

        this.getChildren().addAll(pauseButton, label, searchField, searchButton);

        // Sending a new search query to the GalleryPictures by changing its default field
    }

    /**
     * Returns the value in the search field.
     *
     * @return the value from the search field.
     */
    public String getText() {
        return searchField.getText();
    }
    
}

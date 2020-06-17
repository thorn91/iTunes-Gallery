package cs1302.gallery;

import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Modality;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;


/**
 * Represents a menu bar.
 */
public class GalleryBar extends MenuBar {
    MenuItem exit;
    MenuItem about;
    Menu fileMenu;
    Menu helpMenu;

    /**
     * Constructs a {@code GalleryBar} to be implemented in
     * an above class.
     */
    public GalleryBar() {
        super();

        exit = new MenuItem("Exit");
        fileMenu = new Menu("File");
        fileMenu.getItems().add(exit);

        about = new MenuItem("About");
        helpMenu = new Menu("Help");
        helpMenu.getItems().add(about);

        // Add menu items
        this.getMenus().addAll(fileMenu, helpMenu);

        // Set exit function
        EventHandler<ActionEvent> exitHandler = e -> {
            System.exit(0);
        };
        exit.setOnAction(exitHandler);

        about.setOnAction(this::helpHandler);
    }


    /**
     * Handles the popup for the help/about menu.
     *
     * @param e the {@code ActionEvent} used to trigger the box.
     */
    private void helpHandler(ActionEvent e) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About Thomas Horn!");
        alert.setHeaderText(null);

        Image img = new Image("file:resources/prof_pic.jpg");
        ImageView imgView = new ImageView(img);
        alert.setGraphic(imgView);

        String conText = "";
        conText += "Author: Thomas Horn (not the baby)\n";
        conText += "Email: Thomas.Horn@uga.edu\n";
        conText += "\nImages provided courtesy of iTunes\n";
        conText += "\nVersion: 0.01\n";
        alert.setContentText(conText);
        alert.showAndWait();
    }

}

package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * JavaFX App
 */
public class App extends Application {

    /*==================================================*
     *==             START APPLICATION                ==*
     *==================================================*/

    public static void main(String[] args) {
        launch();
    }

    /*==================================================*
     *==                   FIELDS                     ==*
     *==================================================*/

    private FXMLLoader fxmlLoader;

    /*==================================================*
     *==               JAVAFX-STARTER                 ==*
     *==================================================*/

    @Override
    public void start(Stage stage) throws IOException {

        // create FXML loader
        this.fxmlLoader = new FXMLLoader();

        // init
        initLanguage();
        initIcons(stage);

        // create scene
        Scene scene = new Scene(loadFXML("pane"), 400, 400);
        stage.setScene(scene);
        stage.setTitle("ChecksumVerifier");

        // show window
        stage.show();
    }

    /*==================================================*
     *==               INITIALIZATION                 ==*
     *==================================================*/

    /**
     * Loads the resource bundle for internationalization.
     *
     * Chooses German if system is set so, else English as default language.
     */
    private void initLanguage() {

        // get location of language bundle
        String bundlesPkg = getClass().getPackageName() + ".bundles.language";

        // check if German is set to system language
        boolean germanAsSystemLanguage = Locale.getDefault().getLanguage().equals(new Locale("de").getLanguage());

        // load German package (if system is set so) or English package as default language
        if (germanAsSystemLanguage) {
            this.fxmlLoader.setResources(ResourceBundle.getBundle(bundlesPkg, new Locale("de")));
        } else {
            this.fxmlLoader.setResources(ResourceBundle.getBundle(bundlesPkg, new Locale("en")));
        }
    }

    /**
     * Adds icons for the application.
     *
     * @param stage the stage to which the icons should be added
     */
    private void initIcons(Stage stage) {
        stage.getIcons().addAll(
                new Image(getClass().getResourceAsStream("icon/icon48.png")),
                new Image(getClass().getResourceAsStream("icon/icon64.png"))
        );
    }

    /*==================================================*
     *==       HELPER METHOD FOR CHANGING FXMLs       ==*
     *==================================================*/

    /**
     * Loads the FXML with the given name from the resources and returns the created GUI.
     *
     * @param fxml the name of the FXML to load
     * @return the gui created from the FXML
     * @throws IOException throws an error if the FXML is not found etc.
     */
    private Parent loadFXML(String fxml) throws IOException {
        fxmlLoader.setLocation(App.class.getResource("fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

}
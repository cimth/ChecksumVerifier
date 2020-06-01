package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.example.model.Checksum;
import org.example.utils.ChecksumComputer;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ResourceBundle;

public class WindowController implements Initializable {

    /*==================================================*
     *==             INTERNATIONALIZATION             ==*
     *==================================================*/

    private ResourceBundle bundle;

    /*==================================================*
     *==                GUI-COMPONENTS                ==*
     *==================================================*/

    @FXML
    private TextArea outFile;

    @FXML
    private TextArea inTarget;

    @FXML
    private Button chooseFile;

    @FXML
    private ComboBox<Checksum> algorithms;

    @FXML
    private Label result;

    @FXML
    private Button verify;

    /*==================================================*
     *==               INITIALIZATION                 ==*
     *==================================================*/

    /**
     * Initializes the Controller as adding listeners and filling the ComboBox to select a checksum algorithm.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // save resource bundle for internationalization
        this.bundle = resourceBundle;

        // add algorithms to combobox to select one of those
        insertAlgorithms();

        // add event listening
        addCheckForValidInputListener(outFile);
        addCheckForValidInputListener(inTarget);

        addActionOnEnterListener(inTarget);
    }

    /**
     * Inserts all checksum algorithms available by the program into the corresponding ComboBox.
     * Sets SHA256 as default value since it is the most used when writing this app.
     */
    private void insertAlgorithms() {
        algorithms.getItems().setAll(Checksum.values());
        algorithms.setValue(Checksum.SHA256);
    }

    /**
     * Adds a listener to the given text input to enable or disable the compare button if the input is valid or
     * invalid.
     *
     * @param gui the text input gui
     */
    private void addCheckForValidInputListener(TextInputControl gui) {
        gui.textProperty().addListener((obs, oldText, newText) -> {

            // clear result as soon as making changes in the input
            result.setText("");

            // enable submit button when a valid file and a target checksum are given, else disable
            Path givenPath = Paths.get(outFile.textProperty().get());

            boolean fileExists = !outFile.textProperty().isEmpty().get() && Files.exists(givenPath);
            boolean checksumGiven = !inTarget.textProperty().get().isEmpty();

            verify.setDisable(!fileExists || !checksumGiven);
        });
    }

    /**
     * Activates clicking Enter on the given text input to compare the checksums if available.
     *
     * @param gui the text input gui
     */
    public void addActionOnEnterListener(TextInputControl gui) {
        gui.addEventFilter(KeyEvent.KEY_PRESSED, evt -> {
            if (evt.getCode() == KeyCode.ENTER) {

                // only compare if available
                if (!verify.isDisabled()) {
                    verifyChecksum(null);
                }

                // avoid further event handling
                evt.consume();
            }
        });
    }

    /*==================================================*
     *==                EVENT HANDLING                ==*
     *==================================================*/

    /**
     * Opens a file chooser to select the file for which the checksum should be compared.
     *
     * @param mouseEvent the event initiating the file choosing
     */
    @FXML
    public void chooseFile(ActionEvent mouseEvent) {

        // get window to block it as long as the file chooser is open
        Window window = ((Node)mouseEvent.getTarget()).getScene().getWindow();

        // choose file
        FileChooser fc = new FileChooser();
        File chosen = fc.showOpenDialog(window);

        // set file path in text field
        if (chosen != null && Files.exists(chosen.toPath())) {
            outFile.textProperty().setValue(chosen.getAbsolutePath());
        }
    }

    /**
     * Compare the actual checksum with the target checksum of the file given in the gui.
     *
     * Should only be used when both the target checksum and the file are given and valid.
     *
     * @param actionEvent the event initiating the comparison
     */
    @FXML
    public void verifyChecksum(ActionEvent actionEvent) {

        // prepare comparing
        ChecksumComputer comp = new ChecksumComputer(algorithms.getValue());

        File givenFile = new File(outFile.textProperty().get().trim());
        String targetChecksum = inTarget.textProperty().get().trim();

        // compare
        Optional<Boolean> checksumsIdentical = comp.verifyChecksum(givenFile, targetChecksum);

        // compare and adjust GUI to the result
        boolean correctChecksum = checksumsIdentical.isPresent() && checksumsIdentical.get();
        setResult(correctChecksum);
    }

    /**
     * Changes the label presenting the comparison's result according to the result given by the argument.
     *
     * @param correct true for identical checksums, else false
     */
    private void setResult(boolean correct) {
        if (correct) {
            result.setTextFill(Color.GREEN);
            result.setText(bundle.getString("pane.correctChecksum"));
        } else {
            result.setTextFill(Color.RED);
            result.setText(bundle.getString("pane.falseChecksum"));
        }
    }


}

package org.example.controller;

import javafx.collections.ObservableArray;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.example.model.Checksum;
import org.example.utils.ChecksumComputer;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class WindowController {

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
    private Button compare;

    /*==================================================*
     *==               INITIALIZATION                 ==*
     *==================================================*/

    public void initialize() {
        insertAlgorithms();

        addCheckForValidInputListener(outFile);
        addCheckForValidInputListener(inTarget);

        addActionOnEnterListener(compare);
    }

    private void insertAlgorithms() {
        algorithms.getItems().setAll(Checksum.values());
        algorithms.setValue(Checksum.SHA256);
    }

    private void addCheckForValidInputListener(TextInputControl gui) {
        gui.textProperty().addListener((obs, oldText, newText) -> {

            // clear result as soon as making changes in the input
            result.setText("");

            // enable submit button when a valid file and a target checksum are given, else disable
            Path givenPath = Paths.get(outFile.textProperty().get());

            boolean fileExists = !outFile.textProperty().isEmpty().get() && Files.exists(givenPath);
            boolean checksumGiven = !inTarget.textProperty().get().isEmpty();

            compare.setDisable(!fileExists || !checksumGiven);
        });
    }

    public void addActionOnEnterListener(Button btn) {
        btn.addEventFilter(KeyEvent.KEY_PRESSED, evt -> {
            if (evt.getCode() == KeyCode.ENTER) {
                compareChecksum(null);
                evt.consume();
            }
        });
    }

    /*==================================================*
     *==                EVENT HANDLING                ==*
     *==================================================*/

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

    @FXML
    public void compareChecksum(ActionEvent actionEvent) {

        // prepare comparing
        ChecksumComputer comp = new ChecksumComputer(algorithms.getValue());
        File givenFile = new File(outFile.textProperty().get());

        // get checksums to compare
        String toBe = inTarget.textProperty().get();
        Optional<String> toCheck = comp.getChecksum(givenFile);

        // compare and adjust GUI to the result
        boolean correctChecksum = toCheck.isPresent() && toBe.equals(toCheck.get());
        setResult(correctChecksum);
    }

    private void setResult(boolean correct) {
        if (correct) {
            result.setTextFill(Color.GREEN);
            result.setText("Correct Checksum!");
        } else {
            result.setTextFill(Color.RED);
            result.setText("False Checksum!");
        }
    }


}

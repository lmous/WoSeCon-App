package gr.ihu.cs.lmous.woseconapp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Lefteris Moussiades <lmous@teiemt.gr>
 */
public class SettingsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Button saveReset;
    @FXML
    TextField numberOfLines;
    @FXML
    private TextField numberOfColumns;
    @FXML
    private TextField maxWords;
    @FXML
    private ColorPicker fRandomChars;
    @FXML
    private ColorPicker fWordChars;
    @FXML
    private ColorPicker fTmpOpen;
    @FXML
    private ColorPicker fPerOpen;
    @FXML
    private ColorPicker bRandomChars;
    @FXML
    private ColorPicker bWordChars;
    @FXML
    private ColorPicker bTmpOpen;
    @FXML
    private ColorPicker bPerOpen;

    @FXML
    private void cancelSettings() {
        Stage stage = (Stage) saveReset.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void saveAndReset() throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Save and close App");
        alert.setHeaderText("Changes will apply next time you will run the word search puzzle");
        alert.setContentText("Press Save to save changes and close app");

        ButtonType buttonSave = new ButtonType("Save");
        ButtonType buttonCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonSave, buttonCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonSave) {
            Settings.boardLines = Integer.parseInt(numberOfLines.getText());
            Settings.boardClmns = Integer.parseInt(numberOfColumns.getText());
            Settings.maxWords = Integer.parseInt(maxWords.getText());
            Settings.FontRandomChar = fRandomChars.getValue().toString();
            Settings.FontWord = fWordChars.getValue().toString();
            Settings.FontPerOpen = fPerOpen.getValue().toString();
            Settings.BackGroundRandomChar = bRandomChars.getValue().toString();
            Settings.BackGroundWord = bWordChars.getValue().toString();
            Settings.BackGroundTempOpen = bTmpOpen.getValue().toString();
            Settings.BackGroundPerOpen = bPerOpen.getValue().toString();
            Settings.save();
            Stage stage = (Stage) saveReset.getScene().getWindow();
            stage.close();
            Platform.exit();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        numberOfLines.setText(String.valueOf(Settings.boardLines));
        numberOfColumns.setText(String.valueOf(Settings.boardClmns));
        maxWords.setText(String.valueOf(Settings.maxWords));
        fRandomChars.setValue(Color.web(Settings.FontRandomChar));
        fWordChars.setValue(Color.web(Settings.FontWord));
        fTmpOpen.setValue(Color.web(Settings.FontTempOpen));
        fPerOpen.setValue(Color.web(Settings.FontPerOpen));
        bRandomChars.setValue(Color.web(Settings.BackGroundRandomChar));
        bWordChars.setValue(Color.web(Settings.BackGroundWord));
        bTmpOpen.setValue(Color.web(Settings.BackGroundTempOpen));
        bPerOpen.setValue(Color.web(Settings.BackGroundPerOpen));

    }

}

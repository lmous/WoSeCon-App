package gr.ihu.cs.lmous.woseconapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 *
 * @author Lefteris Moussiades
 */
public class Settings {

    @FXML
    Button closeButton;

    public static char delimiter = '|'; //parameter
    public static int boardLines; // = 12;
    public static int boardClmns; // = 12;
    public static int maxWords; // = 10;

    public static String FontRandomChar; // ="WHITE"; 
    public static String BackGroundRandomChar; // ="BLUE";

    public static String FontWord; // ="BLACK";
    public static String BackGroundWord; // ="YELLOW";

    public static String FontTempOpen;// ="WHITE";
    public static String BackGroundTempOpen;// ="GREEN";

    public static String FontPerOpen;// ="WHITE";
    public static String BackGroundPerOpen;// ="RED";

    private static String getKey(String line) {
        return line.substring(0, line.indexOf("="));
    }

    private static String getValue(String line) {
        return line.substring(line.indexOf("=") + 1);
    }

    private static void updVars(String key, String value) {
        switch (key) {
            case "boardClmns": {
                Settings.boardClmns = Integer.parseInt(value);
                break;
            }

            case "boardLines": {
                Settings.boardLines = Integer.parseInt(value);
                break;

            }

            case "maxWords": {
                Settings.maxWords = Integer.parseInt(value);
                break;
            }
            case "FontRandomChar": {
                Settings.FontRandomChar = value;
                break;
            }
            case "BackGroundRandomChar": {
                Settings.BackGroundRandomChar = value;
                break;
            }
            case "FontWord": {
                Settings.FontWord = value;
                break;
            }
            case "BackGroundWord": {
                Settings.BackGroundWord = value;
                break;
            }
            case "FontTempOpen": {
                Settings.FontTempOpen = value;
                break;
            }
            case "BackGroundTempOpen": {
                Settings.BackGroundTempOpen = value;
                break;
            }
            case "FontPerOpen": {
                Settings.FontPerOpen = value;
                break;
            }
            case "BackGroundPerOpen": {
                Settings.BackGroundPerOpen = value;
                break;
            }
        }
    }

    public void load() throws FileNotFoundException, IOException {
        
        BufferedReader input = new BufferedReader(new FileReader("settings.txt"));
        String line;
        while ((line = input.readLine()) != null) {
            String key = getKey(line);
            String value = getValue(line);
            updVars(key, value);
            //String value=getValue(line);
        }
    }

    public static void save() throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter("settings.txt"));
        out.println("boardLines=" + boardLines);
        out.println("boardClmns=" + boardClmns);
        out.println("maxWords=" + maxWords);
        out.println("FontRandomChar=" + FontRandomChar);
        out.println("BackGroundRandomChar=" + BackGroundRandomChar);
        out.println("FontWord=" + FontWord);
        out.println("BackGroundWord=" + BackGroundWord);
        out.println("FontTempOpen=" + FontTempOpen);
        out.println("BackGroundTempOpen=" + BackGroundTempOpen);
        out.println("FontPerOpen=" + FontPerOpen);
        out.println("BackGroundPerOpen=" + BackGroundPerOpen);
        out.close();
    }

    @FXML
    private void closeButtonAction() {
        // get a handle to the stage
        Stage stage = (Stage) closeButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    void editSettings() {

        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("gr/ihu/cs/lmous/woseconapp/Settings2.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Settings");
            stage.setScene(new Scene(root));

            stage.show();
            // Hide this current window (if this is what you want)
            //((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

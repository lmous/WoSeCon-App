package gr.ihu.cs.lmous.woseconapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Lefteris Moussiades
 */
public class Loader {

    public static Map<String, VocabularyEntry> loadVocabulary(Stage stage) throws FileNotFoundException, IOException {
        FileChooser fC = new FileChooser();
        fC.setInitialDirectory(new File(System.getProperty("user.dir")));
        fC.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt") );
        File f = fC.showOpenDialog(stage);
        if (f==null) return null;
        Map<String, VocabularyEntry> rVal = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        BufferedReader inputStream = new BufferedReader(new FileReader(f));
        String line;
        while ((line = inputStream.readLine()) != null) {
            int delPos = line.indexOf(Settings.delimiter);
            if (delPos < 0) {
                throw new RuntimeException();
            }
            String eng = line.substring(0, delPos);
            String gr = line.substring(delPos + 1);
            rVal.put(eng.toUpperCase(), new VocabularyEntry(gr));
        }
        return rVal;
    }

   

}

package gr.ihu.cs.lmous.woseconapp;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javafx.scene.control.Alert;

/**
 *
 * @author Lefteris Moussiades
 */
public class Locator {

    final static int FORWARD = 1;
    final static int BACKWARD = 2;
    static RandomLocator globalLocator;
    static KLabel[][] labels = MyKryptolexonv3.labels;
    static int operationMode = FORWARD;
    static char[][] board = MyKryptolexonv3.board;

    private static void initBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = '\0';
            }
        }
    }

    public static char randomChar() {
        int noOfLetters = 26;
        int offSet = new Random().nextInt(noOfLetters);
        return (char) ('A' + (char) offSet);
    }

    private static void updateBoard(ArrayList<WordInfo> words) {
        initBoard();
        Iterator<WordInfo> iterator = words.iterator();
        while (iterator.hasNext()) {
            WordInfo word = iterator.next();
            Location[] wLocations = word.getAllLocations();
            for (Location l : wLocations) {
                board[l.getLine()][l.getClmn()] = word.charAt(l);
                labels[l.getLine()][l.getClmn()].getLabel().setStyle("-fx-background-color:\""+Settings.BackGroundWord+"\"; -fx-text-fill:\""+ Settings.FontWord+"\";");
            }
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == '\0') {
                    board[i][j] = Locator.randomChar();
                }
            }
        }
    }

    private static boolean validPlacement(ArrayList<WordInfo> listOfWords, WordInfo currentWord, DirectedLocation dL) {
        currentWord.setFirstLetterLoc(dL);
        Location[] wordLocations = currentWord.getAllLocations();
        for (Location l : wordLocations) {
            if (l.getLine() > Settings.boardLines - 1 || l.getClmn() > Settings.boardClmns - 1) {
                currentWord.setFirstLetterLoc(null);
                return false;
            }
        }

        for (int i = 0; i < listOfWords.size(); i++) {
            WordInfo wToCheck = listOfWords.get(i);
            if (currentWord.equals(wToCheck)) break;
            if (currentWord.conflictedLocation(wToCheck)) {
                currentWord.setFirstLetterLoc(null);
                return false;
            }
        }

        return true;
    }

    public static boolean locateOne(ArrayList<WordInfo> listOfWords, WordInfo currentWord) {
             
        RandomLocator localLocator;
        if (operationMode==BACKWARD) {
            DirectedLocation dL = currentWord.getFirstLetterLoc();
            globalLocator.add(dL);
            currentWord.moveLocationToTested();
            localLocator = globalLocator.minus(currentWord.getTested());
            //System.out.println(wordToLocateIdx/* + " " + wordToLocate + " " + localLocator.dLocations*/);
        } else {
            localLocator = globalLocator;
            //System.out.println(wordToLocateIdx /* + " " + wordToLocate + " " + localLocator.dLocations*/);
        }

        int locationIdx = 0;
        while (locationIdx < localLocator.size()) {
            DirectedLocation newLocation = localLocator.get(locationIdx);
            if (validPlacement(listOfWords, currentWord, newLocation)) {
                globalLocator.remove(newLocation);
                return true;
            }
            locationIdx++;
        }

        return false;
    }

    public static void fail() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText("Το κρυπτόλεξο δεν μπορεί να φορτωθεί\n"
                + "Δοκιμάστε πάλι ή μειώστε τις λέξεις \n"
                + "ή αυξήστε τις γραμμές ή/και στήλες του");
        alert.showAndWait();
        System.exit(0);

    }

    public static int construct(ArrayList<WordInfo> listOfWords) {
        int cWIdx = 0;
        WordInfo currentWord = listOfWords.get(cWIdx);
        LocalTime start = LocalTime.now();

        while (true) {

            if (locateOne(listOfWords, currentWord)) {
                if (cWIdx==listOfWords.size()-1) break;
                currentWord = listOfWords.get(++cWIdx);
                operationMode = FORWARD;
            } else {
                if (cWIdx == 0) fail();
                currentWord.deleteTested();
                currentWord=listOfWords.get(--cWIdx);
                operationMode=BACKWARD;
            }
        }
        LocalTime end = LocalTime.now();
        Duration timeNeeded = Duration.between(end, start);
        //System.out.println("time: " + timeNeeded);
        updateBoard(listOfWords);
        return cWIdx+1;
    }
}

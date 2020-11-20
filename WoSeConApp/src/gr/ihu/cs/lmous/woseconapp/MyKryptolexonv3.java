/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ihu.cs.lmous.woseconapp;

import java.awt.Toolkit;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Lefteris Moussiades <lmous@teiemt.gr>
 */
public class MyKryptolexonv3 extends Application {

    static KLabel[][] labels;
    GridPane gridPane;
    Map<String, VocabularyEntry> vocabulary;
    int loadFromWordIdx;
    static char[][] board;
    int level;
    ArrayList<StudentEvaluation> studentEvaluationArray;
    Checker checker;
    //TextField explanationArea;
    TextArea explanationArea;
    
    TextArea messageArea;
    Button previousLevelButton, nextLevelButton;
    ArrayList<String> keys;
    ArrayList<WordInfo> listOfWords;
    private static final DecimalFormat DF = new DecimalFormat("#.##");
    Settings settings = new Settings();

    static MyKryptolexonv3 mainApp;
    

    void initApp() {
        try {
            settings.load();
        } catch (IOException ex) {
            Logger.getLogger(MyKryptolexonv3.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        labels = new KLabel[Settings.boardLines][Settings.boardClmns];
        board = new char[Settings.boardLines][Settings.boardClmns];
    }

    Location findLabel(Label l) {
        for (int i = 0; i < Settings.boardLines; i++) {
            for (int j = 0; j < Settings.boardClmns; j++) {
                if (labels[i][j].getLabel() == l) {
                    return new Location(i, j);
                }
            }
        }
        return null;
    }

    private GridPane getGridPane() {
        GridPane gridPane = new GridPane();
        for (int i = 0; i < Settings.boardClmns; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / Settings.boardClmns);
            gridPane.getColumnConstraints().add(colConst);
        }

        for (int i = 0; i < Settings.boardLines; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / Settings.boardLines);
            gridPane.getRowConstraints().add(rowConst);
        }

        //Font font = new Font(14);
        Insets gPMargin = new Insets(0);
        for (int i = 0; i < Settings.boardLines; i++) {
            for (int j = 0; j < Settings.boardClmns; j++) {
                labels[i][j] = new KLabel();
                //labels[i][j].getLabel().setMaxHeight(Double.MAX_VALUE);
                //labels[i][j].getLabel().setMaxWidth(Double.MAX_VALUE);
                //labels[i][j].getLabel().setAlignment(Pos.CENTER);
                //labels[i][j].getLabel().setFont(font);
                labels[i][j].getLabel().setId("letterLabel");

                labels[i][j].getLabel().setOnMouseClicked(labelClickHandler);
            }
        }
        for (int i = 0; i < Settings.boardLines; i++) {
            for (int j = 0; j < Settings.boardClmns; j++) {
                gridPane.add(labels[i][j].getLabel(), j, i);
                GridPane.setMargin(labels[i][j].getLabel(), gPMargin);
            }
        }

        gridPane.setGridLinesVisible(true);

        gridPane.setId("gridPane");
        return gridPane;
    }

    private void UpdateTranslation(String[] words) {
        String explanation = "";
        if (words[0] != null) {
            explanation += words[0] + ": " + vocabulary.get(words[0]).getTranslation();
            if (words[1] != null) {
                explanation += "\n";
            }
        }
        if (words[1] != null) {
            explanation += words[1] + ": " + vocabulary.get(words[1]).getTranslation();
        }
        explanationArea.setText(explanation);
    }

    private ArrayList<WordInfo> getWordsToLoad() {
        if (keys == null) {
            keys = new ArrayList<>(vocabulary.keySet());
            Collections.shuffle(keys);
        }
        ArrayList<WordInfo> words = new ArrayList<>();
        for (int i = loadFromWordIdx;
                (i < loadFromWordIdx + Settings.maxWords) && (i < vocabulary.size()); i++) {
            words.add(new WordInfo(keys.get(i)));
        }
        //Collections.shuffle(words);
        return words;
    }

    private String getMessage(StudentEvaluation studentEvaluation, boolean totalRslts) {
        String message = (totalRslts ? "Total Performance" : ("Load " + level));
        if (studentEvaluation.repeated) {
            message += " (Repeated)";
        }
        message = message + "\nHidden Words : " + studentEvaluation.wordsLocated
                + "\nRevealed Words  : " + studentEvaluation.wordsFound;
        message += "\nLetters Clicked : " + studentEvaluation.labelsClicked;
        double score = ((studentEvaluation.wordsLocated) / (studentEvaluation.labelsClicked / 2.)) * 100;
        message += "\nScore : " + DF.format(score) + "%";
        return message;
    }

    EventHandler<? super MouseEvent> labelClickHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
            Label l = (Label) event.getSource();
            Location lPos = findLabel(l);
            if (labels[lPos.getLine()][lPos.getClmn()].ispOpen() || studentEvaluationArray == null) {
                return;
            }

            StudentEvaluation studentEvaluation = studentEvaluationArray.get(studentEvaluationArray.size() - 1);

            studentEvaluation.labelsClicked++;
            String[] words = checker.checkForWords(lPos);
            if (words[0] != null || words[1] != null) {
                if (words[0] != null) {
                    studentEvaluation.wordsFound++;
                }
                if (words[1] != null) {
                    studentEvaluation.wordsFound++;
                }
                UpdateTranslation(words);
                if (studentEvaluation.wordsFound == studentEvaluation.wordsLocated) {
                    String message = getMessage(studentEvaluation, false);
                    messageArea.setText(messageArea.getText() + "\n\n" + message);

                    StudentEvaluation totalEvaluation = StudentEvaluation.checkEnd(studentEvaluationArray, vocabulary.size());
                    if (totalEvaluation != null) {
                        message = getMessage(totalEvaluation, true);
                        messageArea.setText(messageArea.getText() + "\n\n" + message);
                        Toolkit.getDefaultToolkit().beep();
                    } else {
                        nextLevelButton.setDisable(false);
                        previousLevelButton.setDisable(false);
                    }
                    //wordFound wordLocated ?
                }
            } else if (!checker.isOpen(lPos)) {
                //??
                // l.setStyle("-fx-background-color:" + Color.rgb(32, 24, 35) + "; -fx-text-fill:" + Color.rgb(32, 24, 35) + ";");
                 
                l.setStyle("-fx-background-color:\"" + Settings.BackGroundTempOpen + "\"; -fx-text-fill:\"" + Settings.FontTempOpen + "\";");
                labels[lPos.getLine()][lPos.getClmn()].settOpen(true);
            } else {
                if (WordInfo.isWordLetter(listOfWords, lPos)) {
                    l.setStyle("-fx-background-color:\"" + Settings.BackGroundWord + "\"; -fx-text-fill:\"" + Settings.FontWord + "\";");
                } else {
                    l.setStyle("-fx-background-color:\"" + Settings.BackGroundRandomChar + "\"; -fx-text-fill:\"" + Settings.FontRandomChar + "\";"); //??
                }
                labels[lPos.getLine()][lPos.getClmn()].settOpen(false);
                //?
            }
        }
    };

    EventHandler<ActionEvent> nextLevelButtonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            StudentEvaluation studentEvaluation = new StudentEvaluation();
            studentEvaluationArray.add(studentEvaluation);
            for (int i = 0; i < Settings.boardLines; i++) {
                for (int j = 0; j < Settings.boardClmns; j++) {
                    labels[i][j].getLabel().setStyle("-fx-background-color:\"" + Settings.BackGroundRandomChar + "\"; -fx-text-fill:\"" +  Settings.FontRandomChar + "\";");
                    labels[i][j].setpOpen(false);
                    labels[i][j].settOpen(false);
                }
            }
            listOfWords = getWordsToLoad();
            studentEvaluation.wordsLocated = Locator.construct(listOfWords); //?

            loadFromWordIdx += studentEvaluation.wordsLocated;
            nextLevelButton.setDisable(true);
            previousLevelButton.setDisable(true);
            level++;
            studentEvaluation.level = level;
            for (int i = 0; i < Settings.boardLines; i++) {
                for (int j = 0; j < Settings.boardClmns; j++) {
                    labels[i][j].getLabel().setText(Character.toString(board[i][j]));
                }
            }
        }
    };

    EventHandler<ActionEvent> previousLevelButtonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            StudentEvaluation lastEvaluation = studentEvaluationArray.get(studentEvaluationArray.size() - 1);
            loadFromWordIdx -= lastEvaluation.wordsLocated;
            level--;
            StudentEvaluation studentEvaluation = new StudentEvaluation();
            studentEvaluationArray.add(studentEvaluation);
            studentEvaluation.level = level;
            studentEvaluation.repeated = true;
            for (int i = 0; i < Settings.boardLines; i++) {
                for (int j = 0; j < Settings.boardClmns; j++) {
                    labels[i][j].getLabel().setStyle("-fx-background-color:\"" + Settings.BackGroundRandomChar + "\"; -fx-text-fill:\"" + Settings.FontRandomChar + "\";");
                    labels[i][j].setpOpen(false);
                    labels[i][j].settOpen(false);
                }
            }
            listOfWords = getWordsToLoad();
            studentEvaluation.wordsLocated = Locator.construct(listOfWords); //?
            //if (studentEvaluation.wordsLocated>0) {
            loadFromWordIdx += studentEvaluation.wordsLocated;
            nextLevelButton.setDisable(true);
            previousLevelButton.setDisable(true);

            level++;
            for (int i = 0; i < Settings.boardLines; i++) {
                for (int j = 0; j < Settings.boardClmns; j++) {
                    labels[i][j].getLabel().setText(Character.toString(board[i][j]));
                }
            }
        }
    };

   
    private void initGame() {
        //System.out.println("CLEAR");
        for (int i = 0; i < Settings.boardLines; i++) {
            for (int j = 0; j < Settings.boardClmns; j++) {
                labels[i][j].getLabel().setStyle("-fx-background-color:\"" + Settings.BackGroundRandomChar + "\"; -fx-text-fill:\"" + Settings.FontRandomChar + "\";"); //?
                //labels[i][j].getLabel().setStyle("-fx-background-color: red;");
                labels[i][j].setpOpen(false);
                labels[i][j].settOpen(false);
                board[i][j] = '\0';
                labels[i][j].getLabel().setText(Character.toString('\0'));
            }
        }
        messageArea.clear();
        explanationArea.clear();
        loadFromWordIdx = 0;
        studentEvaluationArray = new ArrayList<>();
        nextLevelButton.setDisable(true);
        previousLevelButton.setDisable(true);
        keys = null;
        Locator.globalLocator = new RandomLocator();
    }

    private void loadVocabularyAction(ActionEvent event) {
        try {
            vocabulary = Loader.loadVocabulary((Stage) gridPane.getScene().getWindow());
        } catch (IOException ex) {
            Logger.getLogger(MyKryptolexonv3.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (vocabulary == null) {
            return;
        }
        initGame();

        StudentEvaluation studentEvaluation = new StudentEvaluation();
        studentEvaluationArray.add(studentEvaluation);

        listOfWords = getWordsToLoad();
        studentEvaluation.wordsLocated = Locator.construct(listOfWords); //?
        loadFromWordIdx += studentEvaluation.wordsLocated;

        checker = new Checker(labels, vocabulary);
        level = 1;
        for (int i = 0; i < Settings.boardLines; i++) {
            for (int j = 0; j < Settings.boardClmns; j++) {
                labels[i][j].getLabel().setText(Character.toString(board[i][j]));
            }
        }
        messageArea.clear();
    }

    @Override
    public void start(Stage primaryStage) {
        mainApp=this;
        initApp();

        MenuItem loadVocMenu = new MenuItem("Load Vocabulary");
        loadVocMenu.setOnAction(e -> loadVocabularyAction(e));
        MenuItem clearBoardMenu = new MenuItem("Clear");
        clearBoardMenu.setOnAction(e -> initGame());
        MenuItem settingsMenu = new MenuItem("Settings");
        settingsMenu.setOnAction(e -> settings.editSettings());
        MenuItem exitMenu = new MenuItem("Exit");
        exitMenu.setOnAction(e->Platform.exit());
        Menu fileMenu = new Menu("File");
        fileMenu.getItems().addAll(loadVocMenu, clearBoardMenu, settingsMenu, exitMenu);

        MenuItem aboutMenu = new MenuItem("About");
        Menu helpMenu = new Menu("Help");
        helpMenu.getItems().addAll(aboutMenu);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, helpMenu);

        messageArea = new TextArea();
        messageArea.setId("messageArea");
        messageArea.setEditable(false);

        Image previousLevelArrow = new Image(getClass().getResourceAsStream("leftArrow.png"));
        previousLevelButton = new Button();
        ImageView tmp = new ImageView(previousLevelArrow);
        tmp.setFitHeight(30);
        tmp.setFitWidth(30);
        previousLevelButton.setGraphic(tmp);

        Image nextLevelArrow = new Image(getClass().getResourceAsStream("rightArrow.png"));
        nextLevelButton = new Button();
        tmp = new ImageView(nextLevelArrow);
        tmp.setFitHeight(30);
        tmp.setFitWidth(30);
        nextLevelButton.setGraphic(tmp);
        nextLevelButton.setOnAction(nextLevelButtonHandler);

        nextLevelButton.setDisable(true);
        previousLevelButton.setDisable(true);
        previousLevelButton.setOnAction(previousLevelButtonHandler);

        VBox rightBottomVBox = new VBox();

        rightBottomVBox.getChildren().addAll(nextLevelButton, previousLevelButton);

        explanationArea = new TextArea();
        explanationArea.setId("explanationArea");
        explanationArea.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        explanationArea.setWrapText(true);

        HBox bottomHBox = new HBox();
        HBox.setHgrow(explanationArea, Priority.ALWAYS);

        bottomHBox.getChildren().addAll(explanationArea, rightBottomVBox);

        gridPane = getGridPane();
        gridPane.setId("gridPane");

        BorderPane root = new BorderPane(gridPane, menuBar, messageArea, bottomHBox, null);


       
        Scene scene = new Scene(root);//, 600, 600);

        primaryStage.setTitle("Word Search Puzzle!");
        primaryStage.setScene(scene);
        scene.getStylesheets().add(MyKryptolexonv3.class.getResource("Kryptolexo.css").toExternalForm());
        primaryStage.show();
        initGame();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

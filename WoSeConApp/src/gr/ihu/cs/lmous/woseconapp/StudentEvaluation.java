package gr.ihu.cs.lmous.woseconapp;

import java.util.ArrayList;

/**
 *
 * @author Lefteris Moussiades
 */
public class StudentEvaluation {

    boolean repeated;
    int level;
    int wordsFound;
    int wordsLocated;
    int labelsClicked;
    double score;

    public static StudentEvaluation checkEnd(ArrayList<StudentEvaluation> evaluations, int allWordsCnt) {

        StudentEvaluation rslts = new StudentEvaluation();
        for (StudentEvaluation sE : evaluations) {
            if (!sE.repeated) {
                rslts.wordsLocated += sE.wordsLocated;
                rslts.wordsFound += sE.wordsFound;
            }
            rslts.labelsClicked += sE.labelsClicked;
            if (rslts.wordsLocated == allWordsCnt) {
                return rslts;
            }
        }
        return null;
    }

}

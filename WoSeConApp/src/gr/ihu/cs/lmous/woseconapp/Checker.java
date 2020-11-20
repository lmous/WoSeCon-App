package gr.ihu.cs.lmous.woseconapp;

import java.util.Map;

/**
 *
 * @author Lefteris Moussiades
 */
public class Checker {

    KLabel[][] labels;
    Map<String, VocabularyEntry> vocabulary;

    public Checker(KLabel[][] labels, Map<String, VocabularyEntry> vocabulary) {
        this.labels = labels;
        this.vocabulary = vocabulary;
    }

    boolean isOpen(Location labelPos) {
        return labels[labelPos.getLine()][labelPos.getClmn()].ispOpen() || labels[labelPos.getLine()][labelPos.getClmn()].istOpen();
    }

    Location findOpenH(Location cPos, int from) {
        for (int i = from; i < Settings.boardClmns; i++) {
            if (i != cPos.getClmn() && isOpen(new Location(cPos.getLine(), i))) {
                return new Location(cPos.getLine(), i);
            }
        }
        return null;
    }

    String tryGetHWord(Location l1, Location l2) {
        String word = "";
        Location sLabel = l1.getClmn() < l2.getClmn() ? l1 : l2;
        Location eLabel = l1 == sLabel ? l2 : l1;
        for (int i = sLabel.getClmn(); i <= eLabel.getClmn(); i++) {
            word += labels[sLabel.getLine()][i].getLabel().getText();
        }
        if (vocabulary.containsKey(word)) {
            for (int i = sLabel.getClmn(); i <= eLabel.getClmn(); i++) {
                labels[sLabel.getLine()][i].settOpen(false);
                labels[sLabel.getLine()][i].setpOpen(true);
                labels[sLabel.getLine()][i].getLabel().setStyle("-fx-background-color:\"" + Settings.BackGroundPerOpen + "\"; -fx-text-fill:\"" + Settings.FontPerOpen+"\"");
            }
            return word;
        }
        return null;
    }

    Location findOpenV(Location cPos, int from) {
        for (int i = from; i < Settings.boardLines; i++) {
            if (i != cPos.getLine() && isOpen(new Location(i, cPos.getClmn()))) {
                return new Location(i, cPos.getClmn());
            }
        }
        return null;
    }

    String tryGetVWord(Location l1, Location l2) {
        String word = "";
        Location sLabel = l1.getLine() < l2.getLine() ? l1 : l2;
        Location eLabel = l1 == sLabel ? l2 : l1;
        for (int i = sLabel.getLine(); i <= eLabel.getLine(); i++) {
            word += labels[i][sLabel.getClmn()].getLabel().getText();
        }
        if (vocabulary.containsKey(word)) {
            for (int i = sLabel.getLine(); i <= eLabel.getLine(); i++) {
                labels[i][sLabel.getClmn()].settOpen(false);
                labels[i][sLabel.getClmn()].setpOpen(true);
                labels[i][sLabel.getClmn()].getLabel().setStyle("-fx-background-color:\"" + Settings.BackGroundPerOpen + "\"; -fx-text-fill:\"" + Settings.FontPerOpen+"\"");
            }
            return word;
        }
        return null;
    }

    String[] checkForWords(Location labelPos) {
        String[] rVal = new String[2];
        int from = 0;
        while (from < Settings.boardClmns-1) {
            Location hLabel = findOpenH(labelPos, from);

            if (hLabel != null) {
                rVal[0] = tryGetHWord(labelPos, hLabel);
                if (rVal[0] != null) {
                    break;
                }
                from++;
            } else {
                break;
            }
        }
        from = 0;
        while (from < Settings.boardLines-1) {
            Location vLabel = findOpenV(labelPos, from);
            if (vLabel != null) {
                rVal[1] = tryGetVWord(labelPos, vLabel);
                if (rVal[1] != null) {
                    break;
                }
                from++;
            } else {
                break;
            }
        }

        return rVal;
    }
}

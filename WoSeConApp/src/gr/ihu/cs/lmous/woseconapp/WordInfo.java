package gr.ihu.cs.lmous.woseconapp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;


/**
 *
 * @author Lefteris Moussiades
 */
public class WordInfo {

    private String word;
    private DirectedLocation firstLetterLoc;
    private ArrayList<DirectedLocation> testedLocations;

    public WordInfo(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public DirectedLocation getFirstLetterLoc() {
        return firstLetterLoc;
    }

    public void setFirstLetterLoc(DirectedLocation firstLetterLoc) {
        this.firstLetterLoc = firstLetterLoc;
    }
    
    public void deleteTested() {
        testedLocations=null;
    }
    
    public ArrayList<DirectedLocation> getTested() {
        return testedLocations;
    }
    
    public void moveLocationToTested() {
        if (testedLocations==null)
            testedLocations=new ArrayList<>();
        testedLocations.add(firstLetterLoc);
        firstLetterLoc=null;
    }
    
    Location[] getAllLocations() {
        if (firstLetterLoc==null) return null;
        Location[] rVal = new Location[word.length()];
        int line = firstLetterLoc.getLine();
        int clmn = firstLetterLoc.getClmn();
        if (firstLetterLoc.getDirection() == Direction.HORIZONTAL) {
            for (int i = 0; i < rVal.length; i++) {
                rVal[i] = new Location(line, clmn + i);
            }
        } else {
            for (int i = 0; i < rVal.length; i++) {
                rVal[i] = new Location(line + i, clmn);
            }
        }
        return rVal;
    }

    public char charAt(Location loc) {
        if (this.firstLetterLoc.getDirection() == Direction.HORIZONTAL) {
            return word.charAt(loc.getClmn() - firstLetterLoc.getClmn());
        } else {
            return word.charAt(loc.getLine() - firstLetterLoc.getLine());
        }
    }

    public boolean conflictedLocation(WordInfo w2) {
        Location[] w1Ls = getAllLocations();
        Location[] w2Ls = w2.getAllLocations();
//        if (w2Ls==null) 
//            System.out.println("conflictedLocation "+w2);
        for (Location l1 : w1Ls) {
            for (Location l2 : w2Ls) {
                if (l1.equals(l2)) {
                    if (firstLetterLoc.getDirection() == w2.firstLetterLoc.getDirection()) {
                        return true;
                    }
                    if (charAt(l2)!=w2.charAt(l2)) return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return word;
    }
    
    public boolean isLocated() {
        return firstLetterLoc != null;
    }

    public DirectedLocation dealloc() {
        DirectedLocation dL=new DirectedLocation(firstLetterLoc);
        firstLetterLoc=null;
        return dL;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.word);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WordInfo other = (WordInfo) obj;
        if (!Objects.equals(this.word, other.word)) {
            return false;
        }
        return true;
    }
    
    public static boolean isWordLetter(ArrayList<WordInfo> listOfWords, Location loc) {
        Iterator<WordInfo> it=listOfWords.iterator();
        while (it.hasNext()) {
            WordInfo wi=it.next();
            Location[] locs=wi.getAllLocations();
            for (Location l:locs)
                if (l.equals(loc)) return true; 
        }
        return false;
    }
    
}

package gr.ihu.cs.lmous.woseconapp;

/**
 *
 * @author Lefteris Moussiades
 */

public class Location {
    private int line;
    private int clmn;

    public Location(int line, int clmn) {
        this.line = line;
        this.clmn = clmn;
    }

    public int getClmn() {
        return clmn;
    }

    public void setClmn(int clmn) {
        this.clmn = clmn;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.line;
        hash = 97 * hash + this.clmn;
        return hash;
    }
    
    @Override
    public boolean equals (Object l) {
        Location loc=(Location)l;
        return (line==loc.line && clmn==loc.clmn);
    }
    
    public String toString() {
        return "("+this.line+", "+this.clmn+")";
    }
    
}

package gr.ihu.cs.lmous.woseconapp;

import java.util.Objects;

/**
 *
 * @author Lefteris Moussiades
 */

public class DirectedLocation {
    private int line;
    private int clmn;
    private Direction direction;

    public DirectedLocation(int line, int clmn, Direction direction) {
        this.line=line;
        this.clmn=clmn;
        this.direction = direction;
    }
    
    public DirectedLocation(DirectedLocation dL) {
        this.line=dL.line;
        this.clmn=dL.clmn;
        this.direction = dL.direction;
    }
    
    @Override
    public String toString() {
        return "("+line+","+clmn+","+direction+")";
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getClmn() {
        return clmn;
    }

    public void setClmn(int clmn) {
        this.clmn = clmn;
    }

    public Direction getDirection() {
        return direction;
    }

  
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.line;
        hash = 17 * hash + this.clmn;
        hash = 17 * hash + Objects.hashCode(this.direction);
        return hash;
    }

    public boolean equals(Object o) {
        DirectedLocation dL= (DirectedLocation)o;
        return (line==dL.line) && (clmn==dL.clmn) && (direction==dL.direction);
    }
    
}

package gr.ihu.cs.lmous.woseconapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author Lefteris Moussiades
 */
public class RandomLocator {

    ArrayList<DirectedLocation> dLocations = new ArrayList<>();
    Random rGen = new Random();

    public RandomLocator() {
        Direction[] directionValues = Direction.values();
        for (int i = 0; i < Settings.boardLines; i++) {
            for (int j = 0; j < Settings.boardClmns; j++) {
                  for (Direction d : directionValues) {
                    if (!((d == Direction.HORIZONTAL && i == Settings.boardClmns - 1)
                            || (d == Direction.VERTICAL && j == Settings.boardLines - 1))) {
                        dLocations.add(new DirectedLocation(i, j, d));
                    }
                }
            }
        }
        Collections.shuffle(dLocations);
    }
//    private int rDirection(int previous) {
//        if (previous == -1) {
//            double r = Math.random();
//            if (r < 5) {
//                return 0;
//            }
//            return 1;
//        } else if (previous == 0) {
//            return 1;
//        }
//        return 0;
//    }
//
//    public RandomLocator() {
//        Direction[] directionValues = Direction.values();
//        for (int i = 0; i < Settings.boardLines; i++) {
//            for (int j = 0; j < Settings.boardClmns; j++) {
//                int previous = -1;
//                for (int k = 0; k < 2; k++) {
//                    previous = rDirection(previous);
//                    Direction d = Direction.values()[previous];
//                    if (!((d == Direction.HORIZONTAL && i == Settings.boardClmns - 1)
//                            || (d == Direction.VERTICAL && j == Settings.boardLines - 1))) {
//                        dLocations.add(new DirectedLocation(i, j, d));
//                    }
//                }
//            }
//        }
//        Collections.shuffle(dLocations);
//    }

    public DirectedLocation get(int idx) {
        return dLocations.get(idx);
    }

    public void add(DirectedLocation dL) {
        int pos = rGen.nextInt(dLocations.size());
        dLocations.add(pos, dL);
    }

    /*public void add(ArrayList<DirectedLocation> dLs) {
        for (int i = 0; i < dLs.size(); i++) {
            add(dLs.remove(i));
        }
    }

    public DirectedLocation remove(int idx) {
        return dLocations.remove(idx);
    }
     */
    public boolean remove(DirectedLocation loc) {
        return dLocations.remove(loc);
    }

    public int size() {
        return dLocations.size();
    }

    public RandomLocator minus(ArrayList<DirectedLocation> visited) {
        RandomLocator rVal = new RandomLocator();
        for (DirectedLocation dL : visited) {
            rVal.dLocations.remove(dL);
        }
        return rVal;
    }

//    public static void main(String[] args) {
//        Settings.boardLines=10;
//        Settings.boardClmns=10;
//        RandomLocator rLoc = new RandomLocator();
//        for (DirectedLocation dL : rLoc.dLocations) {
//            System.out.println(dL);
//        }
//
//    }

}

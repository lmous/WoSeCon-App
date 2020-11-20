/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ihu.cs.lmous.woseconapp;

import javafx.scene.control.Label;

/**
 *
 * @author Lefteris
 */
public class KLabel {
    private Label label;
    private boolean tOpen;
    private boolean pOpen;

    public KLabel() {
        this.label = new Label();
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public boolean istOpen() {
        return tOpen;
    }

    public void settOpen(boolean tOpen) {
        this.tOpen = tOpen;
    }

    public boolean ispOpen() {
        return pOpen;
    }

    public void setpOpen(boolean pOpen) {
        this.pOpen = pOpen;
    }
    
    
    
}

package Processor;

import java.awt.*;

public enum Selection {
    MATCH(new Color(160,200,255),Color.CYAN), PARETHESIS(Color.RED,Color.RED);

    Color basic;
    Color selection;
    Selection(Color basic, Color selection){
        this.basic = basic;
        this.selection = selection;
    }

    public Color getBasicColor() {
        return basic;
    }

    public Color getSelectionColor() {
        return selection;
    }
}

package com.ma.Outputter;

import java.awt.*;

/**
 * Created by Marco on 09.05.2016.
 */
public class LabelColorPair {
    private String label;
    private Color color;

    public LabelColorPair(String label, Color color) {
        this.label = label;
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public Color getColor() {
        return color;
    }
}

package com.ma.Outputter;

import org.ejml.simple.SimpleMatrix;

import java.io.File;

/**
 * Created by Marco on 18.01.2016.
 */
public abstract class Outputter {
    private String prePath = "";

    public abstract void save(String path, SimpleMatrix reputations, String[] mapping, String[] labels, String time);

    public void initPrePath() {
        new File(prePath).mkdirs();
    }

    public String getPrePath() {
        return prePath;
    }

    public void setPrePath(String prePath) {
        this.prePath = prePath;
    }
}

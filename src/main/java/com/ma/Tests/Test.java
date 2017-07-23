package com.ma.Tests;

/**
 * Created by Marco on 27.06.2016.
 */
public abstract class Test {
    final String prePath = "results/";
    String name;

    public abstract void run();

    public String getPrePath() {
        return prePath + getName() + "/";
    }

    public String getName() {
        return name;
    }
}

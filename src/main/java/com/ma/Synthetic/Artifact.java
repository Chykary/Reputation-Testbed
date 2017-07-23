package com.ma.Synthetic;

import java.util.HashSet;

/**
 * Created by Marco on 23.03.2016.
 */
public class Artifact {
    private int from;
    private boolean good;
    private HashSet<Integer> votees = new HashSet<>();

    public Artifact(int from, boolean good) {
        this.from = from;
        this.good = good;
    }

    public void vote(int idx) {
        votees.add(idx);
    }

    public boolean hasVoted(int idx) {
        if (votees.contains(idx)) {
            return true;
        }
        return false;
    }

    public int getFrom() {
        return from;
    }

    public boolean isGood() {
        return good;
    }
}

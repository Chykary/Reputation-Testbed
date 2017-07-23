package com.ma.Misc;

import org.ejml.simple.SimpleMatrix;

/**
 * Created by Marco on 18.01.2016.
 */
public class SortedReputation {
    private String[] mapping;
    private String[] labels;
    private SimpleMatrix reputation;

    public SortedReputation(String[] mapping, String[] labels, SimpleMatrix reputation) {
        this.mapping = mapping;
        this.reputation = reputation;
        this.labels = labels;
    }

    public SimpleMatrix getReputation() {
        return reputation;
    }

    public void setReputation(SimpleMatrix reputation) {
        this.reputation = reputation;
    }

    public String[] getMapping() {
        return mapping;
    }

    public void setMapping(String[] mapping) {
        this.mapping = mapping;
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }
}

package com.ma.ReputationAlgorithms.Backstage.EstimatedPraiseProbability;

import org.ejml.simple.SimpleMatrix;

/**
 * Created by Marco on 07.12.2015.
 */
public abstract class EstimatedPraiseProbabilityMatrix {
    SimpleMatrix m;

    public abstract SimpleMatrix get();

    public abstract SimpleMatrix getReputation();

    public void print() {
        System.out.println("Estimated Praise Probability Matrix");
        m.print();
    }

}

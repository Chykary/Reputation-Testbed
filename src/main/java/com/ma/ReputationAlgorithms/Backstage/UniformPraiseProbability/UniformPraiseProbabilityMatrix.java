package com.ma.ReputationAlgorithms.Backstage.UniformPraiseProbability;

import org.ejml.simple.SimpleMatrix;

/**
 * Created by Marco on 07.12.2015.
 */
public abstract class UniformPraiseProbabilityMatrix {
    SimpleMatrix m;

    public abstract SimpleMatrix get();

    public void print() {
        System.out.println("Uniform Praise Probability Matrix");
        m.print();
    }
}

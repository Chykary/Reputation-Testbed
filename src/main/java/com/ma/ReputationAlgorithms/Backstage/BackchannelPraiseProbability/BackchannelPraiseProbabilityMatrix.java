package com.ma.ReputationAlgorithms.Backstage.BackchannelPraiseProbability;

import org.ejml.simple.SimpleMatrix;

/**
 * Created by Marco on 07.12.2015.
 */
public abstract class BackchannelPraiseProbabilityMatrix {
    SimpleMatrix m;

    public abstract SimpleMatrix get();

    public void print() {
        System.out.println("Backchannel Praise Probability Matrix");
        m.print();
    }
}

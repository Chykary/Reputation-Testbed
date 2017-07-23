package com.ma.ReputationAlgorithms.Backstage.UniformPraiseProbability;

import org.ejml.simple.SimpleMatrix;

/**
 * Created by Marco on 08.12.2015.
 */
public class SimpleUniformPraiseProbabilityMatrix extends UniformPraiseProbabilityMatrix {

    public SimpleUniformPraiseProbabilityMatrix(SimpleMatrix data) {
        int studentCount = data.numRows();
        m = new SimpleMatrix(studentCount, studentCount);
        double val = 1 / (double) studentCount;
        m.set(val);
    }

    public SimpleMatrix get() {
        return m;
    }
}

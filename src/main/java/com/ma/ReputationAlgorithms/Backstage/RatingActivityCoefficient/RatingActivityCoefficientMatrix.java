package com.ma.ReputationAlgorithms.Backstage.RatingActivityCoefficient;

import org.ejml.simple.SimpleMatrix;

/**
 * Created by Marco on 07.12.2015.
 */
public abstract class RatingActivityCoefficientMatrix {
    SimpleMatrix m;

    public abstract SimpleMatrix get();

    public void print() {
        System.out.println("Rating Activity Coefficient Matrix");
        m.print();
    }
}

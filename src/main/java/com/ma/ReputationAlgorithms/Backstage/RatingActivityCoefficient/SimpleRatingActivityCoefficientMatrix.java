package com.ma.ReputationAlgorithms.Backstage.RatingActivityCoefficient;

import org.ejml.simple.SimpleMatrix;

/**
 * Created by Marco on 09.12.2015.
 */
public class SimpleRatingActivityCoefficientMatrix extends RatingActivityCoefficientMatrix {

    public SimpleRatingActivityCoefficientMatrix(SimpleMatrix votes, int smoothing) {
        int studentCount = votes.numRows();
        m = new SimpleMatrix(studentCount, studentCount);
        for (int i = 0; i < studentCount; i++) {
            double total = 0;
            for (int j = 0; j < studentCount; j++) {
                total += votes.get(i, j);
            }
            double activity = (total + smoothing) / (total + (2 * smoothing));
            m.set(i, i, activity);
        }
    }

    public SimpleMatrix get() {
        return m;
    }
}

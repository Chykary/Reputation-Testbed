package com.ma.ReputationAlgorithms.Backstage.EstimatedPraiseProbability;

import com.ma.Misc.Helpers;
import com.ma.ReputationAlgorithms.Backstage.BackchannelPraiseProbability.BackchannelPraiseProbabilityMatrix;
import com.ma.ReputationAlgorithms.Backstage.RatingActivityCoefficient.RatingActivityCoefficientMatrix;
import com.ma.ReputationAlgorithms.Backstage.UniformPraiseProbability.UniformPraiseProbabilityMatrix;
import org.ejml.simple.SimpleMatrix;

/**
 * Created by Marco on 07.12.2015.
 */
public class SimpleEstimatedPraiseProbabilityMatrix extends EstimatedPraiseProbabilityMatrix {

    public SimpleEstimatedPraiseProbabilityMatrix(BackchannelPraiseProbabilityMatrix B, RatingActivityCoefficientMatrix A, UniformPraiseProbabilityMatrix I) {
        SimpleMatrix identity = SimpleMatrix.identity(A.get().numRows());
        SimpleMatrix p1 = A.get().mult(B.get());
        SimpleMatrix p2 = identity.minus(A.get());
        p2 = p2.mult(I.get());
        m = p1.plus(p2);
    }

    public SimpleMatrix get() {
        return m;
    }

    public SimpleMatrix getReputation() {
        return Helpers.powerMethod(m.transpose(), 10);
    }
}

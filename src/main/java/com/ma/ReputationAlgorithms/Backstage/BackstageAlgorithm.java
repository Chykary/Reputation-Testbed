package com.ma.ReputationAlgorithms.Backstage;

import com.ma.Misc.ReputationData;
import com.ma.ReputationAlgorithms.Backstage.BackchannelPraiseProbability.BackchannelPraiseProbabilityMatrix;
import com.ma.ReputationAlgorithms.Backstage.BackchannelPraiseProbability.SimpleBackchannelPraiseProbabilityMatrix;
import com.ma.ReputationAlgorithms.Backstage.EstimatedPraiseProbability.EstimatedPraiseProbabilityMatrix;
import com.ma.ReputationAlgorithms.Backstage.EstimatedPraiseProbability.SimpleEstimatedPraiseProbabilityMatrix;
import com.ma.ReputationAlgorithms.Backstage.RatingActivityCoefficient.RatingActivityCoefficientMatrix;
import com.ma.ReputationAlgorithms.Backstage.RatingActivityCoefficient.SimpleRatingActivityCoefficientMatrix;
import com.ma.ReputationAlgorithms.Backstage.UniformPraiseProbability.SimpleUniformPraiseProbabilityMatrix;
import com.ma.ReputationAlgorithms.Backstage.UniformPraiseProbability.UniformPraiseProbabilityMatrix;
import com.ma.ReputationAlgorithms.ReputationAlgorithm;
import org.ejml.simple.SimpleMatrix;

/**
 * Created by Marco on 10.03.2016.
 */
public class BackstageAlgorithm extends ReputationAlgorithm {
    static int smoothness = 1;
    static String name = "Backstage";

    public BackstageAlgorithm(int smoothness) {
        this.smoothness = smoothness;
    }

    public BackstageAlgorithm() {
    }

    public static int getSmoothness() {
        return smoothness;
    }

    public static void setSmoothness(int smoothness) {
        BackstageAlgorithm.smoothness = smoothness;
    }

    private SimpleMatrix calculate(SimpleMatrix rep) {
        BackchannelPraiseProbabilityMatrix B = new SimpleBackchannelPraiseProbabilityMatrix(rep);
        RatingActivityCoefficientMatrix A = new SimpleRatingActivityCoefficientMatrix(rep, smoothness);
        UniformPraiseProbabilityMatrix I = new SimpleUniformPraiseProbabilityMatrix(rep);
        EstimatedPraiseProbabilityMatrix R = new SimpleEstimatedPraiseProbabilityMatrix(B, A, I);
        return R.getReputation();
    }

    public SimpleMatrix getRawReputation(ReputationData rep) {
        return calculate(rep.getPositive());
    }

    public String getName() {
        return name;
    }
}

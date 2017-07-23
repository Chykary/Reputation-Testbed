package com.ma.ReputationAlgorithms.EigenTrust;

import com.ma.Misc.Helpers;
import com.ma.Misc.ReputationData;
import com.ma.ReputationAlgorithms.ReputationAlgorithm;
import org.ejml.simple.SimpleMatrix;

/**
 * Created by Marco on 10.03.2016.
 */
public class EigenTrustAlgorithm extends ReputationAlgorithm {
    static double error = 0.2;
    static long maxIterations = 200;
    static String name = "EigenTrust";

    public EigenTrustAlgorithm(double error) {
        this.error = error;
    }

    public EigenTrustAlgorithm() {
    }

    public static long getMaxIterations() {
        return maxIterations;
    }

    public static void setMaxIterations(long maxIterations) {
        EigenTrustAlgorithm.maxIterations = maxIterations;
    }

    private SimpleMatrix calculate(SimpleMatrix edge) {
        SimpleMatrix ev = new SimpleMatrix(edge);
        double uniformProbability = 1.0 / ev.numRows();
        for (int i = 0; i < ev.numRows(); i++) {
            int totalColScore = 0;
            for (int j = 0; j < ev.numCols(); j++) {
                totalColScore += Math.max(ev.get(i, j), 0);
            }

            if (totalColScore > 0) {
                for (int j = 0; j < ev.numCols(); j++) {
                    double newVal = Math.max(ev.get(i, j), 0);
                    newVal /= totalColScore;
                    ev.set(i, j, newVal);
                }
            } else {
                for (int j = 0; j < ev.numCols(); j++) {
                    ev.set(i, j, uniformProbability);
                }
            }
        }
        SimpleMatrix trustVector = new SimpleMatrix(ev.numRows(), 1);
        trustVector.set(uniformProbability);
        double currentError;
        int i = 1;
        ev = Helpers.makeRowStochastic(ev);
        do {
            SimpleMatrix trustVectorNext = ev.transpose().mult(trustVector);
            currentError = Helpers.sumNorm(trustVectorNext.minus(trustVector));
            trustVector = new SimpleMatrix(trustVectorNext);
            i++;
        } while (currentError > error && i < 200);
        return Helpers.toLengthOneMinimumZero(trustVector);
    }

    public SimpleMatrix getRawReputation(ReputationData rep) {
        SimpleMatrix results = rep.getPositive().minus(rep.getNegative());
        for (int i = 0; i < results.getNumElements(); i++) {
            results.set(i, Math.max(0, results.get(i)));
        }
        return calculate(results);
    }

    public String getName() {
        return name;
    }
}

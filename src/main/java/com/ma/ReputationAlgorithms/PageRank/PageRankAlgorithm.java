package com.ma.ReputationAlgorithms.PageRank;

import com.ma.Misc.Helpers;
import com.ma.Misc.ReputationData;
import com.ma.ReputationAlgorithms.ReputationAlgorithm;
import org.ejml.simple.SimpleMatrix;

/**
 * Created by Marco on 10.03.2016.
 */
public class PageRankAlgorithm extends ReputationAlgorithm {
    static double inverseLeapFactor = 0.85;
    static String name = "PageRank";

    public PageRankAlgorithm(double inverseLeapFactor) {
        this.inverseLeapFactor = inverseLeapFactor;
    }

    public PageRankAlgorithm() {
    }

    private SimpleMatrix calculate(SimpleMatrix edges) {
        SimpleMatrix linkMatrix = Helpers.makeRowStochastic(edges);
        SimpleMatrix leapFactorNormalisation = new SimpleMatrix(linkMatrix.numRows(), linkMatrix.numCols());
        for (int i = 0; i < leapFactorNormalisation.numCols(); i++) {
            leapFactorNormalisation.set(i, i, inverseLeapFactor);
        }

        linkMatrix = linkMatrix.mult(leapFactorNormalisation);

        double distributedRandomLeapValue = (1 - inverseLeapFactor) / linkMatrix.numCols();
        SimpleMatrix randomLeap = new SimpleMatrix(linkMatrix.numRows(), linkMatrix.numCols());
        randomLeap.set(distributedRandomLeapValue);
        linkMatrix = linkMatrix.plus(randomLeap);
        for (int i = 0; i < linkMatrix.numCols(); i++) {
            linkMatrix.set(i, i, 0);
        }
        linkMatrix = Helpers.makeRowStochastic(linkMatrix);
        SimpleMatrix ev = Helpers.powerMethod(linkMatrix.transpose(), 10);
        return Helpers.toLengthOne(ev);
    }

    public SimpleMatrix getRawReputation(ReputationData rep) {
        return calculate(rep.getPositive());
    }

    public String getName() {
        return name;
    }
}

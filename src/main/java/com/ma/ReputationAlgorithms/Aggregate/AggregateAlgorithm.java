package com.ma.ReputationAlgorithms.Aggregate;

import com.ma.Misc.Helpers;
import com.ma.Misc.ReputationData;
import com.ma.ReputationAlgorithms.ReputationAlgorithm;
import org.ejml.simple.SimpleMatrix;

/**
 * Created by Marco on 10.03.2016.
 */
public class AggregateAlgorithm extends ReputationAlgorithm {
    static String name = "Aggregate";

    public AggregateAlgorithm() {
    }

    private SimpleMatrix calculate(SimpleMatrix votes) {
        votes = votes.transpose();
        SimpleMatrix aggregation = new SimpleMatrix(votes.numRows(), 1);
        for (int i = 0; i < votes.numCols(); i++) {
            int total = 0;
            for (int j = 0; j < votes.numRows(); j++) {
                total += votes.get(i, j);
            }
            aggregation.set(i, 0, total);
        }
        return Helpers.toLengthOneMinimumZero(aggregation);
    }

    public SimpleMatrix getRawReputation(ReputationData rep) {
        SimpleMatrix results = rep.getPositive().minus(rep.getNegative());
        return calculate(results);
    }

    public String getName() {
        return name;
    }
}

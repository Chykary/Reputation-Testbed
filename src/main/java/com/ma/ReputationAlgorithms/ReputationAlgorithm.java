package com.ma.ReputationAlgorithms;

import com.ma.Misc.Helpers;
import com.ma.Misc.ReputationData;
import com.ma.Misc.SortedReputation;
import org.ejml.simple.SimpleMatrix;

/**
 * Created by Marco on 21.03.2016.
 */
public abstract class ReputationAlgorithm {
    public ReputationAlgorithm() {
    }

    public SortedReputation getReputation(ReputationData rep, String[] mapping, String[] labels) {
        return Helpers.sort(mapping, labels, getRawReputation(rep));
    }

    public abstract SimpleMatrix getRawReputation(ReputationData rep);

    public abstract String getName();
}

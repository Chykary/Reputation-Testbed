package com.ma.ReputationAlgorithms.Backstage.BackchannelPraiseProbability;

import com.ma.Misc.Helpers;
import org.ejml.simple.SimpleMatrix;

/**
 * Created by Marco on 08.12.2015.
 */
public class SimpleBackchannelPraiseProbabilityMatrix extends BackchannelPraiseProbabilityMatrix {

    public SimpleBackchannelPraiseProbabilityMatrix(SimpleMatrix data) {
        m = Helpers.makeRowStochastic(data);
    }

    public SimpleMatrix get() {
        return m;
    }
}
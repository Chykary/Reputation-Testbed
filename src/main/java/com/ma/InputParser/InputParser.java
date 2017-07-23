package com.ma.InputParser;

import com.ma.Misc.ReputationData;

/**
 * Created by Marco on 10.01.2016.
 */
public abstract class InputParser {
    ReputationData reputationData;
    String[] mapping;

    public abstract void parseFile(String path, NegativeVoteTreatment nvt);

    public ReputationData getReputationData() {
        return reputationData;
    }

    public String[] getStudentMapping() {
        return mapping;
    }
}

package com.ma.Scheduler;

import com.ma.Misc.ReputationData;
import com.ma.Misc.SortedReputation;
import com.ma.Outputter.Outputter;
import com.ma.ReputationAlgorithms.ReputationAlgorithm;
import org.apache.commons.lang3.time.StopWatch;
import org.ejml.simple.SimpleMatrix;

import java.util.List;

/**
 * Created by Marco on 04.04.2016.
 */
public class SingleReputationThread implements Runnable {
    private ReputationAlgorithm algorithm;
    private String[] mapping;
    private String[] labels;
    private ReputationData data;
    private long identifier;
    private List<Outputter> outputters;

    public SingleReputationThread(ReputationAlgorithm algorithm, String[] mapping, String[] labels, ReputationData data, long identifier, List<Outputter> outputters) {
        this.algorithm = algorithm;
        this.mapping = mapping;
        this.data = data;
        this.identifier = identifier;
        this.outputters = outputters;
        this.labels = labels;
    }

    public void run() {
        StopWatch sw = new StopWatch();
        sw.start();
        System.out.println("Running " + algorithm.getName() + "... ");
        SortedReputation scores = algorithm.getReputation(data, mapping, labels);
        sw.stop();
        saveAll(Scheduler.getIdentifier(identifier, algorithm.getName()), scores.getReputation(), scores.getMapping(), scores.getLabels(), ((double) sw.getTime() / 1000d) + "s");
        System.out.println("Saved results for " + algorithm.getName() + ", took " + ((double) sw.getTime() / 1000d) + "s");
    }

    private void saveAll(String identifier, SimpleMatrix rep, String[] map, String[] lab, String time) {
        for (Outputter o : outputters) {
            o.save(identifier, rep, map, lab, time);
        }
    }
}

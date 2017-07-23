package com.ma.Scheduler;

import com.ma.Misc.Helpers;
import com.ma.Misc.SortedReputation;
import com.ma.Outputter.Outputter;
import com.ma.ReputationAlgorithms.ReputationAlgorithm;
import com.ma.Synthetic.Community;
import org.apache.commons.lang3.time.StopWatch;
import org.ejml.simple.SimpleMatrix;

import java.util.List;

/**
 * Created by Marco on 04.04.2016.
 */
public class SimulationReputationThread implements Runnable {
    private ReputationAlgorithm algorithm;
    private String[] mapping;
    private String[] labels;
    private Community community;
    private long identifier;
    private List<Outputter> outputters;
    private int steps;
    private int recordAfterEvery;

    public SimulationReputationThread(ReputationAlgorithm algorithm, String[] mapping, String[] labels, Community community, int steps, int recordAfterEvery, long identifier, List<Outputter> outputters) {
        this.algorithm = algorithm;
        this.mapping = mapping;
        this.community = community;
        this.identifier = identifier;
        this.outputters = outputters;
        this.steps = steps;
        this.recordAfterEvery = recordAfterEvery;
        this.labels = labels;
    }

    public void run() {
        StopWatch sw = new StopWatch();
        sw.start();
        SimpleMatrix lastReputation = new SimpleMatrix(community.getSize(), 1);
        lastReputation.set(0);
        System.out.println("Running " + algorithm.getName() + "... ");
        for (int i = 1; i <= steps; i++) {
            community.step(lastReputation);
            lastReputation = algorithm.getRawReputation(community.getCurrentVotings());
            if (i % recordAfterEvery == 0) {
                System.out.println(algorithm.getName() + " completed round " + i);
                SortedReputation scores = Helpers.sort(mapping, labels, lastReputation);
                saveAll(Scheduler.getIdentifierWithIndex(identifier, algorithm.getName(), i), scores.getReputation(), scores.getMapping(), scores.getLabels(), ((double) sw.getTime() / 1000d) + "s");
            }
        }
        sw.stop();
        System.out.println(algorithm.getName() + " done. " + community.getArtifacts().size() + " posted, " + community.getVotes() + " votes cast. Took " + ((double) sw.getTime() / 1000d) + "s");
    }

    private void saveAll(String identifier, SimpleMatrix rep, String[] map, String[] lab, String time) {
        for (Outputter o : outputters) {
            o.save(identifier, rep, map, lab, time);
        }
    }
}

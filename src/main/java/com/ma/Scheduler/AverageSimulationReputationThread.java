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
public class AverageSimulationReputationThread implements Runnable {
    private ReputationAlgorithm algorithm;
    private String[] mapping;
    private String[] labels;
    private Community community;
    private long identifier;
    private List<Outputter> outputters;
    private int steps;
    private int recordAfterEvery;
    private int rounds;

    public AverageSimulationReputationThread(ReputationAlgorithm algorithm, String[] mapping, String[] labels, Community community, int steps, int recordAfterEvery, int rounds, long identifier, List<Outputter> outputters) {
        this.algorithm = algorithm;
        this.mapping = mapping;
        this.community = community;
        this.identifier = identifier;
        this.outputters = outputters;
        this.steps = steps;
        this.recordAfterEvery = recordAfterEvery;
        this.rounds = rounds;
        this.labels = labels;
    }

    public void run() {
        SimpleMatrix[] recordings = new SimpleMatrix[steps / recordAfterEvery];
        if (recordings.length == 0) {
            System.out.println("Did not record anything. Terminating.");
            return;
        }

        for (int i = 0; i < recordings.length; i++) {
            recordings[i] = new SimpleMatrix(community.getSize(), 1);
            recordings[i].set(0);
        }


        StopWatch sw = new StopWatch();
        sw.start();

        System.out.println("Running " + algorithm.getName() + "... ");
        Community communityClone = community.clone();
        for (int s = 0; s < rounds; s++) {
            communityClone = community.clone();
            SimpleMatrix lastReputation = new SimpleMatrix(communityClone.getSize(), 1);
            lastReputation.set(0);
            int recordingIndex = 0;
            for (int i = 1; i <= steps; i++) {
                communityClone.step(lastReputation);
                lastReputation = algorithm.getRawReputation(communityClone.getCurrentVotings());
                if (i % recordAfterEvery == 0) {
                    recordings[recordingIndex] = recordings[recordingIndex].plus(lastReputation);
                    recordingIndex++;
                }
            }
            if (s % 10 == 0) {
                System.out.println(algorithm.getName() + " completed round " + s + " of " + rounds);
            }
        }
        sw.stop();

        for (int i = 0; i < recordings.length; i++) {
            recordings[i] = recordings[i].divide(rounds);
            SortedReputation scores = Helpers.sort(mapping, labels, recordings[i]);
            saveAll(Scheduler.getIdentifierWithIndexAndRounds(identifier, algorithm.getName(), ((i + 1) * recordAfterEvery), rounds), scores.getReputation(), scores.getMapping(), scores.getLabels(), ((double) sw.getTime() / 1000d) + "s");
        }

        System.out.println(algorithm.getName() + " done. Took " + ((double) sw.getTime() / 1000d) + "s");

        communityClone.getStats().print();

    }

    private void saveAll(String identifier, SimpleMatrix rep, String[] map, String[] lab, String time) {
        for (Outputter o : outputters) {
            o.save(identifier, rep, map, lab, time);
        }
    }
}

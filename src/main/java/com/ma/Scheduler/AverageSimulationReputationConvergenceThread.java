package com.ma.Scheduler;

import com.ma.Misc.Helpers;
import com.ma.Misc.SortedReputation;
import com.ma.Outputter.QualityMeasureOutputter;
import com.ma.ReputationAlgorithms.ReputationAlgorithm;
import com.ma.Synthetic.Community;
import org.apache.commons.lang3.time.StopWatch;
import org.ejml.simple.SimpleMatrix;

import java.util.Arrays;

/**
 * Created by Marco on 04.04.2016.
 */
public class AverageSimulationReputationConvergenceThread implements Runnable {
    private ReputationAlgorithm algorithm;
    private String[] mapping;
    private String[] labels;
    private Community community;
    private long identifier;
    private QualityMeasureOutputter qmo;
    private int steps;
    private int recordAfterEvery;
    private int rounds;

    public AverageSimulationReputationConvergenceThread(ReputationAlgorithm algorithm, String[] mapping, String[] labels, Community community, int steps, int recordAfterEvery, int rounds, long identifier, QualityMeasureOutputter qmo) {
        this.algorithm = algorithm;
        this.mapping = mapping;
        this.community = community;
        this.identifier = identifier;
        this.qmo = qmo;
        this.steps = steps;
        this.recordAfterEvery = recordAfterEvery;
        this.rounds = rounds;
        this.labels = labels;
    }

    public void run() {
        double[] correctness = new double[steps / recordAfterEvery];
        double[] inversionQuality = new double[steps / recordAfterEvery];
        double[] distinction = new double[steps / recordAfterEvery];

        Arrays.fill(correctness, 0);
        Arrays.fill(inversionQuality, 0);
        Arrays.fill(distinction, 0);

        if (correctness.length == 0) {
            System.out.println("Did not record anything. Terminating.");
            return;
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
                    SortedReputation scores = Helpers.sort(mapping, labels, lastReputation);
                    correctness[recordingIndex] += qmo.getCorrectness(scores.getLabels());
                    inversionQuality[recordingIndex] += qmo.getInversionQuality(scores.getLabels());
                    distinction[recordingIndex] += qmo.getDistinction(scores.getReputation());
                    recordingIndex++;
                }
            }
            if (s % 10 == 0) {
                System.out.println(algorithm.getName() + " completed round " + s + " of " + rounds);
            }
        }
        sw.stop();

        for (int i = 0; i < correctness.length; i++) {
            correctness[i] = correctness[i] / rounds;
            inversionQuality[i] = inversionQuality[i] / rounds;
            distinction[i] = distinction[i] / rounds;
        }

        System.out.println(algorithm.getName() + " done. Took " + ((double) sw.getTime() / 1000d) + "s");
        qmo.writeConvergenceData(Scheduler.getIdentifierWithIndexAndRounds(identifier, algorithm.getName(), steps, rounds), correctness, inversionQuality, distinction, recordAfterEvery);
        communityClone.getStats().print();

    }
}

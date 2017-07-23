package com.ma.Scheduler;

import com.ma.MappingCreator.MappingCreator;
import com.ma.MappingCreator.ShortStudentMapping;
import com.ma.Misc.Helpers;
import com.ma.Misc.ReputationData;
import com.ma.Outputter.Outputter;
import com.ma.Outputter.QualityMeasureOutputter;
import com.ma.ReputationAlgorithms.ReputationAlgorithm;
import com.ma.Synthetic.Community;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

/**
 * Created by Marco on 21.03.2016.
 */
public class Scheduler {
    Reflections reflections = new Reflections("com.ma");
    ArrayList<Outputter> outputters = new ArrayList<>();
    MappingCreator mappingCreator = new ShortStudentMapping();

    static String getIdentifier(long identifier, String name) {
        return identifier + "_" + name + ".csv";
    }

    static String getIdentifierWithIndex(long identifier, String name, int idx) {
        return identifier + "_" + name + "_" + idx;
    }

    static String getIdentifierWithIndexAndRounds(long identifier, String name, int idx, int rnds) {
        return identifier + "_" + name + "_" + idx + "_" + rnds + "r";
    }

    public void schedule(ReputationData rep, String[] mapping, ReputationAlgorithm... algorithms) {
        System.out.println("Detected " + Runtime.getRuntime().availableProcessors() + " cores, enqueued " + algorithms.length + " algorithms.");
        long identifier = new Date().getTime();
        for (ReputationAlgorithm r : algorithms) {
            (new Thread(new SingleReputationThread(r, mapping.clone(), Helpers.getDefaultLabels(mapping.length), rep, identifier, outputters))).start();
        }
    }

    private void threadedCalculation() {
        int cores = Runtime.getRuntime().availableProcessors();

    }

    public void scheduleAll(ReputationData rep, String[] mapping) {

        schedule(rep, mapping, getAlgorithms());
    }

    public void scheduleSynthetic(Community c, int steps, int recordAfterEvery, ReputationAlgorithm... algorithms) {
        System.out.println("Detected " + Runtime.getRuntime().availableProcessors() + " cores, enqueued " + algorithms.length + " algorithms.");
        Community[] communities = new Community[algorithms.length];
        String[] mapping = mappingCreator.createMapping(c.getSize());
        long identifier = new Date().getTime();

        for (int i = 0; i < communities.length; i++) {
            communities[i] = c.clone();
        }
        int ridx = 0;
        for (ReputationAlgorithm r : algorithms) {
            (new Thread(new SimulationReputationThread(r, mapping.clone(), communities[ridx].getLabels(), communities[ridx], steps, recordAfterEvery, identifier, outputters))).start();
            ridx++;
        }
    }

    public void scheduleSynthethicAll(Community c, int steps, int recordAfterEvery) {
        scheduleSynthetic(c, steps, recordAfterEvery, getAlgorithms());
    }

    public void scheduleSynthethicAveragedAll(Community c, int steps, int recordAfterEvery, int rounds) {
        scheduleSynthethicAveraged(c, steps, recordAfterEvery, rounds, getAlgorithms());
    }

    public void scheduleSynthethicAveraged(Community c, int steps, int recordAfterEvery, int rounds, ReputationAlgorithm... algorithms) {
        System.out.println("Detected " + Runtime.getRuntime().availableProcessors() + " cores, enqueued " + algorithms.length + " algorithms.");
        Community[] communities = new Community[algorithms.length];
        String[] mapping = mappingCreator.createMapping(c.getSize());
        long identifier = new Date().getTime();

        for (int i = 0; i < communities.length; i++) {
            communities[i] = c.clone();
        }
        int ridx = 0;
        for (ReputationAlgorithm r : algorithms) {
            (new Thread(new AverageSimulationReputationThread(r, mapping.clone(), communities[ridx].getLabels(), communities[ridx], steps, recordAfterEvery, rounds, identifier, outputters))).start();
            ridx++;
        }
    }

    public void scheduleSynthethicAveragedConvergenceAll(Community c, int steps, int recordAfterEvery, int rounds, QualityMeasureOutputter qmo) {
        scheduleSynthethicAveragedConverge(c, steps, recordAfterEvery, rounds, qmo, getAlgorithms());
    }

    public void scheduleSynthethicAveragedConverge(Community c, int steps, int recordAfterEvery, int rounds, QualityMeasureOutputter qmo, ReputationAlgorithm... algorithms) {
        System.out.println("Detected " + Runtime.getRuntime().availableProcessors() + " cores, enqueued " + algorithms.length + " algorithms.");
        Community[] communities = new Community[algorithms.length];
        String[] mapping = mappingCreator.createMapping(c.getSize());
        long identifier = new Date().getTime();

        for (int i = 0; i < communities.length; i++) {
            communities[i] = c.clone();
        }
        int ridx = 0;
        for (ReputationAlgorithm r : algorithms) {
            (new Thread(new AverageSimulationReputationConvergenceThread(r, mapping.clone(), communities[ridx].getLabels(), communities[ridx], steps, recordAfterEvery, rounds, identifier, qmo))).start();
            ridx++;
        }
    }

    private ReputationAlgorithm[] getAlgorithms() {
        Set<Class<? extends ReputationAlgorithm>> subTypes = reflections.getSubTypesOf(ReputationAlgorithm.class);
        ReputationAlgorithm[] algorithms = new ReputationAlgorithm[subTypes.size()];

        int i = 0;
        for (Class r : subTypes) {
            try {
                algorithms[i] = (ReputationAlgorithm) r.newInstance();
            } catch (InstantiationException e) {
                System.out.println("Could not instantiate " + r.getName() + " - did you define a parameterless constructor?");
            } catch (IllegalAccessException e) {
                System.out.println("Could not access while instantiating " + r.getName());
            }
            i++;
        }
        return algorithms;
    }

    public void clearOutputters() {
        outputters.clear();
    }

    public void addOutputter(Outputter outputter) {
        outputters.add(outputter);
    }

    public MappingCreator getMappingCreator() {
        return mappingCreator;
    }

    public void setMappingCreator(MappingCreator mappingCreator) {
        mappingCreator = mappingCreator;
    }


}



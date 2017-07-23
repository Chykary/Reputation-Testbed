package com.ma.Outputter;

import com.ma.Misc.Helpers;
import org.ejml.simple.SimpleMatrix;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

/**
 * Created by Marco on 20.05.2016.
 */
public class QualityMeasureOutputter extends Outputter {
    List<LabelColorPair> lcp;
    List<LabelCountPair> labelCountPairs;

    public QualityMeasureOutputter(List<LabelColorPair> lcp) {
        this.lcp = lcp;
    }

    public void save(String path, SimpleMatrix reputations, String[] mapping, String[] labels, String time) {
        initPrePath();
        path = getPrePath() + path;
        double inversions = 0;
        double inversionRatio;
        double distinction = 0;
        double correctness = 0;
        labelCountPairs = Helpers.getLabelCountPairFromMatrixAndColorPair(lcp, labels);

        //Calculate distinction
        double averageDiff = 0d;
        for (int i = 0; i < reputations.getNumElements() - 1; i++) {
            averageDiff += reputations.get(i + 1) - reputations.get(i);
        }
        averageDiff = averageDiff / reputations.getNumElements();

        int currIdx = 0;
        for (int i = 0; i < labelCountPairs.size() - 1; i++) {
            currIdx += labelCountPairs.get(i).getCount();
            distinction += reputations.get(currIdx) - reputations.get(currIdx - 1);
        }
        if (labelCountPairs.size() - 1 != 0) {
            distinction /= labelCountPairs.size() - 1;
        }

        //Calculate correctness
        for (int i = 0; i < labels.length; i++) {
            if (isInCorrectZone(labels[i], i)) {
                correctness += 1;
            }
        }
        correctness = correctness / labels.length;

        //Calculate inversions
        int[] rankedArr = convertLabelsToRankingArray(labels);
        for (int i = 0; i < rankedArr.length; i++) {
            for (int j = 0; j < i; j++) {
                if (rankedArr[j] > rankedArr[i]) {
                    inversions += 1;
                }
            }
        }
        double maxInversions = (rankedArr.length * (rankedArr.length - 1)) / 2;
        for (LabelCountPair tlcp : labelCountPairs) {
            maxInversions -= (tlcp.getCount() * (tlcp.getCount() - 1)) / 2;
        }

        inversionRatio = inversions / maxInversions;

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(path + ".txt"));

            out.write("###Distinction###");
            out.newLine();
            out.write("zoneDiff=" + distinction);
            out.newLine();
            out.write("averageDiff=" + averageDiff);
            out.newLine();
            out.write("distinction=" + (distinction / averageDiff));
            out.newLine();
            out.write("###Correctness###");
            out.newLine();
            out.write("correctness=" + correctness);
            out.newLine();
            out.write("###Inversions###");
            out.newLine();
            out.write("inversions=" + inversions);
            out.newLine();
            out.write("inversionQuality=" + (1 - inversionRatio));
            out.newLine();
            out.write("###Time###");
            out.newLine();
            out.write(time);
            out.close();

        } catch (Exception e) {
            System.out.println("Failed to save file:" + e.getMessage());
        }
    }

    private boolean isInCorrectZone(String label, int idx) {
        int min = 0;
        int max = 0;

        for (LabelCountPair lcop : labelCountPairs) {
            max += lcop.getCount();
            if (lcop.getLabel().equals(label)) {
                break;
            } else {
                min += lcop.getCount();
            }
        }
        return (idx >= min && idx < max);
    }

    private int[] convertLabelsToRankingArray(String[] labels) {
        int[] rankedArr = new int[labels.length];
        for (int i = 0; i < labels.length; i++) {
            rankedArr[i] = getPriority(labels[i]);
        }
        return rankedArr;
    }

    private int getPriority(String label) {
        for (int i = 0; i < labelCountPairs.size(); i++) {
            if (labelCountPairs.get(i).getLabel().equals(label)) {
                return i;
            }
        }
        return -1;
    }

    public double getDistinction(SimpleMatrix reputations) {
        double distinction = 0;
        double averageDiff = 0d;
        for (int i = 0; i < reputations.getNumElements() - 1; i++) {
            averageDiff += reputations.get(i + 1) - reputations.get(i);
        }
        averageDiff = averageDiff / reputations.getNumElements();

        int currIdx = 0;
        for (int i = 0; i < labelCountPairs.size() - 1; i++) {
            currIdx += labelCountPairs.get(i).getCount();
            distinction += reputations.get(currIdx) - reputations.get(currIdx - 1);
        }
        if (labelCountPairs.size() - 1 != 0) {
            distinction /= labelCountPairs.size() - 1;
        }
        return distinction / averageDiff;
    }

    public double getCorrectness(String[] labels) {
        labelCountPairs = Helpers.getLabelCountPairFromMatrixAndColorPair(lcp, labels);
        double correctness = 0;
        for (int i = 0; i < labels.length; i++) {
            if (isInCorrectZone(labels[i], i)) {
                correctness += 1;
            }
        }
        correctness = correctness / labels.length;
        return correctness;
    }

    public double getInversionQuality(String[] labels) {
        double inversions = 0;
        double inversionRatio;
        int[] rankedArr = convertLabelsToRankingArray(labels);
        for (int i = 0; i < rankedArr.length; i++) {
            for (int j = 0; j < i; j++) {
                if (rankedArr[j] > rankedArr[i]) {
                    inversions += 1;
                }
            }
        }
        double maxInversions = (rankedArr.length * (rankedArr.length - 1)) / 2;
        for (LabelCountPair tlcp : labelCountPairs) {
            maxInversions -= (tlcp.getCount() * (tlcp.getCount() - 1)) / 2;
        }

        inversionRatio = inversions / maxInversions;

        return 1 - inversionRatio;
    }

    public void writeConvergenceData(String path, double[] correctness, double[] inversionQuality, double[] distinction, int recordAfterEvery) {
        initPrePath();
        path = getPrePath() + path;

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(path + ".txt"));
            out.write("###Correctness###");
            out.newLine();
            for (int i = 0; i < correctness.length; i++) {
                out.write((i + 1) * recordAfterEvery + ":" + correctness[i]);
                out.newLine();
            }

            out.write("###Inversion Quality###");
            out.newLine();
            for (int i = 0; i < inversionQuality.length; i++) {
                out.write((i + 1) * recordAfterEvery + ":" + inversionQuality[i]);
                out.newLine();
            }

            out.write("###Distinction###");
            out.newLine();
            for (int i = 0; i < distinction.length; i++) {
                out.write((i + 1) * recordAfterEvery + ":" + distinction[i]);
                out.newLine();
            }

            out.close();

        } catch (Exception e) {
            System.out.println("Failed to save file:" + e.getMessage());
        }
    }


}

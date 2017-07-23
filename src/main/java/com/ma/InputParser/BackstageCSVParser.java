package com.ma.InputParser;

import com.ma.Misc.Helpers;
import com.ma.Misc.LooseVote;
import com.ma.Misc.ReputationData;
import org.ejml.simple.SimpleMatrix;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Marco on 10.01.2016.
 */
public class BackstageCSVParser extends InputParser {

    final int TYPE_INDEX = 2;
    final int FROM_INDEX = 3;
    final int TO_INDEX = 5;
    final int EXPECTED_SIZE = 10;
    String[] positiveTypes = {"Approve"};
    String[] negativeTypes = {"Reject", "OffTopic"};
    NegativeVoteTreatment mode;

    public void parseFile(String path, NegativeVoteTreatment nvt) {
        mode = nvt;
        System.out.println("Parsing " + path);
        HashSet<String> students = new HashSet<String>();
        ArrayList<LooseVote> looseVotes = new ArrayList<LooseVote>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));
            String line;
            int lineCount = 0;
            int negatives = 0;
            int positives = 0;
            while ((line = in.readLine()) != null) {
                if (lineCount > 0) { //ignore first line
                    String[] fields = line.split("@");
                    Helpers.cleanString(fields, "\"");
                    if (fields.length == EXPECTED_SIZE) {
                        int value = 0;
                        if (Helpers.containedInStringArray(positiveTypes, fields[TYPE_INDEX])) {
                            value = 1;
                            positives++;
                        } else if (Helpers.containedInStringArray(negativeTypes, fields[TYPE_INDEX])) {
                            if (mode != NegativeVoteTreatment.Ignore) {
                                value = -1;
                                negatives++;
                            }
                        }
                        LooseVote lv = new LooseVote(fields[FROM_INDEX], fields[TO_INDEX], value);
                        students.add(fields[FROM_INDEX]);
                        students.add(fields[TO_INDEX]);
                        looseVotes.add(lv);
                    }
                }
                lineCount++;
            }

            reputationData = new ReputationData(students.size());
            mapping = new String[students.size()];
            students.toArray(mapping);
            for (LooseVote lv : looseVotes) {
                reputationData.vote(Helpers.find(mapping, lv.from), Helpers.find(mapping, lv.to), lv.value);
            }

            if (mode == NegativeVoteTreatment.Elevate) {
                SimpleMatrix votes = reputationData.getPositive();
                double min = Helpers.findMinimum(votes);
                if (min < 0) {
                    min = Math.abs(min);
                    SimpleMatrix elevateMatrix = new SimpleMatrix(votes.numRows(), votes.numCols());
                    elevateMatrix.set(min);
                    reputationData.setPositive(votes.plus(elevateMatrix));
                }

            } else if (mode == NegativeVoteTreatment.Ratio) {
                String currentStudent = null;
                for (Iterator<LooseVote> iterator = looseVotes.iterator(); iterator.hasNext(); ) {

                }
            }

            System.out.println("Processed " + lineCount + " lines, " + students.size() + " students. -- " + negatives + " ++ " + positives);

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (IOException e) {
            System.out.println("I/O Error.");
        }
    }
}

package com.ma.Misc;

import org.ejml.simple.SimpleMatrix;

/**
 * Created by Marco on 07.12.2015.
 */
public class ReputationData {

    SimpleMatrix positiveVotes;
    SimpleMatrix negativeVotes;
    int studentCount;

    public ReputationData(int students) {
        positiveVotes = new SimpleMatrix(students, students);
        negativeVotes = new SimpleMatrix(students, students);
        studentCount = students;
    }

    public void vote(int student1, int student2) {
        if (student1 > positiveVotes.numRows() || student2 > positiveVotes.numCols()) {
            return;
        }
        double currentVote = positiveVotes.get(student1, student2);
        positiveVotes.set(student1, student2, currentVote + 1);
    }

    public void vote(int student1, int student2, int voteCount) {
        if (student1 > positiveVotes.numRows() || student2 > positiveVotes.numCols()) {
            return;
        }
        if (voteCount >= 0) {
            double currentVote = positiveVotes.get(student1, student2);
            positiveVotes.set(student1, student2, currentVote + voteCount);
        } else {
            double currentVote = negativeVotes.get(student1, student2);
            negativeVotes.set(student1, student2, currentVote + (voteCount * -1));
        }

    }

    public void vote(int student1, int student2, double score) {
        if (student1 > positiveVotes.numRows() || student2 > positiveVotes.numCols()) {
            return;
        }
        if (score >= 0) {
            double currentVote = positiveVotes.get(student1, student2);
            positiveVotes.set(student1, student2, currentVote + score);
        } else {
            double currentVote = negativeVotes.get(student1, student2);
            negativeVotes.set(student1, student2, currentVote + (score * -1));
        }
    }

    public SimpleMatrix getPositive() {
        return positiveVotes;
    }

    public void setPositive(SimpleMatrix m) {
        positiveVotes = m;
        studentCount = m.numRows();
    }

    public SimpleMatrix getNegative() {
        return negativeVotes;
    }

    public void setNegative(SimpleMatrix m) {
        negativeVotes = m;
        studentCount = m.numRows();
    }

    public int getStudentCount() {
        return studentCount;
    }
}

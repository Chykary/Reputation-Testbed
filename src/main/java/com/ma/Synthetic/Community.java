package com.ma.Synthetic;

import com.ma.Misc.Helpers;
import com.ma.Misc.ReputationData;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Marco on 22.03.2016.
 */
public class Community {
    Statistics stats;
    private int scoreForGoodVote = 1;
    private int scoreForBadVote = -1;
    private int maxTopStudents = 5;
    private int maxFlopStudents = 5;
    private SimpleMatrix lastReputation;
    private int deferSocialInfluenceRoundCount = 20;
    private List<Integer> currentTopStudents;
    private List<Integer> currentFlopStudents;
    private ArrayList<Artifact> artifacts = new ArrayList<>();
    private ArrayList<Student> students = new ArrayList<>();
    private ReputationData currentVotings;
    private int iterationCount;
    private int[] actionSequence;

    public void step(SimpleMatrix lastReputation) {
        if (currentVotings == null) {
            System.out.println("Cannot make a step, community is not initialised.");
        } else {
            this.lastReputation = lastReputation;
            currentTopStudents = Helpers.findNMaxIndizes(lastReputation, maxTopStudents);
            currentFlopStudents = Helpers.findNMinIndizes(lastReputation, maxFlopStudents);


            for (int i = 0; i < actionSequence.length; i++) {
                students.get(actionSequence[i]).act();
            }
            iterationCount++;
            Helpers.shuffleArray(actionSequence);
        }
    }

    public void initialise() {
        currentVotings = new ReputationData(students.size());
        iterationCount = 0;
        actionSequence = new int[students.size()];
        for (int i = 0; i < actionSequence.length; i++) {
            actionSequence[i] = i;
        }
        Helpers.shuffleArray(actionSequence);
        stats = new Statistics(getCurrentLabelSet());
    }

    public void addUser(String label, double activity, double productivity, double maliciousness,
                        double hostility, double influenceability, double positivity,
                        double competence, double loyality, double socialPositivity, double variance, List<String> affiliations, List<String> repulsions, List<String> ownGroups, int count) {
        currentVotings = null;
        for (int i = 0; i < count; i++) {
            students.add(new Student(students.size(), this, label, activity, productivity, maliciousness, hostility, influenceability, positivity, competence, loyality, socialPositivity, variance, affiliations, repulsions, ownGroups));
        }
    }

    public void addArtifact(Artifact a, String label) {
        artifacts.add(a);
        stats.post(a.isGood(), label);

    }

    public ArrayList<Artifact> getArtifacts() {
        return artifacts;
    }

    public boolean isRepulsed(int idx, List<String> repulsed) {
        Student u = findUser(idx);
        for (String s : repulsed) {
            if (u.getOwnGroups().contains(s)) {
                return true;
            }
        }
        return false;
    }

    public boolean isFriend(int idx, List<String> affiliations) {
        //basically performs just a "belongs to group"-check anyway
        return isRepulsed(idx, affiliations);
    }

    public void vote(int from, Artifact a, boolean good, String label) {

        if (good) {
            currentVotings.vote(from, a.getFrom(), scoreForGoodVote);
        } else {
            currentVotings.vote(from, a.getFrom(), scoreForBadVote);
        }

        if (good) {
            if (a.isGood()) {
                stats.vote(true, true, label);
            } else {
                stats.vote(true, false, label);
            }
        } else {
            if (a.isGood()) {
                stats.vote(false, false, label);
            } else {
                stats.vote(false, true, label);
            }
        }
    }

    public Student findUser(int idx) {
        for (Student u : students) {
            if (u.getIndex() == idx) {
                return u;
            }
        }
        System.out.println("Error! Student could not be found, unstable state!");
        return null;
    }

    public List<Artifact> getTopStudentArtifacts() {

        if (iterationCount > deferSocialInfluenceRoundCount) {
            ArrayList<Artifact> topArtifacts = new ArrayList<>();
            for (Artifact a : artifacts) {
                if (currentTopStudents.contains(a.getFrom())) {
                    topArtifacts.add(a);
                }
            }
            return topArtifacts;
        }
        return artifacts;
    }

    public List<Artifact> getFlopStudentArtifacts() {
        ArrayList<Artifact> flopArtifacts = new ArrayList<>();
        for (Artifact a : artifacts) {
            if (currentFlopStudents.contains(a.getFrom())) {
                flopArtifacts.add(a);
            }
        }
        return flopArtifacts;
    }

    public int getMaxTopStudents() {
        return maxTopStudents;
    }

    public void setMaxTopStudents(int maxTopStudents) {
        this.maxTopStudents = maxTopStudents;
    }

    public int getMaxFlopStudents() {
        return maxFlopStudents;
    }

    public void setMaxFlopStudents(int maxFlopStudents) {
        this.maxFlopStudents = maxFlopStudents;
    }

    public int getScoreForGoodVote() {
        return scoreForGoodVote;
    }

    public void setScoreForGoodVote(int scoreForGoodVote) {
        this.scoreForGoodVote = scoreForGoodVote;
    }

    public int getScoreForBadVote() {
        return scoreForBadVote;
    }

    public void setScoreForBadVote(int scoreForBadVote) {
        this.scoreForBadVote = scoreForBadVote;
    }

    public int getSize() {
        return students.size();
    }

    public int getVotes() {
        return stats.getVotes();
    }

    public int getDeferSocialInfluenceRoundCount() {
        return deferSocialInfluenceRoundCount;
    }

    public void setDeferSocialInfluenceRoundCount(int deferSocialInfluenceRoundCount) {
        this.deferSocialInfluenceRoundCount = deferSocialInfluenceRoundCount;
    }

    public boolean isNotSocialDeferred() {
        return iterationCount > deferSocialInfluenceRoundCount;
    }

    public ReputationData getCurrentVotings() {
        return currentVotings;
    }

    public Statistics getStats() {
        return stats;
    }

    public Set<String> getCurrentLabelSet() {
        HashSet<String> labels = new HashSet<>();
        String[] labelArr = getLabels();
        for (String s : labelArr) {
            labels.add(s);
        }
        return labels;
    }

    public String[] getLabels() {
        String[] labels = new String[students.size()];
        for (int i = 0; i < students.size(); i++) {
            labels[i] = students.get(i).getLabel();
        }
        return labels;
    }

    public Community clone() {
        Community c = new Community();
        for (Student u : students) {
            c.addUser(u.getLabel(), u.getActivity(), u.getProductivity(), u.getMaliciousness(), u.getHostility(), u.getInfluenceability(), u.getPositivity(), u.getCompetence(), u.getLoyalty(), u.getSocialPositivity(), 0, u.getAffiliations(), u.getRepulsions(), u.getOwnGroups(), 1);
        }
        c.initialise();
        c.setScoreForBadVote(this.getScoreForBadVote());
        c.setScoreForGoodVote(this.getScoreForGoodVote());
        c.setMaxFlopStudents(this.getMaxFlopStudents());
        c.setMaxTopStudents(this.getMaxTopStudents());
        c.setDeferSocialInfluenceRoundCount(this.getDeferSocialInfluenceRoundCount());
        return c;
    }
}

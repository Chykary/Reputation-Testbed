package com.ma.Synthetic;

import com.ma.Misc.Helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marco on 22.03.2016.
 */
public class Student {
    private final List<String> affiliations;
    private final List<String> repulsions;
    private final List<String> ownGroups;

    private int index;
    private double activity;
    private double productivity;
    private double maliciousness;
    private double hostility;
    private double influenceability;
    private double positivity;
    private double competence;
    private double loyalty;
    private double socialPositivity;
    private Community myCommunity;

    private String label;

    //State
    private boolean currentlyLoyal;

    public Student(int idx, Community community, String label, double activity, double productivity, double maliciousness,
                   double hostility, double influenceability, double positivity,
                   double competence, double loyalty, double socialPositivity, double variance, List<String> affiliations, List<String> repulsions, List<String> ownGroups) {
        this.activity = activity + Helpers.getRandomDouble(variance * -1, variance);
        this.productivity = productivity + Helpers.getRandomDouble(variance * -1, variance);
        this.maliciousness = maliciousness + Helpers.getRandomDouble(variance * -1, variance);
        this.hostility = hostility + Helpers.getRandomDouble(variance * -1, variance);
        this.influenceability = influenceability + Helpers.getRandomDouble(variance * -1, variance);
        this.positivity = positivity + Helpers.getRandomDouble(variance * -1, variance);
        this.competence = competence + Helpers.getRandomDouble(variance * -1, variance);
        this.loyalty = loyalty + Helpers.getRandomDouble(variance * -1, variance);
        this.socialPositivity = socialPositivity + Helpers.getRandomDouble(variance * -1, variance);
        this.myCommunity = community;
        this.affiliations = affiliations;
        this.repulsions = repulsions;
        this.ownGroups = ownGroups;
        this.index = idx;
        this.label = label;
        sanityCheck();
    }

    public void act() {
        currentlyLoyal = false;

        if (doIt(activity)) {
            voteOrPost();
        }
        //Do nothing
    }

    private void voteOrPost() {
        if (doIt(productivity)) {
            postMaliciousOrNot();
        } else {
            voteMaliciousOrNot();
        }
    }

    private void postMaliciousOrNot() {
        if (doIt(maliciousness)) {
            gainMaliciousTrust();
        } else {
            postQualityArtifact();
        }
    }

    private void postQualityArtifact() {
        if (doIt(competence)) {
            postGoodArtifact();
        } else {
            postBadArtifact();
        }
    }

    private void gainMaliciousTrust() {
        if (doIt(positivity)) {
            postQualityArtifact();
        } else {
            postBadArtifact();
        }
    }

    private void voteMaliciousOrNot() {
        if (doIt(maliciousness)) {
            voteMaliciousButLoyalOrNot();
        } else {
            sociallyInfluencedOrNot();
        }
    }

    private void sociallyInfluencedOrNot() {
        if (doIt(influenceability) && myCommunity.isNotSocialDeferred()) {
            positiveSociallyInfluencedDecisionOrNot();
        } else {
            loyalActOrNot();
        }
    }

    private void loyalActOrNot() {
        if (doIt(loyalty)) {
            voteFriendGood();
        } else {
            attemptGoodVoteOrNot();
        }
    }

    private void attemptGoodVoteOrNot() {
        if (doIt(positivity)) {
            attemptJustifiedGoodVote();
        } else {
            attemptJustifiedBadVote();
        }
    }

    private void attemptJustifiedGoodVote() {
        if (doIt(competence)) {
            voteGoodGood();
        } else {
            voteAnyGood();
        }
    }

    private void attemptJustifiedBadVote() {
        if (doIt(competence)) {
            voteBadBad();
        } else {
            voteAnyBad();
        }
    }

    private void voteFriendGood() {
        List<Artifact> artifacts = myCommunity.getArtifacts();
        ArrayList<Artifact> candidates = new ArrayList<>();

        for (Artifact a : artifacts) {
            if (voteImpossible(a)) {
                continue;
            }
            if (myCommunity.isFriend(a.getFrom(), this.affiliations)) {
                candidates.add(a);
            }
        }
        if (candidates.size() > 0) {
            int targetIdx = Helpers.getRandomInt(candidates.size());
            Artifact a = candidates.get(targetIdx);
            voteGood(a);
        }
    }

    private void positiveSociallyInfluencedDecisionOrNot() {
        if (doIt(socialPositivity)) {
            praiseGoodStudent();
        } else {
            punishBadStudent();
        }
    }

    private void punishBadStudent() {
        List<Artifact> initialCandidates = myCommunity.getFlopStudentArtifacts();
        List<Artifact> candidates = filterImpossible(initialCandidates);
        if (candidates.size() > 0) {
            int targetIdx = Helpers.getRandomInt(candidates.size());
            voteBad(candidates.get(targetIdx));
        }
    }

    private void praiseGoodStudent() {
        List<Artifact> initialCandidates = myCommunity.getTopStudentArtifacts();
        List<Artifact> candidates = filterImpossible(initialCandidates);
        if (candidates.size() > 0) {
            int targetIdx = Helpers.getRandomInt(candidates.size());
            voteGood(candidates.get(targetIdx));
        }
    }

    private void voteMaliciousButLoyalOrNot() {
        if (doIt(loyalty)) {
            currentlyLoyal = true;
            specificTargetOrNot();
        } else {
            specificTargetOrNot();
        }
    }

    private void specificTargetOrNot() {
        if (doIt(hostility)) {
            voteAnyOfRepulsedBad();
        } else {
            positiveChaosOrNot();
        }
    }

    private void voteAnyOfRepulsedBad() {
        List<Artifact> artifacts = myCommunity.getArtifacts();
        ArrayList<Artifact> candidates = new ArrayList<>();

        for (Artifact a : artifacts) {
            if (voteImpossible(a)) {
                continue;
            }
            if (myCommunity.isRepulsed(a.getFrom(), this.repulsions)) {
                candidates.add(a);
            }
        }
        if (candidates.size() > 0) {
            int targetIdx = Helpers.getRandomInt(candidates.size());
            Artifact a = candidates.get(targetIdx);
            voteBad(a);
        }
    }

    private void positiveChaosOrNot() {
        if (doIt(positivity)) {
            voteAnyGood();
        } else {
            if (currentlyLoyal) {
                voteAnyBadButSpareFriends();
            } else {
                voteAnyBad();
            }
        }
    }

    private void voteAnyBadButSpareFriends() {
        List<Artifact> artifacts = myCommunity.getArtifacts();
        ArrayList<Artifact> candidates = new ArrayList<>();

        for (Artifact a : artifacts) {
            if (voteImpossible(a)) {
                continue;
            }
            if (myCommunity.isFriend(a.getFrom(), this.affiliations)) {
                continue;
            }
            candidates.add(a);
        }
        if (candidates.size() > 0) {
            int targetIdx = Helpers.getRandomInt(candidates.size());
            Artifact a = candidates.get(targetIdx);
            voteBad(a);
        }
    }

    private Artifact getAny() {
        List<Artifact> artifacts = myCommunity.getArtifacts();
        List<Artifact> candidates = filterImpossible(artifacts);
        if (candidates.size() > 0) {
            int targetIdx = Helpers.getRandomInt(candidates.size());
            return candidates.get(targetIdx);
        }
        return null;
    }

    private List<Artifact> filterImpossible(List<Artifact> initialCandidates) {
        ArrayList<Artifact> candidates = new ArrayList<>();
        for (Artifact a : initialCandidates) {
            if (voteImpossible(a)) {
                continue;
            }
            candidates.add(a);
        }
        return candidates;
    }

    private void voteAnyGood() {
        Artifact a = getAny();
        if (a != null) {
            voteGood(a);
        }
    }

    private void voteGoodGood() {
        List<Artifact> artifacts = filterImpossible(myCommunity.getArtifacts());
        List<Artifact> candidates = new ArrayList<>();

        for (Artifact a : artifacts) {
            if (a.isGood()) {
                candidates.add(a);
            }
        }

        if (candidates.size() > 0) {
            int targetIdx = Helpers.getRandomInt(candidates.size());
            voteGood(candidates.get(targetIdx));
        }
    }

    private void voteBadBad() {
        List<Artifact> artifacts = filterImpossible(myCommunity.getArtifacts());
        List<Artifact> candidates = new ArrayList<>();

        for (Artifact a : artifacts) {
            if (!a.isGood()) {
                candidates.add(a);
            }
        }

        if (candidates.size() > 0) {
            int targetIdx = Helpers.getRandomInt(candidates.size());
            voteBad(candidates.get(targetIdx));
        }
    }

    private void voteAnyBad() {
        Artifact a = getAny();
        if (a != null) {
            voteBad(a);
        }
    }

    private void postBadArtifact() {
        myCommunity.addArtifact(new Artifact(index, false), label);
    }

    private void postGoodArtifact() {
        myCommunity.addArtifact(new Artifact(index, true), label);
    }

    private void voteBad(Artifact a) {
        a.vote(this.index);
        myCommunity.vote(this.index, a, false, label);
    }

    private void voteGood(Artifact a) {
        a.vote(this.index);
        myCommunity.vote(this.index, a, true, label);
    }

    private boolean voteImpossible(Artifact a) {
        if (a.getFrom() == this.index) {
            return true;
        }
        if (a.hasVoted(index)) {
            return true;
        }
        return false;
    }

    private void sanityCheck() {
        if (activity > 1.0d) {
            activity = 1.0d;
        }
        if (productivity > 1.0d) {
            productivity = 1.0d;
        }
        if (maliciousness > 1.0d) {
            maliciousness = 1.0d;
        }
        if (hostility > 1.0d) {
            hostility = 1.0d;
        }
        if (influenceability > 1.0d) {
            influenceability = 1.0d;
        }
        if (positivity > 1.0d) {
            positivity = 1.0d;
        }
        if (competence > 1.0d) {
            competence = 1.0d;
        }
        if (loyalty > 1.0d) {
            loyalty = 1.0d;
        }
        if (socialPositivity > 1.0d) {
            socialPositivity = 1.0d;
        }

        if (activity < 0) {
            activity = 0;
        }
        if (productivity < 0) {
            productivity = 0;
        }
        if (maliciousness < 0) {
            maliciousness = 0;
        }
        if (hostility < 0) {
            hostility = 0;
        }
        if (influenceability < 0) {
            influenceability = 0;
        }
        if (positivity < 0) {
            positivity = 0;
        }
        if (competence < 0) {
            competence = 0;
        }
        if (loyalty < 0) {
            loyalty = 0;
        }
        if (socialPositivity < 0) {
            socialPositivity = 0;
        }
    }

    public double getActivity() {
        return activity;
    }

    public void setActivity(double activity) {
        this.activity = activity;
        sanityCheck();
    }

    public double getProductivity() {
        return productivity;
    }

    public void setProductivity(double productivity) {
        this.productivity = productivity;
        sanityCheck();
    }

    public double getMaliciousness() {
        return maliciousness;
    }

    public void setMaliciousness(double maliciousness) {
        this.maliciousness = maliciousness;
        sanityCheck();
    }

    public double getHostility() {
        return hostility;
    }

    public void setHostility(double hostility) {
        this.hostility = hostility;
        sanityCheck();
    }

    public double getInfluenceability() {
        return influenceability;
    }

    public void setInfluenceability(double influenceability) {
        this.influenceability = influenceability;
        sanityCheck();
    }

    public double getPositivity() {
        return positivity;
    }

    public void setPositivity(double positivity) {
        this.positivity = positivity;
        sanityCheck();
    }

    public double getCompetence() {
        return competence;
    }

    public void setCompetence(double competence) {
        this.competence = competence;
        sanityCheck();
    }

    public double getLoyalty() {
        return loyalty;
    }

    public void setLoyalty(double loyalty) {
        this.loyalty = loyalty;
        sanityCheck();
    }

    public double getSocialPositivity() {
        return socialPositivity;
    }

    public void setSocialPositivity(double socialPositivity) {
        this.socialPositivity = socialPositivity;
        sanityCheck();
    }

    public Community getMyCommunity() {
        return myCommunity;
    }

    public void setMyCommunity(Community myCommunity) {
        this.myCommunity = myCommunity;
    }

    public List<String> getAffiliations() {
        return affiliations;
    }

    public List<String> getRepulsions() {
        return repulsions;
    }

    public List<String> getOwnGroups() {
        return ownGroups;
    }

    public int getIndex() {
        return index;
    }

    public String getLabel() {
        return label;
    }

    public boolean doIt(double chance) {
        return Helpers.doIt(chance);
    }
}
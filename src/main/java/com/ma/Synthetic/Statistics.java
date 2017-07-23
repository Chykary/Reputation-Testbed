package com.ma.Synthetic;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Marco on 05.04.2016.
 */
public class Statistics {
    private int votes = 0;
    private int goodVotes = 0;
    private int badVotes = 0;

    private int posts = 0;
    private int goodPosts = 0;
    private int badPosts = 0;

    private HashMap<String, Integer> labeledGoodPosts = new HashMap<>();
    private HashMap<String, Integer> labeledBadPosts = new HashMap<>();

    private HashMap<String, Integer> labeledJustifiedGoodVotes = new HashMap<>();
    private HashMap<String, Integer> labeledJustifiedBadVotes = new HashMap<>();
    private HashMap<String, Integer> labeledUnjustifiedGoodVotes = new HashMap<>();
    private HashMap<String, Integer> labeledUnjustifiedBadVotes = new HashMap<>();

    public Statistics(Set<String> labels) {
        for (String s : labels) {
            labeledGoodPosts.put(s, 0);
            labeledBadPosts.put(s, 0);
            labeledJustifiedGoodVotes.put(s, 0);
            labeledJustifiedBadVotes.put(s, 0);
            labeledUnjustifiedGoodVotes.put(s, 0);
            labeledUnjustifiedBadVotes.put(s, 0);
        }
    }

    public void vote(boolean positive, boolean justified, String label) {
        votes++;
        if (positive) {
            goodVotes++;
            if (justified) {
                labeledJustifiedGoodVotes.put(label, labeledJustifiedGoodVotes.get(label) + 1);
            } else {
                labeledUnjustifiedGoodVotes.put(label, labeledUnjustifiedGoodVotes.get(label) + 1);
            }
        } else {
            badVotes++;
            if (justified) {
                labeledJustifiedBadVotes.put(label, labeledJustifiedBadVotes.get(label) + 1);
            } else {
                labeledUnjustifiedBadVotes.put(label, labeledUnjustifiedBadVotes.get(label) + 1);
            }
        }
    }

    public void post(boolean goodQuality, String label) {
        posts++;
        if (goodQuality) {
            goodPosts++;
            labeledGoodPosts.put(label, labeledGoodPosts.get(label) + 1);
        } else {
            badPosts++;
            labeledBadPosts.put(label, labeledBadPosts.get(label) + 1);
        }
    }

    public void print() {
        System.out.println(votes + " votes, " + goodVotes + " positive votes, " + badVotes + " negative votes. (" + String.format("%.2f", ((double) goodVotes / (double) votes) * 100) + "% positive)");
        System.out.println(posts + " posts, " + goodPosts + " quality posts, " + badPosts + " bad posts. (" + String.format("%.2f", (((double) goodPosts / (double) posts) * 100)) + "% positive)");

        System.out.println("#### Posts by label ####");
        for (Map.Entry<String, Integer> e : labeledGoodPosts.entrySet()) {
            int badPostCount = labeledBadPosts.get(e.getKey());
            System.out.println(e.getKey() + ": " + e.getValue() + " quality posts, " + badPostCount + " bad posts. (" + String.format("%.2f", (((double) e.getValue() / (double) (e.getValue() + badPostCount)) * 100)) + "% positive)");
        }

        System.out.println("#### Votes by label ####");

        for (Map.Entry<String, Integer> e : labeledJustifiedGoodVotes.entrySet()) {
            int goodUnjustifiedVotes = labeledUnjustifiedGoodVotes.get(e.getKey());
            int badJustifiedVotes = labeledJustifiedBadVotes.get(e.getKey());
            int badUnjustifiedVotes = labeledUnjustifiedBadVotes.get(e.getKey());

            System.out.println(e.getKey() + ": True Good: " + e.getValue() + ", False Good: " + goodUnjustifiedVotes + ", True Bad: " + badJustifiedVotes + ", False Bad: " + badUnjustifiedVotes);
        }
    }

    public int getVotes() {
        return votes;
    }

    public int getGoodVotes() {
        return goodVotes;
    }

    public int getBadVotes() {
        return badVotes;
    }

    public int getPosts() {
        return posts;
    }

    public int getGoodPosts() {
        return goodPosts;
    }

    public int getBadPosts() {
        return badPosts;
    }

    public HashMap<String, Integer> getLabeledGoodPosts() {
        return labeledGoodPosts;
    }

    public HashMap<String, Integer> getLabeledBadPosts() {
        return labeledBadPosts;
    }

    public HashMap<String, Integer> getLabeledJustifiedGoodVotes() {
        return labeledJustifiedGoodVotes;
    }

    public HashMap<String, Integer> getLabeledJustifiedBadVotes() {
        return labeledJustifiedBadVotes;
    }

    public HashMap<String, Integer> getLabeledUnjustifiedGoodVotes() {
        return labeledUnjustifiedGoodVotes;
    }

    public HashMap<String, Integer> getLabeledUnjustifiedBadVotes() {
        return labeledUnjustifiedBadVotes;
    }
}

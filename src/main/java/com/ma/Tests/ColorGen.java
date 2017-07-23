package com.ma.Tests;

import com.ma.Outputter.ChartOutputter;
import com.ma.Outputter.LabelColorPair;
import com.ma.ReputationAlgorithms.Aggregate.AggregateAlgorithm;
import com.ma.Scheduler.Scheduler;
import com.ma.Synthetic.Community;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Marco on 28.06.2016.
 */
public class ColorGen extends Test {

    public void run() {
        name = "ColorGen";

        ArrayList<LabelColorPair> lcp = new ArrayList<>();
        lcp.add(new LabelColorPair("a", Color.cyan));
        lcp.add(new LabelColorPair("b", Color.yellow));
        lcp.add(new LabelColorPair("c", Color.green));
        lcp.add(new LabelColorPair("d", Color.red));
        lcp.add(new LabelColorPair("e", Color.MAGENTA));

        ChartOutputter cout = new ChartOutputter(lcp);

        cout.setPrePath(getPrePath());


        Scheduler scheduler = new Scheduler();

        scheduler.addOutputter(cout);


        Community c = new Community();

        c.addUser("a", 1, 0.2, 0,
                0, 0, 0.5,
                0.5, 0, 0, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 17);

        c.addUser("b", 1, 0.2, 0,
                0, 0, 0.5,
                0.6, 0, 0, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 20);


        c.addUser("c", 1, 0.2, 0,
                0, 0, 0.5,
                0.7, 0, 0, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 25);

        c.addUser("d", 1, 0.2, 0,
                0, 0, 0.5,
                0.8, 0, 0, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 30);

        c.addUser("e", 1, 0.2, 0,
                0, 0, 0.5,
                0.9, 0, 0, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 18);

        scheduler.scheduleSynthethicAveraged(c, 100, 100, 50, new AggregateAlgorithm());
    }
}

package com.ma.Tests;

import com.ma.Outputter.LabelColorPair;
import com.ma.Outputter.QualityMeasureOutputter;
import com.ma.Scheduler.Scheduler;
import com.ma.Synthetic.Community;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Marco on 28.06.2016.
 */
public class CompetenceConvergenceTest extends Test {

    public void run() {
        name = "CompetenceConvergenceTest";

        ArrayList<LabelColorPair> lcp = new ArrayList<>();
        lcp.add(new LabelColorPair("low", Color.red));
        lcp.add(new LabelColorPair("average", Color.yellow));
        lcp.add(new LabelColorPair("high", Color.green));

        QualityMeasureOutputter qmo = new QualityMeasureOutputter(lcp);
        qmo.setPrePath(getPrePath());

        Scheduler scheduler = new Scheduler();
        Community c = new Community();


        c.addUser("average", 1, 0.2, 0,
                0, 0, 0.5,
                0.7, 0, 0, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 10);


        c.addUser("high", 1, 0.2, 0,
                0, 0, 0.5,
                0.9, 0, 0, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 10);

        c.addUser("low", 1, 0.2, 0,
                0, 0, 0.5,
                0.5, 0, 0, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 10);

        scheduler.scheduleSynthethicAveragedConvergenceAll(c, 600, 10, 300, qmo);
    }
}

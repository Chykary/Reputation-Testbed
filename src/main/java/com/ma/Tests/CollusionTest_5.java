package com.ma.Tests;

import com.ma.Outputter.ChartOutputter;
import com.ma.Outputter.LabelColorPair;
import com.ma.Outputter.QualityMeasureOutputter;
import com.ma.Scheduler.Scheduler;
import com.ma.Synthetic.Community;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Marco on 28.06.2016.
 */
public class CollusionTest_5 extends Test {

    public void run() {
        name = "CollusionTest_5";

        ArrayList<LabelColorPair> lcp = new ArrayList<>();
        lcp.add(new LabelColorPair("mal", Color.red));
        lcp.add(new LabelColorPair("normal", Color.green));

        ChartOutputter cout = new ChartOutputter(lcp);
        QualityMeasureOutputter qmo = new QualityMeasureOutputter(lcp);

        cout.setPrePath(getPrePath());
        qmo.setPrePath(getPrePath());

        Scheduler scheduler = new Scheduler();
        scheduler.addOutputter(qmo);
        scheduler.addOutputter(cout);

        Community c = new Community();

        c.addUser("normal", 1.0, 0.3, 0,
                0, 0, 0.5,
                0.8, 0, 0, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 25);

        c.addUser("mal", 1.0, 0, 1.0,
                0, 0, 0,
                0.8, 0, 0, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 5);

        scheduler.scheduleSynthethicAveragedAll(c, 100, 100, 2000);
    }
}

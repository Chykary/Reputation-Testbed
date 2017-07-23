package com.ma.Tests;

import com.ma.Outputter.CSVOutputter;
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
public class IndividualTest extends Test {

    public void run() {
        name = "IndividualTest";

        ArrayList<LabelColorPair> lcp = new ArrayList<>();
        lcp.add(new LabelColorPair("a", Color.cyan));
        lcp.add(new LabelColorPair("b", Color.cyan));
        lcp.add(new LabelColorPair("c", Color.cyan));
        lcp.add(new LabelColorPair("d", Color.cyan));
        lcp.add(new LabelColorPair("e", Color.cyan));
        lcp.add(new LabelColorPair("f", Color.cyan));
        lcp.add(new LabelColorPair("g", Color.cyan));


        ChartOutputter cout = new ChartOutputter(lcp);
        QualityMeasureOutputter qmo = new QualityMeasureOutputter(lcp);
        CSVOutputter csvo = new CSVOutputter();

        cout.setPrePath(getPrePath());
        qmo.setPrePath(getPrePath());
        csvo.setPrePath(getPrePath());

        Scheduler scheduler = new Scheduler();
        scheduler.addOutputter(qmo);
        scheduler.addOutputter(cout);
        scheduler.addOutputter(csvo);

        Community c = new Community();

        c.addUser("a", 1, 0.2, 0,
                0, 0, 0.5,
                0.5, 0, 0, 1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 1);

        c.addUser("b", 1, 0.2, 0,
                0, 0, 0.5,
                0.5, 0, 0, 1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 1);
        c.addUser("c", 1, 0.2, 0,
                0, 0, 0.5,
                0.5, 0, 0, 1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 1);
        c.addUser("d", 1, 0.2, 0,
                0, 0, 0.5,
                0.5, 0, 0, 1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 1);
        c.addUser("e", 1, 0.2, 0,
                0, 0, 0.5,
                0.5, 0, 0, 1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 1);
        c.addUser("f", 1, 0.2, 0,
                0, 0, 0.5,
                0.5, 0, 0, 1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 1);
        c.addUser("g", 1, 0.2, 0,
                0, 0, 0.5,
                0.5, 0, 0, 1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 1);

        scheduler.scheduleSynthethicAveragedAll(c, 100, 100, 2000);
    }
}

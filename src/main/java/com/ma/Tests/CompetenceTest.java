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
public class CompetenceTest extends Test {

    public void run() {
        name = "CompetenceTest";

        ArrayList<LabelColorPair> lcp = new ArrayList<>();
        lcp.add(new LabelColorPair("low", Color.red));
        lcp.add(new LabelColorPair("average", Color.yellow));
        lcp.add(new LabelColorPair("high", Color.green));

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

        c.addUser("low", 1, 0.2, 0,
                0, 0, 0.5,
                0.5, 0, 0, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 10);

        c.addUser("average", 1, 0.2, 0,
                0, 0, 0.5,
                0.7, 0, 0, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 10);


        c.addUser("high", 1, 0.2, 0,
                0, 0, 0.5,
                0.9, 0, 0, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 10);

        scheduler.scheduleSynthethicAveragedAll(c, 100, 100, 2000);
    }
}

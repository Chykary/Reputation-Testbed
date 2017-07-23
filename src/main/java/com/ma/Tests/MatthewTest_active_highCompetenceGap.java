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
public class MatthewTest_active_highCompetenceGap extends Test {

    public void run() {
        name = "MatthewTest_active_highCompetenceGap";

        ArrayList<LabelColorPair> lcp = new ArrayList<>();
        lcp.add(new LabelColorPair("average", Color.yellow));
        lcp.add(new LabelColorPair("top", Color.green));

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
        c.setDeferSocialInfluenceRoundCount(100);
        c.setMaxTopStudents(3);

        c.addUser("average", 1, 0.4, 0,
                0, 0.5, 0.5,
                0.5, 0, 1.0, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 20);


        c.addUser("top", 1, 0.4, 0,
                0, 0, 0.5,
                0.9, 0, 0, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 3);

        scheduler.scheduleSynthethicAveragedAll(c, 200, 200, 2000);

    }
}

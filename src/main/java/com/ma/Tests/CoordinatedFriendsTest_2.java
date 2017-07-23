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
public class CoordinatedFriendsTest_2 extends Test {

    public void run() {
        name = "CoordinatedFriendsTest_2";

        ArrayList<LabelColorPair> lcp = new ArrayList<>();
        lcp.add(new LabelColorPair("f", Color.blue));
        lcp.add(new LabelColorPair("unaffiliated", Color.cyan));

        ChartOutputter cout = new ChartOutputter(lcp);
        QualityMeasureOutputter qmo = new QualityMeasureOutputter(lcp);

        cout.setPrePath(getPrePath());
        qmo.setPrePath(getPrePath());

        Scheduler scheduler = new Scheduler();
        scheduler.addOutputter(qmo);
        scheduler.addOutputter(cout);

        Community c = new Community();

        ArrayList<String> friendGroup = new ArrayList<>();
        friendGroup.add("f");

        c.addUser("unaffiliated", 1, 0.3, 0,
                0, 0, 0.5,
                0.9, 0, 0, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 25);

        c.addUser("f", 1, 0.3, 0,
                0, 0, 0.5,
                0.2, 1.0, 0, 0, friendGroup, new ArrayList<>(), friendGroup, 2);

        scheduler.scheduleSynthethicAveragedAll(c, 100, 100, 2000);
    }
}

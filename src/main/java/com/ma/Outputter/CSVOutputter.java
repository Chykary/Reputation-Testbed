package com.ma.Outputter;

import org.ejml.simple.SimpleMatrix;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Created by Marco on 18.01.2016.
 */
public class CSVOutputter extends Outputter {

    String separator = ";";

    public void save(String path, SimpleMatrix reputations, String[] mapping, String[] labels, String time) {
        initPrePath();
        path = getPrePath() + path;
        if (reputations.getNumElements() != mapping.length) {
            System.out.println("Could not save file: Mapping length does not match vector length.");
            return;
        }

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(path + ".csv"));
            for (int i = 0; i < mapping.length; i++) {
                out.write("\"" + mapping[i] + "\"" + separator + "\"" + labels[i] + "\"" + separator + reputations.get(i));
                out.newLine();
            }
            out.close();
        } catch (Exception e) {
            System.out.println("Failed to save file:" + e.getMessage());
        }

    }
}

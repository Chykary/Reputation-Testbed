package com.ma.MappingCreator;

/**
 * Created by Marco on 09.05.2016.
 */
public class StudentMapping extends MappingCreator {
    public String[] createMapping(int n) {
        String[] mapping = new String[n];
        for (int i = 0; i < n; i++) {
            mapping[i] = "Student " + (i + 1);
        }
        return mapping;
    }
}

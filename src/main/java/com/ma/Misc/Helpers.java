package com.ma.Misc;

import com.ma.Outputter.LabelColorPair;
import com.ma.Outputter.LabelCountPair;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Marco on 08.12.2015.
 */
public class Helpers {
    public static Random random = new Random();

    static public SimpleMatrix makeRowStochastic(SimpleMatrix m) {
        SimpleMatrix result = new SimpleMatrix(m.numRows(), m.numCols());
        for (int i = 0; i < m.numRows(); i++) {
            double total = 0;
            for (int j = 0; j < m.numCols(); j++) {
                total += m.get(i, j);
            }
            if (total == 0) {
                double val = 1 / ((double) m.numRows() - 1);
                for (int j = 0; j < m.numCols(); j++) {
                    if (i != j) {
                        result.set(i, j, val);
                    }
                }
            } else {
                for (int j = 0; j < m.numCols(); j++) {
                    if (i != j) {
                        double val = m.get(i, j) / (total);
                        result.set(i, j, val);
                    }
                }
            }
        }
        return result;
    }

    public static void shuffleArray(int[] array) {
        int index;
        for (int i = array.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            if (index != i) {
                array[index] ^= array[i];
                array[i] ^= array[index];
                array[index] ^= array[i];
            }
        }
    }

    public static boolean containedInStringArray(String[] array, String s) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static int find(String[] array, String s) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(s)) {
                return i;
            }
        }
        return -1;
    }

    public static void cleanString(String[] array, String s) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].replace(s, "");
        }
    }

    public static double findMinimum(SimpleMatrix m) {
        double currentMinimum = Double.MAX_VALUE;
        double currentVal;
        for (int i = 0; i < m.numRows(); i++) {
            for (int j = 0; j < m.numCols(); j++) {
                currentVal = m.get(i, j);
                if (currentVal < currentMinimum) {
                    currentMinimum = currentVal;
                }
            }
        }
        return currentMinimum;
    }

    public static double findMaximum(SimpleMatrix m) {
        double currentMaximum = Double.MIN_VALUE;
        double currentVal;
        for (int i = 0; i < m.numRows(); i++) {
            for (int j = 0; j < m.numCols(); j++) {
                currentVal = m.get(i, j);
                if (currentVal > currentMaximum) {
                    currentMaximum = currentVal;
                }
            }
        }
        return currentMaximum;
    }

    public static List<Integer> findNMaxIndizes(SimpleMatrix _m, int n) {
        SimpleMatrix m = new SimpleMatrix(_m);
        ArrayList<Integer> max = new ArrayList<>();
        if (n > m.numRows()) {
            System.out.println("Cannot get max indizes, n is greater than numRows");
            return new ArrayList<Integer>();
        }

        while (max.size() < n) {
            double currentMax = Double.MIN_VALUE;
            int currentIndex = 0;
            double currentVal;
            for (int i = 0; i < m.numRows(); i++) {
                currentVal = m.get(i, 0);
                if (currentVal > currentMax) {
                    currentMax = currentVal;
                    currentIndex = i;
                }
            }
            max.add(currentIndex);
            m.set(currentIndex, 0, Double.MIN_VALUE);
        }
        return max;
    }

    public static List<Integer> findNMinIndizes(SimpleMatrix _m, int n) {
        SimpleMatrix m = new SimpleMatrix(_m);
        ArrayList<Integer> min = new ArrayList<>();
        if (n > m.numRows()) {
            System.out.println("Cannot get min indizes, n is greater than numRows");
            return new ArrayList<Integer>();
        }

        while (min.size() < n) {
            double currentMin = Double.MAX_VALUE;
            int currentIndex = 0;
            double currentVal;
            for (int i = 0; i < m.numRows(); i++) {
                currentVal = m.get(i, 0);
                if (currentVal < currentMin) {
                    currentMin = currentVal;
                    currentIndex = i;
                }
            }
            min.add(currentIndex);
            m.set(currentIndex, 0, Double.MAX_VALUE);
        }
        return min;
    }

    public static SimpleMatrix toLengthOne(SimpleMatrix m) {
        double total = 0;
        for (int i = 0; i < m.getNumElements(); i++) {
            total += m.get(i);
        }
        double current;
        for (int i = 0; i < m.getNumElements(); i++) {
            current = m.get(i);
            if (total != 0) {
                current = current / total;
            }
            m.set(i, current);
        }
        return m;
    }

    public static SimpleMatrix toLengthOneMinimumZero(SimpleMatrix m) {
        double val = findMinimum(m);
        if (val < 0) {
            val = val * -1;
            m = m.plus(val);
        }
        return toLengthOne(m);

    }

    public static SimpleMatrix powerMethod(SimpleMatrix m, int iterationCount) {
        SimpleMatrix v = new SimpleMatrix(m.numRows(), 1);
        v.set(1);
        for (int i = 0; i < iterationCount; i++) {
            v = m.mult(v);
            v = toLengthOne(v);
        }
        return v;
    }

    public static SortedReputation sort(String[] _mapping, String[] _labels, SimpleMatrix m) {
        String[] mapping = _mapping.clone();
        String[] labels = _labels.clone();
        if (mapping.length != m.getNumElements()) {
            System.out.println("Cannot sort: Length mismatch");
            return new SortedReputation(mapping, labels, m);
        }

        ArrayList<KeyValuePairWithLabel<Double, String>> mappedList = new ArrayList<>();

        for (int i = 0; i < mapping.length; i++) {
            mappedList.add(new KeyValuePairWithLabel<>(m.get(i), mapping[i], labels[i]));
        }
        Collections.sort(mappedList);
        for (int i = 0; i < mappedList.size(); i++) {
            mapping[i] = mappedList.get(i).getValue();
            m.set(i, mappedList.get(i).getKey());
            labels[i] = mappedList.get(i).getString();
        }

        return new SortedReputation(mapping, labels, m);
    }

    public static String[] getDefaultLabels(int number) {
        String[] labels = new String[number];
        for (int i = 0; i < number; i++) {
            labels[i] = "manual";
        }
        return labels;
    }

    public static double sumNorm(SimpleMatrix m) {
        double val = 0;
        for (int i = 0; i < m.numRows(); i++) {
            for (int j = 0; j < m.numCols(); j++) {
                val += Math.abs(m.get(i, j));
            }
        }
        return val;
    }

    public static String[] inverseArray(String[] arr) {
        for (int i = 0; i < arr.length / 2; i++) {
            String temp = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = temp;
        }
        return arr;
    }

    public static List<LabelCountPair> getLabelCountPairFromMatrixAndColorPair(List<LabelColorPair> lcp, String[] labels) {
        List<LabelCountPair> labelCountPair = new ArrayList<>();
        for (LabelColorPair slcp : lcp) {
            int count = 0;
            for (int i = 0; i < labels.length; i++) {
                if (labels[i].equals(slcp.getLabel())) {
                    count++;
                }
            }
            if (count != 0) {
                labelCountPair.add(new LabelCountPair(slcp.getLabel(), count));
            }
        }
        return labelCountPair;
    }

    public static boolean doIt(double chance) {
        double d = random.nextDouble();
        if (d <= chance) {
            return true;
        }
        return false;
    }

    public static double getRandomDouble(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    public static int getRandomInt(int max) {
        return random.nextInt(max);
    }


}

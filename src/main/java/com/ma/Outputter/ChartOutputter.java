package com.ma.Outputter;

import com.ma.Misc.Helpers;
import org.ejml.simple.SimpleMatrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Marco on 09.05.2016.
 */
public class ChartOutputter extends Outputter {
    List<LabelColorPair> lcp;
    double width = 1920;
    double height = 1080;

    public ChartOutputter(List<LabelColorPair> lcp) {
        this.lcp = lcp;
    }

    public void save(String path, SimpleMatrix reputations, String[] mapping, String[] labels, String time) {
        initPrePath();
        path = getPrePath() + path;
        DecimalFormat df = new DecimalFormat("0.0000");
        BufferedImage img = new BufferedImage(rint(width), rint(height), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        double minRep = Helpers.findMinimum(reputations);
        double maxRep = Helpers.findMaximum(reputations);

        double chartX = 0.05;
        double chartY = 0.05;
        double totalChartY = 0.9;

        double barShare = 0.9d * (1.0d / (double) labels.length);
        double barDistance = barShare * 0.1;
        barShare = barShare - barDistance;
        double smallDistanceWidth = 0.005 * width;
        double smallDistanceHeight = 0.025 * height;

        int coverage = rint(width * 0.05);

        for (int i = 0; i < lcp.size(); i++) {
            LabelColorPair currentLCP = lcp.get(i);
            int count = getLabelCount(labels, currentLCP.getLabel());

            double share = count * (barShare + barDistance);

            g.setColor(getLessSaturated(currentLCP.getColor()));
            int y = rint(height * chartY);
            int sWidth = rint(share * width);
            int sHeight = rint(totalChartY * height);
            g.fillRect(coverage, y, sWidth, sHeight);
            g.setColor(Color.black);

            g.setFont(new Font("Dialog", Font.PLAIN, rint(height / 17)));
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.drawString(currentLCP.getLabel() + (" (n=" + count + ")"), rint(coverage + smallDistanceWidth), rint(chartY * height + smallDistanceHeight * 2));
            coverage += sWidth;
        }


        g.setColor(Color.black);
        g.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        g.drawLine(rint(width * 0.045), rint(height * 0.15), coverage, rint(height * 0.15));
        g.drawLine(rint(width * 0.045), rint(height * 0.325), coverage, rint(height * 0.325));
        g.drawLine(rint(width * 0.045), rint(height * 0.50), coverage, rint(height * 0.50));
        g.drawLine(rint(width * 0.045), rint(height * 0.675), coverage, rint(height * 0.675));
        g.drawLine(rint(width * 0.045), rint(height * 0.85), coverage, rint(height * 0.85));
        g.setStroke(new BasicStroke(1));


        int currentPos;
        for (int i = 0; i < labels.length; i++) {
            currentPos = rint(width * chartX) + rint(width * i * (barShare + barDistance)) + 2;
            int entryHeight = rint(height * 0.7 * ((reputations.get(i) - minRep) / (maxRep - minRep)) + (height * 0.1));
            int ey = rint(height * 0.95) - entryHeight;
            g.setColor(getColor(labels[i]));
            g.fillRect(currentPos, ey, rint(barShare * width), entryHeight);
            g.setColor(Color.black);
            g.drawRect(currentPos, ey, rint(barShare * width), entryHeight);

        }

        g.setColor(Color.black);
        g.setStroke(new BasicStroke(5));
        g.drawLine(rint(width * chartX), rint(height * chartY), rint(width * chartX), rint(height * 0.95));
        g.drawLine(rint(width * chartX), rint(height * 0.95), coverage, rint(height * 0.95));

        g.setFont(new Font("Dialog", Font.PLAIN, rint(height / 55)));
        g.drawString(df.format(maxRep), rint(width * 0.005), rint(height * (0.15 + 0.006)));
        g.drawString(df.format(minRep + (maxRep - minRep) * 0.75), rint(width * 0.005), rint(height * (0.325 + 0.006)));
        g.drawString(df.format(minRep + (maxRep - minRep) * 0.5), rint(width * 0.005), rint(height * (0.50 + 0.006)));
        g.drawString(df.format(minRep + (maxRep - minRep) * 0.25), rint(width * 0.005), rint(height * (0.675 + 0.006)));
        g.drawString(df.format(minRep), rint(width * 0.005), rint(height * (0.85 + 0.006)));

        g.drawString("Range: " + df.format(maxRep - minRep), rint(0.005 * width), rint(0.02 * height));

        try {
            if (ImageIO.write(img, "png", new File(path + ".png"))) {
                System.out.println("Chart " + path + " saved");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Color getColor(String label) {
        for (LabelColorPair l : lcp) {
            if (l.getLabel().equals(label)) {
                return l.getColor();
            }
        }
        return Color.black;
    }

    private Color getLessSaturated(Color c) {
        float[] hsv = new float[3];
        Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsv);
        hsv[1] = hsv[1] * 0.5f;
        return Color.getHSBColor(hsv[0], hsv[1], hsv[2]);
    }

    private int getLabelCount(String[] labels, String label) {
        int count = 0;
        for (int j = 0; j < labels.length; j++) {
            if (labels[j].equals(label)) {
                count++;
            }
        }
        return count;
    }

    private int rint(double x) {
        return (int) Math.round(x);
    }
}

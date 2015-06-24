/**
 * Copyright (C) 2012 SINTEF <franck.fleurey@sintef.no>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June
 * 2007; you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
/**
 * ../HEADER
 */
package org.thingml.rtcharts.swing;

import java.awt.*;
import java.text.DecimalFormat;

public abstract class GraphPanel extends AbstractGraphPanel {

    protected static int TOP_OFFSET = 20;
    protected static int BOTTOM_OFFSET = 2;

    protected GraphBuffer graphBuffer;
    protected int[] graphValues;

    //Variables with getters/setters 
    protected long sleepTime = 100;
    protected Color color = Color.RED;
    protected int ymin = 0;
    protected int ymax = 1000;
    protected int yminor = 100;
    protected int xminor = 50;
    protected DecimalFormat labelFormat = new DecimalFormat("0");
    protected double scale = 1.0;
    protected String avgValText = "";
    protected String lastValText = "";

    public int getXminor() {
        return xminor;
    }

    public void setXminor(int xminor) {
        this.xminor = xminor;
    }
    protected String name = "";

    public long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        super.setColor(color);
        this.color = color;
    }

    public int getYmin() {
        return ymin;
    }

    public void setYmin(int ymin) {
        
        if (ymin == ymax) ymin--; // Avoid divide by zero
        
        this.ymin = ymin;
        //jLabelYMin.setText("" + ymin);
        jLabelYMin.setText("" + labelFormat.format(ymin * scale));
    }

    public int getYmax() {
        return ymax;
    }

    public void setYmax(int ymax) {
        
        if (ymax == ymin) ymax++; // Avoid divide by zero
        
        this.ymax = ymax;
        //jLabelYMax.setText("" + ymax);
        jLabelYMax.setText("" + labelFormat.format(ymax * scale));
    }

    public int getYminor() {
        return yminor;
    }

    public void setYminor(int yminor) {
        this.yminor = yminor;
    }

    public double getScale() {
        return scale;
    }

    public void setScaleAndFormat(double scale, String labelFormatString) {
        this.scale = scale;
        this.labelFormat = new DecimalFormat(labelFormatString);
        jLabelYMin.setText("" + labelFormat.format(ymin * scale));
        jLabelYMax.setText("" + labelFormat.format(ymax * scale));
    }

    public DecimalFormat getLabelFormat() {
        return labelFormat;
    }

    public String getAvgValText() {
        return avgValText;
    }

    public void setAvgValText(String avgValText) {
        this.avgValText = avgValText;
    }

    public String getLastValText() {
        return lastValText;
    }

    public void setLastValText(String lastValText) {
        this.lastValText = lastValText;
    }

    /**
     * Create the panel.
     */
    public GraphPanel(GraphBuffer buffer, String name, int ymin, int ymax, int yminor, Color color) {
        super();
        this.graphBuffer = buffer;
        this.color = color;
        this.ymin = ymin;
        this.ymax = ymax;
        this.yminor = yminor;
        this.name = name;

        this.xminor = buffer.getGraphData().length + 1; // no grid

        //jLabelYMax.setText("" + ymax);
        jLabelYMax.setText("" + labelFormat.format(ymax * scale));
        //jLabelYMin.setText("" + ymin);
        jLabelYMin.setText("" + labelFormat.format(ymin * scale));
        jLabelTitle.setText(name);

        super.setColor(color);

    }

    protected int computeX(int value) {
        return value * getWidth() / graphBuffer.getSize();
    }

    protected int computeY(int value) {
        return getHeight() - BOTTOM_OFFSET - map(value, ymin, ymax, BOTTOM_OFFSET, getHeight() - TOP_OFFSET);
    }

    final static float dash1[] = {5.0f};
    final static BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, dash1, 0.0f);

    final static Color cline = new Color(128, 128, 128);
    //final static Color cxline = new Color(90,90,90);

    protected abstract void drawData(Graphics g);

    protected void drawAxis(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        Stroke s = g2.getStroke();

        g2.setColor(cline);

        //g2.fillRect(0, 0, getWidth(), jLabelTitle.getHeight());
        g2.setStroke(new BasicStroke(2));

        // draw the 0 axis:
        if (ymin <= 0 && ymax >= 0) {
            int y0 = computeY(0);
            g.drawLine(0, y0, getWidth(), y0);
        }

        g2.setStroke(dashed);

        // g2.setStroke(new BasicStroke(1));
        for (int ypos = yminor; ypos <= ymax; ypos += yminor) {
            if (ypos >= ymin) {
                int y = computeY(ypos);
                g.drawLine(0, y, getWidth(), y);
            }
        }
        for (int ypos = -yminor; ypos >= ymin; ypos -= yminor) {
            if (ypos <= ymax) {
                int y = computeY(ypos);
                g.drawLine(0, y, getWidth(), y);
            }
        }

        int _Ymin = computeY(ymin);
        int _Ymax = computeY(ymax);
        int xmax = graphBuffer.getSize();
        for (int xpos = 0; xpos <= xmax; xpos += xminor) {
            if (xpos <= xmax) {
                int x = computeX(xpos);
                g.drawLine(x, _Ymin, x, _Ymax);
            }
        }

        g2.setStroke(s);

    }

    int bufferYmax = 0;
    int bufferYmin = 0;
    @Override
    public void paintComponent(Graphics g) {
        if (graphValues != null){
            bufferYmax = findHighestValue();
            bufferYmin = findLowestValue();
        }
        super.paintComponent(g);
        drawAxis(g);
        drawData(g);
        //jLabelAVG.setText("" + graphBuffer.average());
        jLabelAVG.setText(avgValText + labelFormat.format(graphBuffer.average() * scale));
        //jLabelValue.setText("" + graphBuffer.last());
        jLabelValue.setText(lastValText + labelFormat.format(graphBuffer.last() * scale));
    }

    protected int findHighestValue() {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < graphValues.length; i++) {
            if (graphValues[i] > max && graphValues[i] != graphBuffer.getInvalidNumber()) {
                max = graphValues[i];
            }
        }
        return max;
    }

    protected int findLowestValue() {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < graphValues.length; i++) {
            if (graphValues[i] < min && graphValues[i] != graphBuffer.getInvalidNumber()) {
                min = graphValues[i];
            }
        }
        return min;
    }

    protected int map(int x, int in_min, int in_max, int out_min, int out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    @Override
    void fitToContent() {
//        if ( autoScale == true ) {
            if (bufferYmax != Integer.MIN_VALUE) setYmax(bufferYmax);
            if (bufferYmin != Integer.MAX_VALUE) setYmin(bufferYmin);
//        }
    }
             
    @Override
     void mouseClicked(java.awt.event.MouseEvent evt) {
        //fitToContent();
    }                                 
    
    private boolean stop = false;

    public void start() {
        new PaintManager().start();
    }

    public void stop() {
        stop = true;
    }

    public void redrawGraph() {
        if (graphBuffer != null) {
            graphValues = graphBuffer.getGraphData();
            repaint();
        }
    }
    
    protected class PaintManager extends Thread {
        public void run() {

            while (!stop) {

                try {
                    if ( autoScale == true ) {
                        if ( getYmax() < bufferYmax ) setYmax(bufferYmax);
                        if ( getYmin() > bufferYmin ) setYmin(bufferYmin);
                    }
                    redrawGraph();
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("End of paint thread.");
        }
    }
}

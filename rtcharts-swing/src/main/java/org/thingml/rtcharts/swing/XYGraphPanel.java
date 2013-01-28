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
package org.thingml.rtcharts.swing;

import java.awt.*;

public class XYGraphPanel extends AbstractXYGraphPanel {

    protected static int TOP_OFFSET = 20;
    protected static int BOTTOM_OFFSET = 2;
    protected static int LEFT_OFFSET = 0;
    protected static int RIGHT_OFFSET = 0;
    protected DataBuffer graphBuffer;
    protected int[][] graphValues;
    //Variables with getters/setters 
    protected long sleepTime = 100;
    protected Color color = Color.RED;
    protected int ymin = 0;
    protected int ymax = 1000;
    protected int xmin = 0;
    protected int xmax = 1000;
    protected int yminor = 100;
    protected int xminor = 50;

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
        this.color = color;
        jLabelXAVG.setForeground(color);
        jLabelYAVG.setForeground(color);
        jLabelTitle.setForeground(color);
    }

    public int getYmin() {
        return ymin;
    }

    public void setYmin(int ymin) {
        this.ymin = ymin;
        jLabelXYMin.setText("" + ymin);
    }

    public int getYmax() {
        return ymax;
    }

    public void setYmax(int ymax) {
        this.ymax = ymax;
        jLabelYMax.setText("" + ymax);
    }

    public int getYminor() {
        return yminor;
    }

    public void setYminor(int yminor) {
        this.yminor = yminor;
    }

    /**
     * Create the panel.
     */
    public XYGraphPanel(DataBuffer buffer, String name, int xmin, int xmax, int xminor, int ymin, int ymax, int yminor, Color color) {
        super();
        this.graphBuffer = buffer;
        this.color = color;
        this.ymin = ymin;
        this.ymax = ymax;
        this.yminor = yminor;
        this.xmin = xmin;
        this.xmax = xmax;
        this.xminor = xminor;
        this.name = name;

        jLabelYMax.setText("" + ymax);
        jLabelXYMin.setText("" + ymin);

        jLabelTitle.setText(name);

        jLabelXAVG.setForeground(color);
        jLabelYAVG.setForeground(color);
        jLabelTitle.setForeground(color);
    }

    protected int computeX(int value) {
        //return value * getWidth() / graphBuffer.getRows();
        return map(value, xmin, xmax, LEFT_OFFSET, getWidth() - RIGHT_OFFSET);
    }

    protected int computeY(int value) {
        return getHeight() - BOTTOM_OFFSET - map(value, ymin, ymax, BOTTOM_OFFSET, getHeight() - TOP_OFFSET);
    }
    final static float dash1[] = {5.0f};
    final static BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, dash1, 0.0f);
    final static Color cline = new Color(128, 128, 128);
    //final static Color cxline = new Color(90,90,90);

    protected void drawData(Graphics g) {
        if (graphValues == null) return;
        g.setColor(color);
        
        int X, Y;
        int xval, yval;

        for (int i = 0; i < graphBuffer.getRows(); i++) {
            
            xval = graphValues[0][i];
            yval = graphValues[1][i];

            if (xval == graphBuffer.getInvalidNumber()) break;
            if (yval == graphBuffer.getInvalidNumber()) break;

            // do not plot "out of range" values
            if (xval < xmin || xval > xmax || yval < ymin || yval > ymax) continue;
            
         
            X = computeX(xval);
            Y = computeY(yval);

            g.drawLine(X-2, Y, X+2, Y);
            g.drawLine(X, Y-2, X, Y+2);
        }
    }

    protected void drawAxis(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        Stroke s = g2.getStroke();

        g2.setColor(cline);

        g2.fillRect(0, 0, getWidth(), jLabelTitle.getHeight());

        g2.setStroke(new BasicStroke(2));

        int _Ymin = computeY(ymin);
        int _Ymax = computeY(ymax);
        int _Xmin = computeX(xmin);
        int _Xmax = computeX(xmax);

        // draw the 0 axis:
        if (xmin <= 0 && xmax >= 0) {
            int x0 = computeX(0);
            g.drawLine(x0, _Ymin, x0, _Ymax);
        }
        if (ymin <= 0 && ymax >= 0) {
            int y0 = computeY(0);
            g.drawLine(_Xmin, y0, _Xmax, y0);
        }

        g2.setStroke(dashed);

        // g2.setStroke(new BasicStroke(1));
        for (int ypos = yminor; ypos <= ymax; ypos += yminor) {
            if (ypos >= ymin) {
                int y = computeY(ypos);
                g.drawLine(_Xmin, y, _Xmax, y);
            }
        }
        for (int ypos = -yminor; ypos >= ymin; ypos -= yminor) {
            if (ypos <= ymax) {
                int y = computeY(ypos);
                g.drawLine(_Xmin, y, _Xmax, y);
            }
        }

        for (int xpos = xminor; xpos <= xmax; xpos += xminor) {
            if (xpos >= xmin) {
                int x = computeX(xpos);
                g.drawLine(x, _Ymin, x, _Ymax);
            }
        }
        for (int xpos = -xminor; xpos >= xmin; xpos -= xminor) {
            if (xpos <= xmax) {
                int x = computeX(xpos);
                g.drawLine(x, _Ymin, x, _Ymax);
            }
        }

        g2.setStroke(s);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawAxis(g);
        jLabelXYMin.setText(ymin + " | " + xmin);
        jLabelXMax.setText(""+ xmax);
        jLabelYMax.setText(""+ ymax);
        drawData(g);
        int[][] sd = graphBuffer.stdDevColumns();
        jLabelXAVG.setText(sd[1][0] + " ["+sd[0][0]+"]");
        jLabelYAVG.setText(sd[1][1] + " ["+sd[0][1]+"]");
    }

    protected int map(int x, int in_min, int in_max, int out_min, int out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
    private boolean stop = false;

    public void start() {
        new PaintManager().start();
    }

    public void stop() {
        stop = true;
    }

    protected class PaintManager extends Thread {

        public void run() {

            while (!stop) {

                if (graphBuffer != null) {
                    graphValues = graphBuffer.getDataClone();
                    repaint();
                }
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("End of paint thread.");
        }
    }
}

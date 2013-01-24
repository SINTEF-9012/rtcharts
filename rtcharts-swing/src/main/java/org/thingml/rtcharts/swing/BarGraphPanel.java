/**
 * Copyright (C) 2012 SINTEF <franck.fleurey@sintef.no>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * ../HEADER
 */
package org.thingml.rtcharts.swing;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: franck
 * Date: 01/07/12
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
public class BarGraphPanel extends GraphPanel {

    public BarGraphPanel(GraphBuffer buffer, String name, int ymin, int ymax, int yminor, Color color) {
        super(buffer, name, ymin, ymax, yminor, color);
        setXminor(buffer.getGraphData().length);
    }
    
    @Override
    protected void drawData(Graphics g) {
         if(graphValues == null) return;

            int X, Y;
            int lastX = Integer.MIN_VALUE;
            int highestValue = findHighestValue();
	    int lowestValue = findLowestValue();

            if (lowestValue <= highestValue){ 
                jLabelVMin.setText("" + lowestValue);
                jLabelVMax.setText("" + highestValue);
            }

            int w = computeX(1)- computeX(0) + 1;

            g.setColor(color);
            Graphics2D g2 = (Graphics2D)g;
            Stroke s = g2.getStroke();
            g2.setStroke(new BasicStroke(w));

            for(int i = 0; i < graphValues.length; i++) {

                if(graphValues[i] == graphBuffer.getInvalidNumber()) break;
                X = computeX(i);

                if (graphValues[i] < ymin) {
                    Y = computeY(ymin);
                }
                else if (graphValues[i] > ymax) {
                    Y = computeY(ymax);
                }
                else {

                   Y = computeY(graphValues[i]);
                }



                if (graphValues[i] > 0)
                    g2.drawLine(X, computeY(0)-w/2, X, Y+w/2);
                else if (graphValues[i] < 0)
                    g2.drawLine(X, computeY(0)+w/2, X, Y-w/2);

            }

        g2.setStroke(s);
    }
}

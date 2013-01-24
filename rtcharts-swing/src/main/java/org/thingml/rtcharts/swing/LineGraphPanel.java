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

import java.awt.Color;
import java.awt.Graphics;

/**
 * Created by IntelliJ IDEA.
 * User: franck
 * Date: 01/07/12
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
public class LineGraphPanel extends GraphPanel {

    protected boolean saturate = true;
    
    public LineGraphPanel(GraphBuffer buffer, String name, int ymin, int ymax, int yminor, int xminor, Color color) {
        super(buffer, name, ymin, ymax, yminor, color);
        setXminor(xminor);
    }
    
    public LineGraphPanel(GraphBuffer buffer, String name, int ymin, int ymax, int yminor, Color color) {
        super(buffer, name, ymin, ymax, yminor, color);
    }
    
    @Override
    protected void drawData(Graphics g) {
         if(graphValues == null) return;

            int X, Y;
            int lastX = 0, lastY = Integer.MIN_VALUE;
            int highestValue = findHighestValue();
            int lowestValue = findLowestValue();
            
            if (lowestValue <= highestValue){ 
                jLabelVMin.setText("" + lowestValue);
                jLabelVMax.setText("" + highestValue);
            }

            g.setColor(color);
            
            int yval;

            for(int i = 0; i < graphValues.length; i++) {

                yval = graphValues[i];
                
                if(yval == graphBuffer.getInvalidNumber()) break;

                if (saturate) { // Plot out of bound value with the maximum visible value
                    if (yval < ymin) yval = ymin;
                    if (yval > ymax) yval = ymax;
                }
                else { // do not plot "out of range" values
                    if (yval < ymin || yval > ymax) {
                        lastY = Integer.MIN_VALUE;
                        continue;
                    }
                }

                X = computeX(i);
                Y = computeY(yval);

                if(lastY == Integer.MIN_VALUE) {
                    g.drawLine(X, Y, X , Y);
                }
                else {
                    g.drawLine(lastX, lastY, X , Y);
                }
                lastY = Y;
                lastX = X;
            }
    }
}

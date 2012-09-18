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

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: franck
 * Date: 01/07/12
 * Time: 11:30
 * To change this template use File | Settings | File Templates.
 */
public class TestGraphPanel extends JFrame implements Runnable {

    GraphBuffer graph_buffer = new GraphBuffer(500);
    GraphPanel graph_panel =  new BarGraphPanel(graph_buffer, "Sinus Graph !", -100, 100, 25, Color.RED);

    public TestGraphPanel() {

        this.getContentPane().add(graph_panel);

        this.setSize(400, 300);

        this.setVisible(true);
        new Thread(this).start();
        graph_panel.start();
    }


    public void run() {
          double angle = -Math.PI;

          while (this.isVisible()) {
               try {
                Thread.sleep(25);
            } catch (InterruptedException ex) {
               ex.printStackTrace();
            }
            angle += Math.PI/50;
            if (angle > Math.PI) angle = -Math.PI;

            int v = (int)(Math.sin(angle)*100.0);
            graph_buffer.insertData(v);

          }
        graph_panel.stop();
        System.out.println("End of producer thread.");

        System.exit(0);
    }


    public static void main(String[] args) {
        new TestGraphPanel();
        System.out.println("End of main thread.");
    }
}

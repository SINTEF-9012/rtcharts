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

public class GraphBuffer implements GraphBufferInterface {

    private int[] graphData;
   // private int size;
    private DataBuffer data;

    public int getSize() {
        return data.getRows();
    }
    private int notValidNumber = Integer.MIN_VALUE;
    private int counter = 0;

    public GraphBuffer() {
        data = new DataBuffer(1, 100);
    }

    public GraphBuffer(int customSize) {
        data = new DataBuffer(1, customSize);
    }

    @Override
    public synchronized int[] getGraphData() {
        return data.getColumnClone(0);
    }

    @Override
    public synchronized boolean insertData(int value) {
        return data.appendDataRow(new int[]{value});
    }

    @Override
    public int getInvalidNumber() {
        return data.getInvalidNumber();
    }

    @Override
    public void resetBuffer() {
        data.resetBuffer();
    }

    public int average() {
        return data.averageColumns()[0];
    }

    public int last() {
        return data.lastRow()[0];
    }
}

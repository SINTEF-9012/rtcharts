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

public class DataBuffer {

    private int notValidNumber = Integer.MIN_VALUE;
    private int row_count = 0;
    private int[][] data;
    private int columns;
    private int rows;

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
    
    public int getRowCount() {
        return row_count;
    }
    
        public int getInvalidNumber() {
        return notValidNumber;
    }
    
    public DataBuffer(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        data = new int[columns][rows];
        initialize(notValidNumber);
    }

    private void initialize(int inValidNumber) {
        row_count = 0;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                data[i][j] = inValidNumber;
            }
        }
    }
    
    public void resetBuffer() {
        initialize(notValidNumber);
    }

    public synchronized int[][] getDataClone() {
        int[][] result = data.clone();
        return result;
    }
    
    public synchronized int[] getColumnClone(int column) {
        int[] result = data[column].clone();
        return result;
    }

    public synchronized boolean setData(int row, int column, int data) {
        this.data[column][row] = data;
        return true;
    }
    
    public synchronized boolean setDataRow(int row, int[] data) {
        for (int j = 0; j < columns; j++) {
            this.data[j][row] = data[j];
        }
        return true;
    }
    
    public synchronized boolean appendDataRow(int[] data) {
        
        for (int j = 0; j < columns; j++) {
            if (data[j] == notValidNumber) return false;
        }
        
        if (row_count < rows) { // There is space for the new row
            for (int j = 0; j < columns; j++) {
                this.data[j][row_count] = data[j];
            }
            row_count++;
        }
        else { // shift all rows to make space for the new one at the end
            
            for (int i = 1; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    this.data[j][i - 1] = this.data[j][i];
                }
            }
            for (int j = 0; j < columns; j++) {
                this.data[j][rows - 1] = data[j];
            }
        }
        return true;
    }
    
    public int[] lastRow() {
        int[] result = new int[columns];
        if (row_count == 0) return result;
        for (int j = 0; j < columns; j++) {
            result[j] = data[j][row_count-1];
        }
        
        return result;
    }

    public int[] averageColumns() {
        
        long[] sum = new long[columns];
        int[] count = new int[columns];
        
        for (int j = 0; j < columns; j++) {
            sum[j]=0;
            count[j]=0;
        }
        
        for (int i = 0; i < row_count; i++) {
            for (int j = 0; j < columns; j++) {
                if (data[j][i] != notValidNumber) {
                    sum[j] += data[j][i];
                    count[j]++;
                }
            }
        }
        
        int[] avg = new int[columns];
        
        for (int j = 0; j < columns; j++) {
            if (count[j] > 0) avg[j] = (int) (sum[j]/count[j]);
            else avg[j] = 0;
        }
        
        return avg;
    }
    
    public int[][] stdDevColumns() {
        
        long[] sum = new long[columns];
        int[] count = new int[columns];
        int[] avg = averageColumns();
        
        for (int j = 0; j < columns; j++) {
            sum[j]=0;
            count[j]=0;
        }
        
        for (int i = 0; i < row_count; i++) {
            for (int j = 0; j < columns; j++) {
                if (data[j][i] != notValidNumber) {
                    sum[j] = sum[j] + (data[j][i] - avg[j]) * (data[j][i] - avg[j]);
                    count[j]++;
                }
            }
        }
        
        int[][] std = new int[2][columns];
        
        for (int j = 0; j < columns; j++) {
            if (count[j] > 0) std[0][j] = (int) Math.sqrt(sum[j]/count[j]);
            else std[0][j] = 0;
            std[1][j] = avg[j];
        }
        
        return std;
    }
    
}

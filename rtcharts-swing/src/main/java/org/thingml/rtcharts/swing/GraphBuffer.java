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


public class GraphBuffer implements GraphBufferInterface {

	private int[] graphData;
	private int size;

    public int getSize() {
        return size;
    }
	private int notValidNumber = Integer.MIN_VALUE;
	private int counter = 0;
	
	
	/**
	 * Default constructor, gives datasize of 100
	 */
	public GraphBuffer(){
		size = 100;
		graphData = new int[size];
		initializeArray(notValidNumber);
	}
	

	/**
	 * Custom constructor. Allows you to define the datazise
	 * Buffersize should not exceed 500 as it won't be displayed
	 * properly on the graph
	 * @param customSize number of data values to store
	 */
	public GraphBuffer(int customSize){
		size = customSize;
		graphData = new int[size];
		initializeArray(notValidNumber);
	}
	
	/**
	 * Custom constructor. Allows you to define the datazise
	 * and change the default invalid number to fot you data readings
	 * @param customSize
	 * @param inValidNumber
	 */
	public GraphBuffer(int customSize, int inValidNumber){
		size = customSize;
		graphData = new int[size];
		initializeArray(inValidNumber);
	}
	
	private void initializeArray(int inValidNumber) {
		for(int i = 0; i < graphData.length; i++){
			graphData[i] = inValidNumber;
		}
	}
	
	@Override
	public synchronized int[] getGraphData() {
		int[] result = graphData.clone();
		return result;
	}


	@Override
	public synchronized boolean insertData(int data) {
		if(data == notValidNumber){
			return false;
		}
		if(counter >= size){
			for(int i = 1; i < graphData.length; i++){
				graphData[i-1] = graphData[i];
			}
			graphData[size-1] = data;
			counter++;
			return true;
		}else{
			for(int i = 0; i < graphData.length; i++){
				if(graphData[i] == notValidNumber){
					graphData[i] = data;
					counter++;
					return true;
				}
			}
		}
		return false;
	}


	@Override
	public int getInvalidNumber() {
		return notValidNumber;
	}


	public void setArray(int[] intArray) {
		graphData = intArray;
		size = intArray.length;
	}
	
	@Override
	public void resetBuffer(){
		graphData = new int[size];
	}
        
        public int average() {
            long result = 0;
            int count = 0;
            for (int i = 0; i<graphData.length; i++) {
                if(graphData[i] != notValidNumber){
                    result += graphData[i];
                    count++;
                }
            }
            if (count > 0) return (int)result/count;
            else return 0;
        }
        
        public int last() {
            for (int i =graphData.length-1; i>=0; i--) {
                if(graphData[i] != notValidNumber){
                    return graphData[i];
                }
            }
            return 0;
        }
	
}

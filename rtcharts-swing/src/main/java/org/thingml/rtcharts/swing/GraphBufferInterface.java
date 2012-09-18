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

/**
 * This is not a traditional buffer. This buffer will contain
 * from 0 to x values. If a new value is inserted while size is less than x
 * the value is inserted at the first free spot.
 * If there are no free spaces, the least resent value will be omitted
 * and the new value inserted at the end.
 * @author Jan Ole Skotterud
 *
 */
public interface GraphBufferInterface {
	/**
	 * 
	 * @return a double array with sensor data
	 */
	int[] getGraphData();
	/**
	 * Inserts data into the array
	 * @param data the data to insert
	 * @return true if the insert went ok else false
	 */
	boolean insertData(int data);

	/**
	 * 
	 * @return the number that are default in the buffer
	 */
	int getInvalidNumber();
	
	/**
	 * Deletes the current buffer data and creates a new
	 * empety buffer
	 */
	void resetBuffer();
}

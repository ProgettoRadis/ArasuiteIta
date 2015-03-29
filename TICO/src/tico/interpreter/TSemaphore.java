/*
 * File: TSemaphore.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Carolina Palacio
 * 
 * Date: Feb, 2010
 * 
 * Company: Universidad de Zaragoza, CPS, DIIS
 * 
 * License:
 * 		This program is free software: you can redistribute it and/or 
 * 		modify it under the terms of the GNU General Public License 
 * 		as published by the Free Software Foundation, either version 3
 * 		of the License, or (at your option) any later version.
 * 
 * 		This program is distributed in the hope that it will be useful,
 * 		but WITHOUT ANY WARRANTY; without even the implied warranty of
 * 		MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * 		GNU General Public License for more details.
 * 
 * 		You should have received a copy of the GNU General Public License
 *     	along with this program.  If not, see <http://www.gnu.org/licenses/>.   
 */

package tico.interpreter;

/**
 * 
 * @author Carolina Palacio
 * @version e1.0 Feb, 2010
 *
 */

public class TSemaphore {
	
	private int value;
	
	public TSemaphore (int initial){
		value = initial;
	}
	
	synchronized public void release()
		throws InterruptedException{
			while (value==1) wait();
			++value;
			notify();
	}

	synchronized public void acquire()
		throws InterruptedException{
			while (value==0) wait();
			--value;
			notify();
	}
	
	synchronized public void releaseWhenStop()
	throws InterruptedException{
		if (value==0)
			value=1;
	}
}

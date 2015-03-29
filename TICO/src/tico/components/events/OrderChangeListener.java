/*
 * File: OrderChangeListener.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Oct 5, 2006
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

package tico.components.events;

import java.util.EventListener;

/**
 * The listener interface for receiving order change events. The class that is
 * interested in processing an <code>OrderChangeEvent</code> implements this
 * interface, and the object created with that class is registered with a
 * component, using the component's <code>addOrderChangeListener</code> method.
 * When the <code>OrderChangeEvent</code> occurs, that object's
 * <code>orderChanged</code> method is invoked.
 *
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public interface OrderChangeListener extends EventListener {
	/**
	 * Invoked when an <code>OrderChangeEvent</code> occurs.
	 * 
	 * @param e The <code>OrderChangeEvent</code>
	 */
	public void orderChanged(OrderChangeEvent e);
}

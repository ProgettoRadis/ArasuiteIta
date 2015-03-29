/*
 * File: TBorderSizeComboBox.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: May 18, 2006
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

package tico.components;

import javax.swing.DefaultComboBoxModel;

/**
 * Combo box to choose a border size with predefined values.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TBorderSizeComboBox extends TComboBox {
	// The possible combo box list border sizes
	private static Object[] borderSizes = { new Integer(0), new Integer(1),
			new Integer(2), new Integer(3), new Integer(4), new Integer(5),
			new Integer(6), new Integer(7), new Integer(8), new Integer(9),
			new Integer(10), new Integer(11), new Integer(12), new Integer(13),
			new Integer(14), new Integer(15), new Integer(16), new Integer(17),
			new Integer(18), new Integer(19), new Integer(20) };
	// Default initial borderSize value
	private static int DEFAULT_BORDER_SIZE = ((Integer)borderSizes[1]).intValue();
	
	/**
	 * Creates a new <code>TBorderSizeComboBox</code> with <code>borderSize</code>
	 * defaults to <i>1</i>.
	 */
	public TBorderSizeComboBox() {
		this(DEFAULT_BORDER_SIZE);
	}
	
	/**
	 * Creates a new <code>TBorderSizeComboBox</code> with the specified
	 * initial <code>borderSize</code>.
	 * 
	 * @param borderSize The specified initial <code>borderSize</code> value
	 */
	public TBorderSizeComboBox(int borderSize) {
		super();

		setEditable(true);
		setModel(new DefaultComboBoxModel(borderSizes));
		setBorderSize(borderSize);
	}

	/**
	 * Returns the selected <code>borderSize</code>.
	 * 
	 * @return The selected <code>borderSize</code>
	 */	
	public int getBorderSize() {
		try{
			return ((Integer)getSelectedItem()).intValue();
		}catch (Exception e){
			return DEFAULT_BORDER_SIZE;
		}		
	}

	/**
	 * Set the <code>borderSize</code>. 
	 * 
	 * @param borderSize The <code>borderSize</code> to set
	 */	
	public void setBorderSize(int borderSize) {
		setSelectedItem(new Integer(borderSize));
	}
}

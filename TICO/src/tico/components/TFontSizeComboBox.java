/*
 * File: TFontSizeComboBox.java
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
public class TFontSizeComboBox extends TComboBox {
	// Default initial font size
	private static int DEFAULT_SIZE = 12;
	
	// Font size combo box list options
	private static Object[] sizes = { new Integer(6), new Integer(8),
			new Integer(10), new Integer(12), new Integer(14), new Integer(16),
			new Integer(20), new Integer(24), new Integer(28), new Integer(32),
			new Integer(40), new Integer(48), new Integer(56), new Integer(64),
			new Integer(80), new Integer(96) };
	
	/**
	 * Creates a new <code>TFontSizeComboBox</code> with <code>size</code>
	 * defaults to <i>12</i>.
	 */
	public TFontSizeComboBox() {
		this(DEFAULT_SIZE);
	}
	
	/**
	 * Creates a new <code>TFontSizeComboBox</code> with the specified
	 * initial <code>size</code>.
	 * 
	 * @param size The specified initial <code>size</code> value
	 */
	public TFontSizeComboBox(int size) {
		super();

		setEditable(true);
		setModel(new DefaultComboBoxModel(sizes));
		setFontSize(size);
	}

	/**
	 * Returns the selected <code>size</code>.
	 * 
	 * @return The selected <code>size</code>
	 */	
	public int getFontSize() {
		try{
			return ((Integer)getSelectedItem()).intValue();
		}catch (Exception e){
			return DEFAULT_SIZE;
		}
	}

	/**
	 * Set the <code>size</code>. 
	 * 
	 * @param size The <code>size</code> to set
	 */	
	public void setFontSize(int size) {
		setSelectedItem(new Integer(size));
	}
}

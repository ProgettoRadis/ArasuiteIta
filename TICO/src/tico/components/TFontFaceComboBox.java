/*
 * File: TFontFaceComboBox.java
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

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;

import javax.swing.DefaultComboBoxModel;

/**
 * Combo box to choose a font face only showing the system available fonts as
 * options.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TFontFaceComboBox extends TComboBox {
	// Default TFontFaceComboBox width
	protected final static int DEFAULT_WIDTH = 120;
	
	// The posible combo box font faces
	private static String[] fonts =
		GraphicsEnvironment.getLocalGraphicsEnvironment().
			getAvailableFontFamilyNames();
	// Default initial fontFace
	private static String DEFAULT_FONT = fonts[0];
	
	/**
	 * Creates a new <code>TFontFaceComboBox</code> with <code>fontFace</code>
	 * defaults to <i>default</i>.
	 */
	public TFontFaceComboBox() {
		this(DEFAULT_FONT);
	}

	/**
	 * Creates a new <code>TFontFaceComboBox</code> with  the specified initial
	 * <code>fontFace</code>.
	 * 
	 * @param fontFace The selected initial <code>fontFace</code>
	 */
	public TFontFaceComboBox(String fontFace) {		
		super();
	    
		setModel(new DefaultComboBoxModel(fonts));
		setFontFace(fontFace);
	}

	/**
	 * Returns the selected <code>fontFace</code>.
	 * 
	 * @return The selected <code>fontFace</code>
	 */
	public String getFontFace() {
		return (String)getSelectedItem();
	}

	/**
	 * Set the <code>fontFace</code>.
	 * 
	 * @param fontFace The <code>fontFace</code> to set
	 */
	public void setFontFace(String fontFace) {
		setSelectedItem(fontFace);
	}

	/**
	 * Sets the <code>TFontFaceComboBox</code> default size. To change it
	 * this function must be overriden. 
	 */
	protected void setDefaultSize() {
		setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAUL_HEIGHT));
		setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAUL_HEIGHT));
	}
}

/*
 * File: TTextField.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Aug 1, 2006
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

import java.awt.Insets;

import javax.swing.JTextField;

/**
 * An implementation of a text field with the format parameters for Tico
 * applications.
 *
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TTextField extends JTextField {

	/**
	 * Creates a <code>TTextField</code> with an empty string.

	 */
	public TTextField() {
		this("");
	}

	/**
	 * Creates a <code>TTextField</code> with a <code>text</code>.
	 * 
	 * @param text The <code>text</code> of the button
	 */
	public TTextField(String text) {
		super(text);
		
		setMargin(new Insets(1,2,1,2));
	}
}

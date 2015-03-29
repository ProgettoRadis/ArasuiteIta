/*
 * File: TComboBox.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Aug 17, 2006
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
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

/**
 * An implementation of a combo box with the format parameters for Tico
 * applications.
 *
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TComboBox extends JComboBox {
	// Default TComboBox size
	protected static final int DEFAUL_HEIGHT = 25;
	protected static final int DEFAULT_WIDTH = 70;

	/**
	 * Creates a <code>TComboBox</code> with a default data model.
	 */
	public TComboBox() {
		super();
		setDefaultSize();
	}

	/**
	 * Creates a <code>TComboBox</code> that takes it's items from an existing
	 * <code>ComboBoxModel</code>.
	 * 
	 * @param dataModel The <code>ComboBoxModel</code> that provides the displayed
	 * list of items
	 */
	public TComboBox(ComboBoxModel dataModel) {
		super(dataModel);
		setDefaultSize();
	}

	/**
	 * Creates a <code>TComboBox</code> that contains the elements in the
	 * specified <code>array</code>.
	 * 
	 * @param items An <code>array</code> of objects to insert into the <code>TComboBox</code>
	 */
	public TComboBox(Object[] items) {
		super(items);
		setDefaultSize();
	}

	/**
	 * Creates a <code>TComboBox</code> that contains the elements in the
	 * specified <code>Vector</code>.
	 * 
	 * @param items An <code>Vector</code> of objects to insert into the <code>TComboBox</code>
	 */
	public TComboBox(Vector items) {
		super(items);
		setDefaultSize();
	}

	/**
	 * Sets the <code>TComboBox</code> default size. To change it this function
	 * must be overriden. 
	 */
	protected void setDefaultSize() {
		setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAUL_HEIGHT));
		setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAUL_HEIGHT));
	}
}

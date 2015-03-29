/*
 * File: TToolBar.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Copyright (c) 2005-2006 	Pablo Muñoz
 * 							David Ramos
 * 							Fernando Negre
 * 							Joaquin Ezpeleta
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

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JToolBar;

/**
 * An implementation of a tool bar with the format parameters for Tico
 * applications.
 *
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TToolBar extends JToolBar {

	/**
	 * Creates a <code>TToolBar</code> with the specified <code>name</code>.
	 * 
	 * @param name The specified <code>name</code>
	 */
	public TToolBar(String name) {
		super(name);
		setOrientation(TToolBar.HORIZONTAL);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		Component parent = getParent();
		
		// If the toolbar is on a separated window
		if ((parent != null) && (!(parent instanceof TToolBarContainer)))
			// Also hide the window
			((JPanel)parent).getTopLevelAncestor().setVisible(visible);

		super.setVisible(visible);
	}
}
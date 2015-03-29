/*
 * File: TFontFaceList.java
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import tico.editor.TEditor;

/**
 * List to choose a font face only showing the system available fonts as
 * options.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TFontFaceList extends JPanel {
	// Default TFontFaceList size
	private static int LIST_MIN_HEIGHT = 120;	
	private static int LIST_MIN_WIDTH = 100;
	
	// The posible combo box font faces
	private static String[] fonts =
		GraphicsEnvironment.getLocalGraphicsEnvironment().
			getAvailableFontFamilyNames();
	// Default initial fontFace
	private static String DEFAULT_FONT = fonts[0];

	// The font list scroll bar
	private JScrollPane fontListScroll;
	// The font faces list to choose
	private JList fontList;
	// A text field who shows the selected font face
	private JTextField fontField;
	
	/**
	 * Creates a new <code>TFontFaceList</code> with <code>fontFace</code>
	 * defaults to <i>default</i>.
	 */
	public TFontFaceList() {
		this(DEFAULT_FONT);
	}

	/**
	 * Creates a new <code>TFontFaceList</code> with  the specified initial
	 * <code>fontFace</code>.
	 * 
	 * @param fontFace The selected initial <code>fontFace</code>
	 */
	public TFontFaceList(String fontFace) {		
		super();

		createFontList();
		createFontField();
		placeComponents();
		
		fontList.addListSelectionListener(new TFontFaceSelectionListener());
		
		setFontFace(fontFace);
		
	}
	
	// Creates the fontList inside of the fontListScroll
	private void createFontList() {
		fontListScroll = new JScrollPane();
		
		fontListScroll.setMinimumSize(new Dimension(LIST_MIN_WIDTH,LIST_MIN_HEIGHT));
		fontListScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		fontListScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		fontList = new JList();

		fontList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		fontList.setLayoutOrientation(JList.VERTICAL);
		fontList.setListData(fonts);	
		
		fontListScroll.setViewportView(fontList);
		
		if(TEditor.get_android_mode()) fontList.setEnabled(false);
	}
	
	// Creates the fontField
	private void createFontField() {
		fontField = new JTextField();
		
		fontField.setEditable(false);
		fontField.setFocusable(false);
	}
	
	// Place the components in the panel
	private void placeComponents() {
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());
		
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 0.0;
		add(fontField,c);
		
		c.insets = new Insets(5,0,0,0);
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(fontListScroll,c);
	}

	/**
	 * Returns the selected <code>fontFace</code>.
	 * 
	 * @return The selected <code>fontFace</code>
	 */
	public String getFontFace() {
		return (String)fontList.getSelectedValue();
	}

	/**
	 * Set the <code>fontFace</code>.
	 * 
	 * @param fontFace The <code>fontFace</code> to set
	 */
	public void setFontFace(String fontFace) {
		if (fontFace == null) return;
		
		fontList.setSelectedValue(fontFace,true);
		
		fontField.setText((String)fontFace);
		fontField.setCaretPosition(0);
	}

	/**
	 * Adds an <code>ListSelectionListener</code>.
	 * 
	 * The <code>ListSelectionListener</code> will receive an
	 * <code>ListSelectionEvent</code> when the <code>fontFace</code> has been
	 * changed.
	 * 
	 * @param listener The <code>ListSelectionListener</code> that is to be notified
	 */
	public void addListSelectionListener(ListSelectionListener listener) {
		fontList.addListSelectionListener(listener);
	}
	
	/**
	 * Removes an <code>ListSelectionListener</code>.
	 * 
	 * @param listener The <code>ListSelectionListener</code> to remove
	 */
    public void removeListSelectionListener(ListSelectionListener listener) {
    	fontList.removeListSelectionListener(listener);
    }
	
	// Set the selected fontFace to the fontField
	private class TFontFaceSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent event) {
			if (!event.getValueIsAdjusting()) {
				fontField.setText((String)fontList.getSelectedValue());
				fontField.setCaretPosition(0);
			}
		}
	}
}

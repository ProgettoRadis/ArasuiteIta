/*
 * File: TColorComboBox.java
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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import tico.configuration.TLanguage;

/**
 * Combo box to choose a color with predefined options. The options are
 * are divided in basic colors and custom colors. The custom colors are chosen
 * by the user and shared between all the <code>TColorComboBox</code> instances.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TColorComboBox extends TComboBox {
	// Default initial color value
	private static Color DEFAULT_COLOR = (Color)Color.BLACK;
	// Default initial allowTransparent value
	private static final boolean DEFAULT_TRANSPARENT = false;
	
	// Max custom colors than can be added by the user
	private static final int MAX_CUSTOM_COLORS = 4;
	// Determines if the transparent value can be chosen
	private boolean allowTransparent;
	
	// The posible combo box basic colors
	private static Object[] basicColors = { Color.WHITE,
			new Color(252, 233, 79), new Color(252, 175, 62),
			new Color(115, 210, 22), new Color(52, 101, 164),
			new Color(173, 127, 168), new Color(239, 41, 41), Color.BLACK };
	// The posible combo box custom colors
	private static Vector customColors = new Vector();
	
	private Color currentColor;
	
	/**
	 * Creates a new <code>TColorComboBox</code> with <code>color</code>
	 * defaults to <i>black</i> and <code>allowTransparent</code> to false.
	 */
	public TColorComboBox() {
		this(DEFAULT_COLOR, DEFAULT_TRANSPARENT);
	}
	
	/**
	 * Creates a new <code>TColorComboBox</code> with the specified initial
	 * <code>color</code> and defaults to <code>allowTransparent</code> to false.
	 * 
	 * @param color The specified initial <code>color</code> value
	 */
	public TColorComboBox(Color color) {
		this(color, DEFAULT_TRANSPARENT);
	}
	
	/**
	 * Creates a new <code>TColorComboBox</code> with <code>color</code>
	 * defaults to <i>black</i> and the specified initial
	 * <code>allowTransparent</code> value.
	 * 
	 * @param allowTransparent The specified initial <code>allowTransparent</code> value
	 */
	public TColorComboBox(boolean allowTransparent) {
		this(DEFAULT_COLOR, allowTransparent);
	}

	/**
	 * Creates a new <code>TColorComboBox</code> with the specified initial
	 * <code>color</code> and <code>allowTransparent</code> value.
	 * 
	 * @param color The specified initial <code>color</code> value
	 * @param allowTransparent The specified initial <code>allowTransparent</code> value
	 */
	public TColorComboBox(Color color, boolean allowTransparent) {
		super();
		// Set allow transparent value
		this.allowTransparent = allowTransparent;
		// Creates the model and set the list renderer
		setModel(createColorModel());
		setRenderer(new TColorComboListRenderer());
		
		// Add listener to update he color model, its necesary to get all
		// the color combo boxes selected with the last custom color set
		addPopupMenuListener(new PopupMenuListener() {
			public void popupMenuCanceled(PopupMenuEvent arg0) {}
			public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {}
			
			public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
				// Store current color because its selection is lost when
				// the model its changed
				Color currentColor = getColor();
				// Create the new model, with the current custom colors
				setModel(createColorModel());
				// Set the current color which also adds that color to the model
				// if it has been removed
				setColor(currentColor);	
			}
		});
		// Set the initial color
		setColor(color);
	}

	// Adds a color to the customColor vector, if it is not contained yet, and
	// updates the JColorComboBox model. If the MAX_CUSTOM_COLORS is reached, the
	// oldest custom color is deleted
	private void addCustomColor(Color color) {
		if (color == null) return;
		// If the color is in the model, do not add it
		for (int i = 0; basicColors.length > i; i++)
			if (basicColors[i].equals(color))
				return;
		if (customColors.contains(color))
			return;

		// If the customColors vector is full, delete the oldest color
		if (customColors.size() >= MAX_CUSTOM_COLORS)
			customColors.removeElementAt(0);
		// Add the new color
		customColors.add(color);
		// Create the new color model with the new color vectors
		setModel(createColorModel());
	}

	/**
	 * Create the component model using the <code>basicColors</code> and
	 * <code>customColors</code> variables, adding the "custom" color and
	 * the "transparent" if <code>allowTransparent</code> is <i>true</i>.
	 * 
	 * @return The created <code>comboBoxModel</code>
	 */
	protected DefaultComboBoxModel createColorModel() {
		// Create empty combo box model
		DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
		// If allowTransparent add a null color that will represent
		// the "transparent" color
		if (allowTransparent) comboBoxModel.addElement((Color)null);
		// Add standard color
		for (int i = 0; i < basicColors.length; i++)
			comboBoxModel.addElement((Color)basicColors[i]);
		// Add last colors added by the user
		for (int i = 0; i < customColors.size(); i++)
			comboBoxModel.addElement((Color)customColors.get(i));
		// Add "custom" color to allow the user to add customColors
		comboBoxModel.addElement("Custom");

		return comboBoxModel;
	}

	/**
	 * Returns the selected <code>color</code>.
	 * 
	 * @return The selected <code>color</code>
	 */
	public Color getColor() {
		return currentColor;
	}

	/**
	 * Set the <code>color</code>. If the <code>color</code> is not in the model
	 * it is added .
	 * 
	 * @param color The <code>color</code> to set
	 */
	public void setColor(Color color) {
		if (!allowTransparent &&
		    (color == null))
			throw new NullPointerException();
		// Add the color to the model
		addCustomColor(color);
		// Select it
		setSelectedItem(color);
		// Set it
		currentColor = color;
	}

	// Shows the JColorChooser option and adds the color to the combo
	// box model when the "custom" option has been selected
	protected void fireActionEvent() {
		// The custom option is instance of String
		if (!(getSelectedItem() instanceof Color) &&
		     (getSelectedItem() != null)) {
			Color newColor = JColorChooser.showDialog(null,
					TLanguage.getString("TColorComboBox.CHOOSE_BACKGROUND_COLOR"), null);
			// If the new color is null, do not change it
			if (newColor == null) newColor = currentColor;
			// Set the color
			setColor(newColor);
		}
		else
			currentColor = (Color)getSelectedItem();

		super.fireActionEvent();
	}

	// JColorComboBox popup renderer
	private class TColorComboListRenderer extends JLabel implements
			ListCellRenderer {
		
		// The color to display
		private Color color;
		// Determines if the color has to be shown as normal or has to be
		// shown as an emty label with a text
		private boolean blank;

		// Creates the list renderer
		public TColorComboListRenderer() {
			super();

			setOpaque(true);
			setVerticalAlignment(CENTER);
			setHorizontalAlignment(CENTER);

			setBorder(new CompoundBorder(new MatteBorder(1, 1, 1, 1,
					Color.WHITE), new LineBorder(Color.BLACK)));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.Component#paint(java.awt.Graphics)
		 */
		public void paint(Graphics g) {
			if (!blank) {
				setBackground(color);
				setForeground(color);
			} else {
				setBackground(Color.WHITE);
				setForeground(Color.BLACK);
			}

			super.paint(g);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList,
		 *      java.lang.Object, int, boolean, boolean)
		 */
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			if (value instanceof Color) {
				color = (Color)value;
				// The text is needed to maintain the label size
				setText(TLanguage.getString("TColorComboBox.COLOR"));
				blank = false;
			} else if (value == null) {
				setText(TLanguage.getString("TColorComboBox.TRANSPARENT"));
				blank = true;
			} else {
				setText(TLanguage.getString("TColorComboBox.OTHER"));
				blank = true;
			}
			// Return the component that will be shown in the comboBox options
			// panel
			return this;
		}
	}
}

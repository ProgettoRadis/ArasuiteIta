/*
 * File: TTextAlignSelectorPanel.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Aug 16, 2006
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
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;

/**
 * Component to choose the alingment of a text inside a component.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TTextAlignSelectorPanel extends JPanel {
	// Button groups for the components toggle buttons
	private ButtonGroup horizontalButtonGroup;
	private ButtonGroup verticalButtonGroup;

	// Components horizontal align toggle buttons
	private JToggleButton horizontalLeftAlignToggleButton;
	private JToggleButton horizontalCenterAlignToggleButton;
	private JToggleButton horizontalRightAlignToggleButton;

	// Components vertical align toggle buttons
	private JToggleButton verticalTopAlignToggleButton;
	private JToggleButton verticalCenterAlignToggleButton;
	private JToggleButton verticalBottomAlignToggleButton;

	/**
	 * Creates a new <code>TTextAlignSelectorPanel</code>.
	 */
	public TTextAlignSelectorPanel() {
		super();
		// Creates the border of the component
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), TLanguage.getString("TTextAlignSelectorPanel.TITLE")));
		// Create components
		createButtons();

		// Place components
		setLayout(new FlowLayout());
		// Add horizontal text align buttons
		add(horizontalLeftAlignToggleButton);
		add(horizontalCenterAlignToggleButton);
		add(horizontalRightAlignToggleButton);
		// Add a blank space
		add(new JPanel());
		// Add vertical text align buttons
		add(verticalTopAlignToggleButton);
		add(verticalCenterAlignToggleButton);
		add(verticalBottomAlignToggleButton);
		// Set default values
		horizontalCenterAlignToggleButton.setSelected(true);
		verticalCenterAlignToggleButton.setSelected(true);
	}

	// Create all components buttons and add them to their corresponding
	// button groups
	private void createButtons() {
		horizontalButtonGroup = new ButtonGroup();

		horizontalLeftAlignToggleButton = new JToggleButton(TResourceManager
				.getImageIcon("format-horizontal-align-left-32.png"));
		horizontalLeftAlignToggleButton.setMargin(new Insets(2, 2, 2, 2));
		horizontalLeftAlignToggleButton.setToolTipText(TLanguage.getString("TTextAlignSelectorPanel.LEFT_HALIGN_TOOLTIP"));

		horizontalCenterAlignToggleButton = new JToggleButton(TResourceManager
				.getImageIcon("format-horizontal-align-center-32.png"));
		horizontalCenterAlignToggleButton.setMargin(new Insets(2, 2, 2, 2));
		horizontalCenterAlignToggleButton.setToolTipText(TLanguage.getString("TTextAlignSelectorPanel.CENTER_HALIGN_TOOLTIP"));

		horizontalRightAlignToggleButton = new JToggleButton(TResourceManager
				.getImageIcon("format-horizontal-align-right-32.png"));
		horizontalRightAlignToggleButton.setMargin(new Insets(2, 2, 2, 2));
		horizontalRightAlignToggleButton.setToolTipText(TLanguage.getString("TTextAlignSelectorPanel.RIGHT_HALIGN_TOOLTIP"));

		horizontalButtonGroup.add(horizontalLeftAlignToggleButton);
		horizontalButtonGroup.add(horizontalCenterAlignToggleButton);
		horizontalButtonGroup.add(horizontalRightAlignToggleButton);

		verticalButtonGroup = new ButtonGroup();

		verticalTopAlignToggleButton = new JToggleButton(TResourceManager
				.getImageIcon("format-vertical-align-top-32.png"));
		verticalTopAlignToggleButton.setMargin(new Insets(2, 2, 2, 2));
		verticalTopAlignToggleButton.setToolTipText(TLanguage.getString("TTextAlignSelectorPanel.TOP_VALIGN_TOOLTIP"));
		verticalCenterAlignToggleButton = new JToggleButton(TResourceManager
				.getImageIcon("format-vertical-align-center-32.png"));
		verticalCenterAlignToggleButton.setMargin(new Insets(2, 2, 2, 2));
		verticalCenterAlignToggleButton.setToolTipText(TLanguage.getString("TTextAlignSelectorPanel.CENTER_VALIGN_TOOLTIP"));
		verticalBottomAlignToggleButton = new JToggleButton(TResourceManager
				.getImageIcon("format-vertical-align-bottom-32.png"));
		verticalBottomAlignToggleButton.setMargin(new Insets(2, 2, 2, 2));
		verticalBottomAlignToggleButton.setToolTipText(TLanguage.getString("TTextAlignSelectorPanel.BOTTOM_VALIGN_TOOLTIP"));

		verticalButtonGroup.add(verticalTopAlignToggleButton);
		verticalButtonGroup.add(verticalCenterAlignToggleButton);
		verticalButtonGroup.add(verticalBottomAlignToggleButton);
	}

	/**
	 * Returns the selected <code>horizontalAlingnment</code>.
	 * 
	 * @return The selected <code>horizontalAlingnment</code>. Must be one of
	 * the following constants defined in <code>SwingConstants</code>: LEFT,
	 * RIGHT or CENTER.
	 * 
	 * @see SwingConstants
	 */
	public int getHorizontalAlignment() {
		if (horizontalLeftAlignToggleButton.isSelected())
			return SwingConstants.LEFT;
		else if (horizontalRightAlignToggleButton.isSelected())
			return SwingConstants.RIGHT;
		else
			return SwingConstants.CENTER;
	}

	/**
	 * Set the <code>horizontalAlingnment</code>.
	 * 
	 * @param alignment The <code>horizontalAlingnment</code> to set. Must be
	 * one of the following constants defined in <code>SwingConstants</code>:
	 * LEFT, RIGHT or CENTER.
	 * 
	 * @see SwingConstants
	 */
	public void setHorizontalAlignment(int alignment) {
		switch (alignment) {
		case SwingConstants.LEFT:
			horizontalLeftAlignToggleButton.setSelected(true);
			break;
		case SwingConstants.RIGHT:
			horizontalRightAlignToggleButton.setSelected(true);
			break;
		default:
			horizontalCenterAlignToggleButton.setSelected(true);
		}
	}

	/**
	 * Returns the selected <code>verticalAlingnment</code>.
	 * 
	 * @return The selected <code>verticalAlingnment</code>. Must be one of
	 * the following constants defined in <code>SwingConstants</code>: TOP,
	 * BOTTOM or CENTER.
	 * 
	 * @see SwingConstants
	 */
	public int getVerticalAlignment() {
		if (verticalTopAlignToggleButton.isSelected())
			return SwingConstants.TOP;
		else if (verticalBottomAlignToggleButton.isSelected())
			return SwingConstants.BOTTOM;
		else
			return SwingConstants.CENTER;
	}

	/**
	 * Set the <code>verticalAlingnment</code>.
	 * 
	 * @param alignment The <code>verticalAlingnment</code> to set. Must be
	 * one of the following constants defined in <code>SwingConstants</code>:
	 * TOP, BOTTOM or CENTER.
	 * 
	 * @see SwingConstants
	 */
	public void setVerticalAlignment(int alignment) {
		switch (alignment) {
		case SwingConstants.TOP:
			verticalTopAlignToggleButton.setSelected(true);
			break;
		case SwingConstants.BOTTOM:
			verticalBottomAlignToggleButton.setSelected(true);
			break;
		default:
			verticalCenterAlignToggleButton.setSelected(true);
		}
	}
	
	/**
	 * Adds an <code>ActionListener</code>.
	 * 
	 * The <code>ActionListener</code> will receive an <code>ActionEvent</code>
	 * when any of the components buttons receives an <code>ActionEvent</code>.
	 * 
	 * @param listener The <code>ActionListener</code> that is to be notified
	 */	
	public void addActionListener(ActionListener listener) {
		horizontalLeftAlignToggleButton.addActionListener(listener);
		horizontalCenterAlignToggleButton.addActionListener(listener);
		horizontalRightAlignToggleButton.addActionListener(listener);

		verticalTopAlignToggleButton.addActionListener(listener);
		verticalCenterAlignToggleButton.addActionListener(listener);
		verticalBottomAlignToggleButton.addActionListener(listener);
	}
	
	/**
	 * Removes an <code>ActionListener</code>.
	 * 
	 * @param listener The <code>ActionListener</code> to remove
	 */	
	public void removeActionListener(ActionListener listener) {
		horizontalLeftAlignToggleButton.removeActionListener(listener);
		horizontalCenterAlignToggleButton.removeActionListener(listener);
		horizontalRightAlignToggleButton.removeActionListener(listener);

		verticalTopAlignToggleButton.removeActionListener(listener);
		verticalCenterAlignToggleButton.removeActionListener(listener);
		verticalBottomAlignToggleButton.removeActionListener(listener);
	}
}

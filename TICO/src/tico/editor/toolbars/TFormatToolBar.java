/*
 * File: TFormatToolBar.java
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

package tico.editor.toolbars;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jgraph.graph.AttributeMap;

import tico.board.TBoardConstants;
import tico.board.TBoardLayoutCache;
import tico.board.components.TComponent;
import tico.board.components.TGrid;
import tico.components.TBorderSizeComboBox;
import tico.components.TColorComboBox;
import tico.components.TToolBar;
import tico.components.TToolBarButton;
import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;
import tico.editor.TEditor;

/**
 * Tool bar with the components format components.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TFormatToolBar extends TToolBar {
	// The editor whose selected components will receive the format changes
	private TEditor editor;

	// Background and border components
	private TColorComboBox backgroundColorComboBox;
	private TColorComboBox gradientColorComboBox;
	private TColorComboBox borderColorComboBox;
	private TBorderSizeComboBox borderSizeComboBox;
	// Buttons to appy formats
	private TToolBarButton applyBackground;
	private TToolBarButton applyBorder;
	
	/**
	 * Creates a new <code>TFormatToolBar</code>.
	 * 
	 * @param editor The <code>editor</code> change formats receiver
	 */
	public TFormatToolBar(final TEditor editor) {
		super(TLanguage.getString("TFormatToolBar.NAME"));

		this.editor = editor;

		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		// TUNE Add tool tip texts
		applyBackground = new TToolBarButton(TResourceManager
				.getImageIcon("format-background-22.png"));
		applyBackground.setToolTipText(TLanguage.getString("TFormatToolBar.APPLY_BACKGROUND_TOOLTIP"));
		backgroundColorComboBox = new TColorComboBox(true);
		gradientColorComboBox = new TColorComboBox(true);

		applyBorder = new TToolBarButton(TResourceManager
				.getImageIcon("format-border-22.png"));
		applyBorder.setToolTipText(TLanguage.getString("TFormatToolBar.APPLY_BORDER_TOOLTIP"));
		borderColorComboBox = new TColorComboBox(true);
		borderSizeComboBox = new TBorderSizeComboBox();

		backgroundColorComboBox.addActionListener(new ChangeColorListener());
		borderColorComboBox.addActionListener(new ChangeColorListener());

		applyBackground.addActionListener(new ChangeAttributesListener());
		applyBorder.addActionListener(new ChangeAttributesListener());
		
		// TODO Add default properties to TSetup
		backgroundColorComboBox.setColor(Color.WHITE);
		gradientColorComboBox.setColor(null);
		borderColorComboBox.setColor(Color.BLACK);
		borderSizeComboBox.setBorderSize(1);

		add(applyBackground);
		addSeparator(new Dimension(3, 3));
		add(backgroundColorComboBox);
		addSeparator(new Dimension(3, 3));
		add(gradientColorComboBox);
		addSeparator();
		add(applyBorder);
		addSeparator(new Dimension(3, 3));
		add(borderColorComboBox);
		addSeparator(new Dimension(3, 3));
		add(borderSizeComboBox);
	}

	/**
	 * Updates the format components with the format of the first editors selected
	 * component.
	 */
	public void updateComponents() {
		// The tool bar is going to be enabled if exists a board
		boolean enabled = (editor.getCurrentBoard() != null);

		applyBackground.setEnabled(enabled);
		applyBorder.setEnabled(enabled);
		backgroundColorComboBox.setEnabled(enabled);
		gradientColorComboBox.setEnabled(enabled);
		borderColorComboBox.setEnabled(enabled);
		borderSizeComboBox.setEnabled(enabled);

		// If enabled, set the components to the selected value
		if (enabled) {
			TComponent selectedComponent = (TComponent) editor
					.getCurrentBoard().getSelectionCell();
			// If no component is selected do not actualize the components
			if (selectedComponent != null) {
				// Update the values to the selected components
				
				// If is a TGrid I get the first grid cell
				if (selectedComponent instanceof TGrid) {
					// If is an empty frid, exit
					if (((TGrid) selectedComponent).getChildCount() == 0)
						return;
					selectedComponent = (TComponent) selectedComponent
							.getChildAt(0);
				}

				// Get the selected component attributes
				AttributeMap map = selectedComponent.getAttributes();
				// Set them to the tool bar
				backgroundColorComboBox.setColor(TBoardConstants
						.getBackground(map));
				gradientColorComboBox.setColor(TBoardConstants
						.getGradientColor(map));
				borderColorComboBox.setColor(TBoardConstants
						.getBorderColor(map));
				borderSizeComboBox.setBorderSize((int) TBoardConstants
						.getLineWidth(map));
			}
		}
	}

	/**
	 * Creates an <code>AttributeMap</code> with the tool bar's selected format
	 * parameters and the alternative border parameters.
	 * 
	 * @return The tool bar's format <code>AttributeMap</code>
	 */
	public AttributeMap getAttributes() {
		AttributeMap map = new AttributeMap();

		Color backgroundColor = backgroundColorComboBox.getColor();
		if (backgroundColor != null)
			TBoardConstants.setBackground(map, backgroundColor);

		Color gradientColor = gradientColorComboBox.getColor();
		if (gradientColor != null)
			TBoardConstants.setGradientColor(map, gradientColor);

		Color borderColor = borderColorComboBox.getColor();
		if (borderColor != null)
			TBoardConstants.setBorderColor(map, borderColor);

		TBoardConstants.setLineWidth(map, borderSizeComboBox.getBorderSize());
		
		TBoardConstants.setAlternativeBorderColor(map, TBoardConstants.DEFAULT_ALTERNATIVE_BORDERCOLOR);
		TBoardConstants.setAlternativeLinewidth(map, TBoardConstants.DEFAULT_ALTERNATIVE_LINEWIDTH);

		return map;
	}

	// Apply format button listeners
	private class ChangeAttributesListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			AttributeMap map = new AttributeMap();
			// Apply background format
			if (e.getSource() == applyBackground) {
				Color backgroundColor = backgroundColorComboBox.getColor();
				if (backgroundColor != null)
					TBoardConstants.setBackground(map, backgroundColor);

				Color gradientColor = gradientColorComboBox.getColor();
				if (gradientColor != null)
					TBoardConstants.setGradientColor(map, gradientColor);

				if ((gradientColor == null) && (backgroundColor == null))
					TBoardConstants.setRemoveAttributes(map, new Object[] {
							TBoardConstants.BACKGROUND,
							TBoardConstants.GRADIENTCOLOR });
				else if (gradientColor == null)
					TBoardConstants.setRemoveAttributes(map,
							new Object[] { TBoardConstants.GRADIENTCOLOR });
				else if (backgroundColor == null)
					TBoardConstants.setRemoveAttributes(map,
							new Object[] { TBoardConstants.BACKGROUND });
			// Aplly border format
			} else if (e.getSource() == applyBorder) {
				Color borderColor = borderColorComboBox.getColor();
				if (borderColor != null)
					TBoardConstants.setBorderColor(map, borderColor);
				else
					TBoardConstants.setRemoveAttributes(map,
							new Object[] { TBoardConstants.BORDERCOLOR });

				TBoardConstants.setLineWidth(map, borderSizeComboBox
						.getBorderSize());
			}

			((TBoardLayoutCache)editor.getCurrentBoard().getGraphLayoutCache())
					.editRoots(editor.getCurrentBoard().getSelectionCells(),
							map);
		}

	}

	// Action listener to enable and disable tool bar's components
	private class ChangeColorListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// If the background is null, disable gradient chooser
			if (backgroundColorComboBox.getColor() == null)
				gradientColorComboBox.setEnabled(false);
			else
				gradientColorComboBox.setEnabled(true);
			// If the border is null, disable border width
			if (borderColorComboBox.getColor() == null)
				borderSizeComboBox.setEnabled(false);
			else
				borderSizeComboBox.setEnabled(true);
		}
	}
}
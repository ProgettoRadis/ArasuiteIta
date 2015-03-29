/*
 * File: TTextToolBar.java
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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jgraph.graph.AttributeMap;

import tico.board.TBoardConstants;
import tico.board.TBoardLayoutCache;
import tico.board.components.TComponent;
import tico.board.components.TGrid;
import tico.components.TColorComboBox;
import tico.components.TFontFaceComboBox;
import tico.components.TFontSizeComboBox;
import tico.components.TToolBar;
import tico.components.TToolBarButton;
import tico.components.TToolBarToggleButton;
import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;
import tico.editor.TEditor;

/**
 * Tool bar with the text format.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TTextToolBar extends TToolBar {
	// The editor whose selected components will receive the text format changes
	private TEditor editor;

	// Button to apply font format
	private TToolBarButton applyFont;
	// Font format components	
	private TToolBarToggleButton textBoldButton;
	private TToolBarToggleButton textItalicButton;
	private TFontFaceComboBox fontFaceComboBox;
	private TFontSizeComboBox fontSizeComboBox;
	private TColorComboBox fontColorComboBox;
	
	/**
	 * Creates a new <code>TTextToolBar</code>.
	 * 
	 * @param editor The <code>editor</code> change formats receiver
	 */
	public TTextToolBar(TEditor editor) {
		super(TLanguage.getString("TTextToolBar.NAME"));

		this.editor = editor;
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		applyFont = new TToolBarButton(TResourceManager
				.getImageIcon("format-font-22.png"));
		applyFont.setToolTipText(TLanguage.getString("TTextToolBar.APPLY_TEXT_TOOLTIP"));
		fontFaceComboBox = new TFontFaceComboBox();
		fontSizeComboBox = new TFontSizeComboBox();
		textBoldButton = new TToolBarToggleButton(TResourceManager
				.getImageIcon("format-bold-22.png"));
		textBoldButton.setToolTipText(TLanguage.getString("TTextToolBar.BOLD"));
		textItalicButton = new TToolBarToggleButton(TResourceManager
				.getImageIcon("format-italic-22.png"));
		textItalicButton.setToolTipText(TLanguage.getString("TTextToolBar.ITALIC"));
		fontColorComboBox = new TColorComboBox();
		
		applyFont.addActionListener(new ChangeAttributesListener());
		
		// TODO Add default properties to TSetup
		textBoldButton.setSelected(false);
		textItalicButton.setSelected(false);
		fontColorComboBox.setColor(TBoardConstants.DEFAULT_FOREGROUND);
		
		add(applyFont);
		addSeparator(new Dimension(3, 3));
		add(fontFaceComboBox);
		addSeparator(new Dimension(3, 3));
		add(fontSizeComboBox);
		addSeparator(new Dimension(3, 3));
		add(textBoldButton);
		add(textItalicButton);
		addSeparator(new Dimension(3, 3));
		add(fontColorComboBox);
	}

	/**
	 * Updates the text format components with the text format of the first editors
	 * selected component.
	 */
	public void updateComponents() {
		// The tool bar is going to be enabled if exists a board
		boolean enabled = (editor.getCurrentBoard() != null);

		applyFont.setEnabled(enabled);
		textBoldButton.setEnabled(enabled);
		textItalicButton.setEnabled(enabled);
		if(TEditor.get_android_mode()) fontFaceComboBox.setEnabled(false);
		else fontFaceComboBox.setEnabled(enabled);
		fontSizeComboBox.setEnabled(enabled);
		fontColorComboBox.setEnabled(enabled);

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
				Font font = TBoardConstants.getFont(map);
				// Set them to the tool bar
				if ((font.getStyle() & Font.BOLD) != 0)
					textBoldButton.setSelected(true);
				else
					textBoldButton.setSelected(false);
				if ((font.getStyle() & Font.ITALIC) != 0)
					textItalicButton.setSelected(true);
				else
					textItalicButton.setSelected(false);

				fontFaceComboBox.setFontFace(font.getFamily());
				fontSizeComboBox.setFontSize(font.getSize());
				fontColorComboBox.setColor(TBoardConstants.getForeground(map));
			}
		}
	}

	/**
	 * Creates an <code>AttributeMap</code> with the tool bar's selected text format
	 * parameters.
	 * 
	 * @return The tool bar's text format <code>AttributeMap</code>
	 */
	public AttributeMap getAttributes() {
		AttributeMap attributeMap = new AttributeMap();

		int style = 0;

		if (textBoldButton.isSelected())
			style |= Font.BOLD;
		if (textItalicButton.isSelected())
			style |= Font.ITALIC;

		TBoardConstants.setFont(attributeMap, new Font(fontFaceComboBox
				.getFontFace(), style, fontSizeComboBox.getFontSize()));

		Color fontColor = fontColorComboBox.getColor();

		if (fontColor != null)
			TBoardConstants.setForeground(attributeMap, fontColor);

		return attributeMap;
	}

	// Apply text format button listener
	private class ChangeAttributesListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			((TBoardLayoutCache)editor.getCurrentBoard().getGraphLayoutCache())
					.editRoots(editor.getCurrentBoard().getSelectionCells(),
							getAttributes());
		}
	}
}
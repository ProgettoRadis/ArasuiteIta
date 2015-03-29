/*
 * File: TPolygonDialog.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Mar 6, 2006
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

package tico.editor.dialogs;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import tico.board.TBoardConstants;
import tico.board.components.TComponent;
import tico.components.TBackgroundSelectionPanel;
import tico.components.TBorderSelectionPanel;
import tico.configuration.TLanguage;
import tico.editor.TBoardContainer;
import tico.editor.TEditor;

/**
 * Dialog to change <code>TPolygonDialog</code> attributes.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TPolygonDialog extends TComponentDialog {
	private static String DEFAULT_TITLE = TLanguage.getString("TPolygonDialog.TITLE");

	private JTabbedPane tabbedPane;

	private JPanel componentPropertiesPanel;

	private TBorderSelectionPanel borderSelectionPanel;
	
	private TBackgroundSelectionPanel backgroundSelectionPanel;

	/**
	 * Creates a new <code>TPolygonDialog</code> to edit the <code>polygon</code>
	 * properties.
	 * 
	 * @param boardContainer The <code>boardContainer</code> which contains the
	 * cell to be edited
	 * @param polygon The <code>polygon</code> to be edited
	 */
	public TPolygonDialog(TBoardContainer boardContainer, TComponent polygon) {
		super(boardContainer, DEFAULT_TITLE, polygon);
	}

	/* (non-Javadoc)
	 * @see tico.editor.dialogs.TComponentDialog#setComponentPane()
	 */
	protected JPanel setComponentPane(TEditor editor) {
		JPanel componentPane = new JPanel();

		GridBagConstraints c = new GridBagConstraints();

		componentPane.setLayout(new GridBagLayout());

		createTabbedPane();

		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 0;
		componentPane.add(tabbedPane, c);

		return componentPane;
	}

	private void createTabbedPane() {
		tabbedPane = new JTabbedPane();

		createComponentPropertiesPanel();

		tabbedPane.addTab(TLanguage.getString("TPolygonDialog.TAB_PROPERTIES"),
				componentPropertiesPanel);
	}

	private void createComponentPropertiesPanel() {
		componentPropertiesPanel = new JPanel();

		GridBagConstraints c = new GridBagConstraints();

		componentPropertiesPanel.setLayout(new GridBagLayout());
		
		createBorderSelectionPanel();
		createBackgroundSelectionPanel();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 0;
		componentPropertiesPanel.add(borderSelectionPanel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 1;
		componentPropertiesPanel.add(backgroundSelectionPanel,c);
	}

	private void createBorderSelectionPanel() {
		Map map = getAttributeMap();

		borderSelectionPanel = new TBorderSelectionPanel();

		borderSelectionPanel
				.setBorderColor(TBoardConstants.getBorderColor(map));
		borderSelectionPanel.setBorderSize(Math.max(1, Math
				.round(TBoardConstants.getLineWidth(map))));

	}

	private void createBackgroundSelectionPanel() {
		Map map = getAttributeMap();

		backgroundSelectionPanel = new TBackgroundSelectionPanel();

		backgroundSelectionPanel
				.setBackgroundColor(TBoardConstants.getBackground(map));
		backgroundSelectionPanel.setGradientColor(TBoardConstants.getGradientColor(map));

	}

	/* (non-Javadoc)
	 * @see tico.editor.dialogs.TComponentDialog#newComponentsAttributeMap()
	 */
	protected Map newComponentsAttributeMap() {
		Map nested = new Hashtable();
		Map attributeMap = new Hashtable();
		Vector removalAttributes = new Vector();

		Color color = borderSelectionPanel.getBorderColor();
		if (color != null)
			TBoardConstants.setBorderColor(attributeMap, color);
		else removalAttributes.add(TBoardConstants.BORDERCOLOR);
		
		TBoardConstants.setLineWidth(attributeMap, borderSelectionPanel
				.getBorderSize());
		
		Color background = backgroundSelectionPanel.getBackgroundColor();
		if (background != null)
			TBoardConstants.setBackground(attributeMap, background);
		else removalAttributes.add(TBoardConstants.BACKGROUND);
		
		Color gradient = backgroundSelectionPanel.getGradientColor();
		if (gradient != null)
			TBoardConstants.setGradientColor(attributeMap, gradient);
		else removalAttributes.add(TBoardConstants.GRADIENTCOLOR);
		
		TBoardConstants.setRemoveAttributes(attributeMap,
				removalAttributes.toArray());
		
		nested.put(getComponent(), attributeMap);

		return nested;
	}
}

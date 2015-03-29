/*
 * File: TClickCellActionsPanel.java
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import tico.board.TBoard;
import tico.configuration.TLanguage;


/**
 * Component to choose the click cell actions.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TClickCellActionsPanel extends JPanel {
	// Default component border title
	private static final String DEFAULT_TITLE = TLanguage.getString("TClickCellActionsPanel.TITLE");
	
	// The panel which contains following board selection components
	private JPanel selectBoardPanel;
	// The selection following board combo box
	private JComboBox followingBoardComboBox;
	// The panel which contains accumulate selection components	
	private JPanel accumulatePanel;
	// The accumulate check box
	private JCheckBox accumulateCheckBox;
	// The show controller check box
	private JCheckBox showController;

	/**
	 * Creates a new <code>TClickCellActionsPanel</code> with the elements of
	 * <code>boardList</code> as the following board options.
	 * 
	 * @param boardList The following board options
	 */
	public TClickCellActionsPanel(ArrayList boardList) {
		this(DEFAULT_TITLE, boardList);
	}

	/**
	 * Creates a new <code>TClickCellActionsPanel</code> with the elements of
	 * <code>boardList</code> as the following board options.
	 *
	 * @param title The selected component border title
	 * @param boardList The following board options
	 */
	public TClickCellActionsPanel(String title, ArrayList boardList) {
		super();
		// Creates the border of the component
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), title));
		// Creates the components
		createSelectBoardPanel(boardList);
		createAccumulatePanel();

		// Place the components in the panel
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		// Place selectBoardPanel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 0, 0);
		c.gridx = 0;
		c.gridy = 0;
		add(selectBoardPanel, c);
		// Place accumulatePanel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 0, 0);
		c.gridx = 1;
		c.gridy = 0;
		add(accumulatePanel, c);
	}
	
	// Creates the selectBoardPanel and its internal components
	private void createSelectBoardPanel(ArrayList boardList) {
		selectBoardPanel = new JPanel();
		selectBoardPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		// Create heading label
		JLabel boardLabel = new JLabel(TLanguage.getString("TClickCellActionsPanel.GO_TO"));
		
		// Create board combo box
		followingBoardComboBox = new JComboBox();
		followingBoardComboBox.setPreferredSize(new Dimension(150,20));
		// Create the list of boards for the model
		Vector boards = new Vector();
		boards.add(null);
		boards.addAll(boardList);
		// Add the boards list to the model
		followingBoardComboBox.setModel(new DefaultComboBoxModel(boards));
		// If the boards list is empty, set the component disabled
		followingBoardComboBox.setEnabled(boards.size() > 1);
		
		selectBoardPanel.add(boardLabel);		
		selectBoardPanel.add(followingBoardComboBox);
	}
	
	// Creates the accumulatePanel and its internal components
	private void createAccumulatePanel() {
		accumulatePanel = new JPanel();
		accumulatePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		accumulateCheckBox = new JCheckBox(TLanguage.getString("TClickCellActionsPanel.ACCUMULATE_CELL"));
		
		accumulatePanel.add(accumulateCheckBox);
	}

	/**
	 * Returns the selected <code>followingBoard</code>.
	 * 
	 * @return The selected <code>followingBoard</code>
	 */
	public String getFollowingBoard() {
		return (String)followingBoardComboBox.getSelectedItem();
	}

	/**
	 * Set the <code>followingBoard</code>. 
	 * 
	 * @param followingBoard The <code>followingBoard</code> to set
	 */
	public void setFollowingBoard(String followingBoard) {
		followingBoardComboBox.setSelectedItem(followingBoard);		
	}

	/**
	 * Returns the selected <code>accumulated</code> value.
	 * 
	 * @return The selected <code>accumulated</code> value
	 */
	public boolean getAccumulated() {
		return accumulateCheckBox.isSelected();
	}

	/**
	 * Set the <code>accumulated</code> value. 
	 * 
	 * @param accumulated The <code>accumulated</code> value to set
	 */
	public void setAccumulated(boolean accumulated) {
		accumulateCheckBox.setSelected(accumulated);
	}
}

/*
 * File: TInitialBoardSelectionPanel.java
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

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
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
public class TInitialBoardSelectionPanel extends JPanel {
	// Default component border title
	private static final String DEFAULT_TITLE = TLanguage.getString("TInitialBoardSelectionPanel.TITLE");
	
	// The panel which contains initial board selection components
	private JPanel selectBoardPanel;
	// The selection initial board combo box
	private JComboBox initialBoardComboBox;

	/**
	 * Creates a new <code>TInitialBoardSelectionPanel</code> with the elements of
	 * <code>boardList</code> as the initial board options.
	 * 
	 * @param boardList The initial board options
	 */
	public TInitialBoardSelectionPanel(ArrayList boardList) {
		this(DEFAULT_TITLE, boardList);
	}

	/**
	 * Creates a new <code>TInitialBoardSelectionPanel</code> with the elements of
	 * <code>boardList</code> as the initial board options.
	 *
	 * @param title The selected component border title
	 * @param boardList The initial board options
	 */
	public TInitialBoardSelectionPanel(String title, ArrayList boardList) {
		super();
		// Creates the border of the component
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), title));
		// Creates the components
		createSelectBoardPanel(boardList);

		// Place the components in the panel
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		// Place selectBoardPanel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 0;
		add(selectBoardPanel, c);
	}
	
	// Creates the selectBoardPanel and its internal components
	private void createSelectBoardPanel(ArrayList boardList) {
		selectBoardPanel = new JPanel();
		
		// Create heading label
		JLabel boardLabel = new JLabel(TLanguage.getString("TInitialBoardSelectionPanel.INITIAL_BOARD"));
		
		// Create board combo box
		initialBoardComboBox = new JComboBox();
		initialBoardComboBox.setPreferredSize(new Dimension(150,20));
		// Add the boards list to the model
		initialBoardComboBox.setModel(new DefaultComboBoxModel(boardList.toArray()));
		
		GridBagConstraints c = new GridBagConstraints();
		selectBoardPanel.setLayout(new GridBagLayout());	

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 10, 0);
		c.gridx = 0;
		c.gridy = 0;
		selectBoardPanel.add(boardLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 10, 10);
		c.gridx = 1;
		c.gridy = 0;
		selectBoardPanel.add(initialBoardComboBox, c);
	}

	/**
	 * Returns the selected <code>initialBoard</code>.
	 * 
	 * @return The selected <code>initialBoard</code>
	 */
	public TBoard getInitialBoard() {
		return (TBoard)initialBoardComboBox.getSelectedItem();
	}

	/**
	 * Set the <code>initialBoard</code>. 
	 * 
	 * @param board The <code>initialBoard</code> to set
	 */
	public void setInitialBoard(TBoard board) {
		initialBoardComboBox.setSelectedItem(board);		
	}
}

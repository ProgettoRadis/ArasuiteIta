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
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import tico.board.TBoard;
import tico.board.TBoardConstants;
import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;


/**
 * Component to choose the click cell actions.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TClickControllerCellActionsPanel extends JPanel {
	// Default component border title
	private static final String DEFAULT_TITLE = TLanguage.getString("TClickControllerCellActionsPanel.TITLE");
	
	// The panel which contains following board selection components
	private JPanel selectActionPanel;
	// The selection following board combo box
	private ButtonGroup actions;
	
	private TTextField controllerCellText;
		
	public TTextField getTextField() {
		return controllerCellText;
	}

	public void setTextField(TTextField text) {
		this.controllerCellText = text;
	}

	/**
	 * Creates a new <code>TClickCellActionsPanel</code> with the elements of
	 * <code>boardList</code> as the following board options.
	 * 
	 * @param boardList The following board options
	 */
	public TClickControllerCellActionsPanel(ArrayList boardList, TTextField text) {
		this(DEFAULT_TITLE, boardList, text);
	}

	/**
	 * Creates a new <code>TClickCellActionsPanel</code> with the elements of
	 * <code>boardList</code> as the following board options.
	 *
	 * @param title The selected component border title
	 * @param boardList The following board options
	 */
	public TClickControllerCellActionsPanel(String title, ArrayList boardList, TTextField text) {
		super();
		// Text field
		this.setTextField(text);
		
		// Creates the border of the component
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), title));
		// Creates the components
		createSelectActionPanel(boardList);

		// Place the components in the panel
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		// Place selectActionPanel
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 0, 0);
		c.gridx = 0;
		c.gridy = 0;
		add(selectActionPanel, c);
		
	}
	
	// Creates the selectActionPanel and its internal components
	private void createSelectActionPanel(ArrayList boardList) {
		selectActionPanel = new JPanel();
		GridLayout layoutActionPanel = new GridLayout(4,4);
		layoutActionPanel.setHgap(10);
		selectActionPanel.setLayout(layoutActionPanel);
		
		// Create icons for radio buttons
		
		JLabel exitIcon = new JLabel();
		ImageIcon exit = new ImageIcon((TResourceManager.getImageIcon("controller-exit.png")).getImage().
				getScaledInstance(TBoardConstants.CONTROLLER_IMAGE_SIZE, TBoardConstants.CONTROLLER_IMAGE_SIZE, Image.SCALE_SMOOTH));
		exitIcon.setIcon(exit);
		exitIcon.setText("");
		
		JLabel undoIcon = new JLabel();
		ImageIcon undo = new ImageIcon((TResourceManager.getImageIcon("controller-undo.png")).getImage().
				getScaledInstance(TBoardConstants.CONTROLLER_IMAGE_SIZE, TBoardConstants.CONTROLLER_IMAGE_SIZE, Image.SCALE_SMOOTH));
		undoIcon.setIcon(undo);
		
		JLabel undoAllIcon = new JLabel();
		ImageIcon undoAll = new ImageIcon((TResourceManager.getImageIcon("controller-undo-all.png")).getImage().
				getScaledInstance(TBoardConstants.CONTROLLER_IMAGE_SIZE, TBoardConstants.CONTROLLER_IMAGE_SIZE, Image.SCALE_SMOOTH));
		undoAllIcon.setIcon(undoAll);
		
		JLabel readIcon = new JLabel();
		ImageIcon read = new ImageIcon((TResourceManager.getImageIcon("controller-read.png")).getImage().
				getScaledInstance(TBoardConstants.CONTROLLER_IMAGE_SIZE, TBoardConstants.CONTROLLER_IMAGE_SIZE, Image.SCALE_SMOOTH));
		readIcon.setIcon(read);
		
		JLabel returnIcon = new JLabel();
		ImageIcon back = new ImageIcon((TResourceManager.getImageIcon("controller-return.png")).getImage().
				getScaledInstance(TBoardConstants.CONTROLLER_IMAGE_SIZE, TBoardConstants.CONTROLLER_IMAGE_SIZE, Image.SCALE_SMOOTH));
		returnIcon.setIcon(back);
		
		JLabel homeIcon = new JLabel();
		ImageIcon home = new ImageIcon((TResourceManager.getImageIcon("controller-home.png")).getImage().
				getScaledInstance(TBoardConstants.CONTROLLER_IMAGE_SIZE, TBoardConstants.CONTROLLER_IMAGE_SIZE, Image.SCALE_SMOOTH));
		homeIcon.setIcon(home);
		
		JLabel stopIcon = new JLabel();
		ImageIcon stop = new ImageIcon((TResourceManager.getImageIcon("controller-stop.png")).getImage().
				getScaledInstance(TBoardConstants.CONTROLLER_IMAGE_SIZE, TBoardConstants.CONTROLLER_IMAGE_SIZE, Image.SCALE_SMOOTH));
		stopIcon.setIcon(stop);		
                
                JLabel copyIcon = new JLabel();
                ImageIcon imageIcon = TResourceManager.getImageIcon("controller-copy.png");
		ImageIcon copy = new ImageIcon((imageIcon).getImage().
				getScaledInstance(TBoardConstants.CONTROLLER_IMAGE_SIZE, TBoardConstants.CONTROLLER_IMAGE_SIZE, Image.SCALE_SMOOTH));
		copyIcon.setIcon(copy);	
		
		// Create actions radio buttons
		actions = new ButtonGroup();
		
		String action;
		JRadioButton exitButton = null;
		JRadioButton undoButton = null;
		JRadioButton undoAllButton = null;
		JRadioButton readButton = null;
		JRadioButton returnButton = null;
		JRadioButton homeButton = null;
		JRadioButton stopButton = null;
		JRadioButton copyButton = null;
		
		for (int i=0; i<boardList.size();i++){
			action = boardList.get(i).toString();
			
			if (action.equals(TLanguage.getString("TInterpreterExitAction.NAME"))){
				exitButton = new JRadioButton(action, getTextField().getText().equals(TLanguage.getString("TInterpreterExitAction.NAME")));
				exitButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						controllerCellText.setText(TLanguage.getString("TInterpreterExitAction.NAME"));				
					}});
			}else if (action.equals(TLanguage.getString("TInterpreterUndoAction.NAME"))){
				undoButton = new JRadioButton(action, getTextField().getText().equals(TLanguage.getString("TInterpreterUndoAction.NAME")));
				undoButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						controllerCellText.setText(TLanguage.getString("TInterpreterUndoAction.NAME"));
					}});
			}else if (action.equals(TLanguage.getString("TInterpreterUndoAllAction.NAME"))){
				undoAllButton = new JRadioButton(action, getTextField().getText().equals(TLanguage.getString("TInterpreterUndoAllAction.NAME")));
				undoAllButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {				
						controllerCellText.setText(TLanguage.getString("TInterpreterUndoAllAction.NAME"));
					}});
			}else if (action.equals(TLanguage.getString("TInterpreterReadAction.NAME"))){
				readButton = new JRadioButton(action, getTextField().getText().equals(TLanguage.getString("TInterpreterReadAction.NAME")));
				readButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						controllerCellText.setText(TLanguage.getString("TInterpreterReadAction.NAME"));
					}});
			}else if (action.equals(TLanguage.getString("TInterpreterReturnAction.NAME"))){
				returnButton = new JRadioButton(action, getTextField().getText().equals(TLanguage.getString("TInterpreterReturnAction.NAME")));
				returnButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						controllerCellText.setText(TLanguage.getString("TInterpreterReturnAction.NAME"));
					}});
			}else if (action.equals(TLanguage.getString("TInterpreterHomeAction.NAME"))){
				homeButton = new JRadioButton(action, getTextField().getText().equals(TLanguage.getString("TInterpreterHomeAction.NAME")));
				homeButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						controllerCellText.setText(TLanguage.getString("TInterpreterHomeAction.NAME"));
					}});
			}else if (action.equals(TLanguage.getString("TInterpreterStopAction.NAME"))){
				stopButton = new JRadioButton(action, getTextField().getText().equals(TLanguage.getString("TInterpreterStopAction.NAME")));
				stopButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						controllerCellText.setText(TLanguage.getString("TInterpreterStopAction.NAME"));
					}});
			}else if (action.equals(TLanguage.getString("TInterpreterCopyAction.NAME"))){
                            copyButton = new JRadioButton(action, getTextField().getText().equals(TLanguage.getString("TInterpreterCopyAction.NAME")));
                            copyButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						controllerCellText.setText(TLanguage.getString("TInterpreterCopyAction.NAME"));
					}});
                        }
		}
		
		actions.add(exitButton);
		actions.add(undoButton);
		actions.add(undoAllButton);
		actions.add(readButton);
		actions.add(returnButton);
		actions.add(homeButton);
		actions.add(stopButton);
                actions.add(copyButton);
		
		// Add buttons and icons to panel
		
		selectActionPanel.add(exitIcon);
		selectActionPanel.add(undoIcon);
		selectActionPanel.add(undoAllIcon);
		selectActionPanel.add(readIcon);
		
		selectActionPanel.add(exitButton);
		selectActionPanel.add(undoButton);
		selectActionPanel.add(undoAllButton);
		selectActionPanel.add(readButton);
		
		selectActionPanel.add(returnIcon);
		selectActionPanel.add(stopIcon);
		selectActionPanel.add(homeIcon);
		selectActionPanel.add(copyIcon);
		
		selectActionPanel.add(returnButton);
		selectActionPanel.add(stopButton);
		selectActionPanel.add(homeButton);
		selectActionPanel.add(copyButton);
		
	}
	

}

/*
 * File: TProjectDialog.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Sep 4, 2006
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

import tico.board.TProject;
import tico.components.TInitialBoardSelectionPanel;
import tico.configuration.TLanguage;
import tico.editor.TEditor;

/**
 * Dialog to change <code>TBoard</code> properties.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TProjectDialog extends TPropertiesDialog {

	private TProject currentProject;
	
	private TInitialBoardSelectionPanel initialBoardPanel;
	
	private JPanel orientationPanel;
	
	private String selectedOrientation;

	/**
	 * Creates a new <code>TProjectDialog</code> to edit the editor's
	 * <code>project</code> properties.
	 * 
	 * @param editor The <code>editor</code> which contains the project
	 * properties to be edited
	 */
	public TProjectDialog(TEditor editor) {
		super(editor);
		setTitle(TLanguage.getString("TProjectDialog.TITLE"));

		currentProject = editor.getProject();
		createTabbedPane();

		setVisible(true);
	}

	// Create the main dialog tabbed pane
	private void createTabbedPane() {
		getPropertiesPane().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		JTabbedPane tabbedPane = new JTabbedPane();

		tabbedPane.addTab(TLanguage.getString("TProjectDialog.TAB_PROPERTIES"),
				createPropertiesPanel());

		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 1;
		getPropertiesPane().add(tabbedPane, c);
	}

	// Create the project properties panel
	private JPanel createPropertiesPanel() {
		JPanel preferencesPanel = new JPanel();

		GridBagConstraints c = new GridBagConstraints();
		preferencesPanel.setLayout(new GridBagLayout());

		createInitialBoardPanel();
		createOrientationPanel();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 0;
		preferencesPanel.add(initialBoardPanel, c);
		
		if(TEditor.get_android_mode()){
			c.fill = GridBagConstraints.HORIZONTAL;
			c.insets = new Insets(5, 10, 10, 10);
			c.gridx = 0;
			c.gridy = 1;
			preferencesPanel.add(orientationPanel,c);
		}
		
		return preferencesPanel;
	}
	
	// Creates the initial board panel
	private void createInitialBoardPanel() {
		ArrayList boardList = currentProject.getBoardList();
		
		initialBoardPanel = new TInitialBoardSelectionPanel(boardList);
		
		initialBoardPanel.setInitialBoard(currentProject.getInitialBoard());
	}
	
	private void createOrientationPanel() {
		orientationPanel = new JPanel();
		orientationPanel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)),
				TLanguage.getString("TProjectDialog.ORIENTATION"))); //"orientacion"));
		
		JRadioButton portraitMode = new JRadioButton(TLanguage.getString("TProjectDialog.PORTRAIT"));
		//portraitMode.setActionCommand("portraitCommand"/*TEditorConstants.AUTOMATIC_SCANNING_MODE*/);
		portraitMode.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				selectedOrientation="portrait";//TEditor.set_android_orientation("portrait");
			}});
		if (TEditor.get_android_orientation().equals("portrait")){
			portraitMode.setSelected(true);
			selectedOrientation="portrait";
		}
		
		JRadioButton landscapeMode = new JRadioButton(TLanguage.getString("TProjectDialog.LANDSCAPE"));
		//landscapeMode.setActionCommand("landscapeCommand");//TInterpreterConstants.DIRECT_SELECTION_MODE);
		
		landscapeMode.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				selectedOrientation="landscape";//TEditor.set_android_orientation("landscape");
			}});
		if (TEditor.get_android_orientation().equals("landscape")){
			landscapeMode.setSelected(true);
			selectedOrientation="landscape";
		}
		
		JRadioButton freeMode = new JRadioButton(TLanguage.getString("TProjectDialog.FREE"));
		//freeMode.setActionCommand("freeCommand");//TInterpreterConstants.DIRECT_SELECTION_MODE);
		
		freeMode.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				selectedOrientation="free";//TEditor.set_android_orientation("landscape");
			}});
		if (TEditor.get_android_orientation().equals("free")){
			freeMode.setSelected(true);
			selectedOrientation="free";
		}
		
		
				
		ButtonGroup orientationsButtonGroup = new ButtonGroup();
		orientationsButtonGroup.add(portraitMode);
		orientationsButtonGroup.add(landscapeMode);
		orientationsButtonGroup.add(freeMode);
		
		orientationPanel.add(portraitMode);
		orientationPanel.add(landscapeMode);
		orientationPanel.add(freeMode);
	}

	/* (non-Javadoc)
	 * @see tico.editor.dialogs.TPropertiesDialog#applyValues()
	 */
	protected boolean applyValues() {
		if (!currentProject.getInitialBoard().equals(initialBoardPanel.getInitialBoard()))
			currentProject.setInitialBoard(initialBoardPanel.getInitialBoard());
		
		//anyadimos orientacion en modo android
		if(TEditor.get_android_mode() && !TEditor.get_android_orientation().equals(selectedOrientation)){
			TEditor.set_android_orientation(selectedOrientation);
			
			//para modificar proyecto
			currentProject.setInitialBoard(initialBoardPanel.getInitialBoard());
		}
		
		return true;
	}
}

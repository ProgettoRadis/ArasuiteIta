/*
 * File: TControllerCellDialog.java
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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import tico.board.TBoardConstants;
import tico.board.components.TComponent;
import tico.components.TAlternativeSoundChooser;
import tico.components.TClickControllerCellActionsPanel;
import tico.components.TFontModelChooser;
import tico.components.TIdTextField;
import tico.components.TTextField;
import tico.configuration.TLanguage;
import tico.editor.TBoardContainer;
import tico.editor.TEditor;
import tico.editor.TFileHandler;

/**
 * Dialog to change <code>TControllerCellDialog</code> attributes.
 * 
 * @author Pablo Mu�oz
 * @version 1.0 Nov 20, 2006
 */
public class TControllerCellDialog extends TComponentDialog {
	
	private static String DEFAULT_TITLE = TLanguage.getString("TControllerCellDialog.TITLE");

	// Tabbed pane which contains all the other cell properties panes
	protected JTabbedPane tabbedPane;

	// Text properties panel
	private JPanel textPropertiesPanel;

	private JPanel textFieldPanel;

	private TTextField textField;

	protected JPanel idFieldPanel;

	protected TIdTextField idTextField;

	private TFontModelChooser fontModel;

	// Actions panel
	private JPanel componentActionsPanel;
	
	private TClickControllerCellActionsPanel clickControllerCellActionPanel;
	
	private TAlternativeSoundChooser alternativeSoundChooser;
		
	private TEditor myEditor;
	
	// Action icons
	
	private ImageIcon exitIcon;
	private ImageIcon undoIcon;
	private ImageIcon undoAllIcon;
	private ImageIcon readIcon;
	private ImageIcon returnIcon;
	private ImageIcon homeIcon;
	private ImageIcon stopIcon;
        private ImageIcon copyIcon;
	
	String exitFilePath;
	String undoFilePath;
	String undoAllFilePath;
	String readFilePath;
	String returnFilePath;
	String homeFilePath;
	String stopFilePath;
        String copyFilePath;
	
	/**
	 * Creates a new <code>TCellDialog</code> to edit the <code>cell</code>
	 * properties.
	 * 
	 * @param boardContainer The <code>boardContainer</code> which contains the
	 * cell to be edited
	 * @param cell The <code>cell</code> to be edited
	 */
	public TControllerCellDialog(TBoardContainer boardContainer, TComponent cell) {
		this(boardContainer, DEFAULT_TITLE, cell);
		myEditor = boardContainer.getEditor();	
	}
	
	/**
	 * Creates a new <code>TCellDialog</code> to edit the <code>cell</code>
	 * properties.
	 * 
	 * @param boardContainer The <code>boardContainer</code> which contains the
	 * cell to be edited
	 * @param title The <code>title</code> of the dialog
	 * @param cell The <code>cell</code> to be edited
	 */
	public TControllerCellDialog(TBoardContainer boardContainer, String title,
			TComponent cell) {
		super(boardContainer, title, cell);
		myEditor = boardContainer.getEditor();
	}

	// Creates the main dialog pane
	protected JPanel setComponentPane(TEditor editor) {
		JPanel componentPane = new JPanel();
		myEditor = editor;

		GridBagConstraints c = new GridBagConstraints();

		componentPane.setLayout(new GridBagLayout());
		
		createTabbedPane();

		createIdField();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 0;
		componentPane.add(idFieldPanel, c);

		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 1;
		componentPane.add(tabbedPane, c);

		return componentPane;
	}

	// Creates the cell id field
	protected void createIdField() {
		idFieldPanel = new JPanel();

		idFieldPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		idFieldPanel.add(new JLabel(TLanguage.getString("TControllerCellDialog.ID")));

		idTextField = new TIdTextField();
		
		idFieldPanel.add(idTextField);

		idTextField.setText(TBoardConstants.getId(getAttributeMap()));
	}

	// Creates the main dialog tabbed pane
	protected void createTabbedPane() {
		tabbedPane = new JTabbedPane();

		// Create properties panels
		createTextPropertiesPanel();
		createActionsPanel();
		// Add properties panels to the tabbed pane
		tabbedPane.addTab(TLanguage.getString("TControllerCellDialog.TAB_TEXT"), textPropertiesPanel);
		tabbedPane.addTab(TLanguage.getString("TControllerCellDialog.TAB_ACTIONS"),
				componentActionsPanel);
	}
	
	
	// Creates the text properties panel for the tabbed pane
	private void createTextPropertiesPanel() {
		textPropertiesPanel = new JPanel();

		GridBagConstraints c = new GridBagConstraints();

		textPropertiesPanel.setLayout(new GridBagLayout());

		createTextField();
		createFontModel();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 0;
		textPropertiesPanel.add(textFieldPanel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 1;
		textPropertiesPanel.add(fontModel, c);
	}

	// Creates the actions panel for the tabbed pane
	private void createActionsPanel() {
		componentActionsPanel = new JPanel();

		GridBagConstraints c = new GridBagConstraints();

		componentActionsPanel.setLayout(new GridBagLayout());
		
		createAlternativeSoundChooser();//alternative sound
		
		ArrayList controllerActionsList = new ArrayList();
		controllerActionsList.add(TLanguage.getString("TInterpreterExitAction.NAME"));
		controllerActionsList.add(TLanguage.getString("TInterpreterUndoAction.NAME"));
		controllerActionsList.add(TLanguage.getString("TInterpreterUndoAllAction.NAME"));
		controllerActionsList.add(TLanguage.getString("TInterpreterReadAction.NAME"));
		controllerActionsList.add(TLanguage.getString("TInterpreterReturnAction.NAME"));
		controllerActionsList.add(TLanguage.getString("TInterpreterHomeAction.NAME"));
		controllerActionsList.add(TLanguage.getString("TInterpreterStopAction.NAME"));
                controllerActionsList.add(TLanguage.getString("TInterpreterCopyAction.NAME"));
		
		//Create the icon paths
		
		String currentDirectory = System.getProperty("user.dir");
		exitFilePath = currentDirectory + File.separator + "controller-icons"+ File.separator + "controller-exit.png";
		undoFilePath = currentDirectory + File.separator + "controller-icons"+ File.separator + "controller-undo.png";
		undoAllFilePath = currentDirectory + File.separator + "controller-icons"+ File.separator + "controller-undo-all.png";
		readFilePath = currentDirectory + File.separator + "controller-icons"+ File.separator + "controller-read.png";
		returnFilePath = currentDirectory + File.separator + "controller-icons"+ File.separator + "controller-return.png";
		homeFilePath = currentDirectory + File.separator + "controller-icons"+ File.separator + "controller-home.png";
		stopFilePath = currentDirectory + File.separator + "controller-icons"+ File.separator + "controller-stop.png";
                copyFilePath = currentDirectory + File.separator + "controller-icons"+ File.separator + "controller-copy.png";

		clickControllerCellActionPanel = new TClickControllerCellActionsPanel(controllerActionsList, textField);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 0;
		componentActionsPanel.add(clickControllerCellActionPanel, c);
		
		//anyadido alternativesound
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 1;
		componentActionsPanel.add(alternativeSoundChooser, c);
	}
	
	// Creates the cell text field
	private void createTextField() {
		Map map = getAttributeMap();

		textFieldPanel = new JPanel();

		textFieldPanel.setBorder(new TitledBorder(BorderFactory
				.createEtchedBorder(Color.WHITE, new Color(165, 163, 151)),
				TLanguage.getString("TControllerCellDialog.TEXT_FILED")));
		
		int actionCode = TBoardConstants.getActionCode(map);
		
		textField = new TTextField(TBoardConstants.getText(map));
		if (textField.getText().equals(""))
			textField = new TTextField(TLanguage.getString("TControllerCellDialog.TEXT_DEFAULT"));
		else{
			if (actionCode == TBoardConstants.EXIT_ACTION_CODE){
				textField = new TTextField(TLanguage.getString("TInterpreterExitAction.NAME"));
			}else if (actionCode == TBoardConstants.UNDO_ACTION_CODE){
				textField = new TTextField(TLanguage.getString("TInterpreterUndoAction.NAME"));
			}else if (actionCode == TBoardConstants.UNDO_ALL_ACTION_CODE){
				textField = new TTextField(TLanguage.getString("TInterpreterUndoAllAction.NAME"));
			}else if (actionCode == TBoardConstants.READ_ACTION_CODE){
				textField = new TTextField(TLanguage.getString("TInterpreterReadAction.NAME"));
			}else if (actionCode == TBoardConstants.RETURN_ACTION_CODE){
				textField = new TTextField(TLanguage.getString("TInterpreterReturnAction.NAME"));
			}else if (actionCode == TBoardConstants.HOME_ACTION_CODE){
				textField = new TTextField(TLanguage.getString("TInterpreterHomeAction.NAME"));
			}else if (actionCode == TBoardConstants.STOP_ACTION_CODE){
				textField = new TTextField(TLanguage.getString("TInterpreterStopAction.NAME"));
			}else if(actionCode == TBoardConstants.COPY_ACTION_CODE){
                            textField = new TTextField(TLanguage.getString("TInterpreterCopyAction.NAME"));
                        }
		}
		
		textField.setColumns(30);
		textField.setEditable(false);

		textFieldPanel.add(textField);
	}
	
	// Creates the font model selection panel
	private void createFontModel() {
		Map map = getAttributeMap();

		fontModel = new TFontModelChooser(TBoardConstants.getFont(map)
				.getName(), TBoardConstants.getForeground(map), TBoardConstants
				.getFont(map).getSize(), TBoardConstants.getFont(map)
				.getStyle());
	}
	
	
	/* (non-Javadoc)
	 * @see tico.editor.dialogs.TComponentDialog#newComponentsAttributeMap()
	 */
	protected Map newComponentsAttributeMap() {
		// Create used variables
		Map nested = new Hashtable();
		Map attributeMap = new Hashtable();
		Vector removalAttributes = new Vector();
		File f = null;

		// Set cell text and format
		TBoardConstants.setText(attributeMap, textField.getText());

		TBoardConstants.setForeground(attributeMap, fontModel.getFontColor());
		TBoardConstants.setFont(attributeMap, new Font(fontModel.getFontFace(),
				fontModel.getFontStyle(), fontModel.getFontSize()));
		TBoardConstants.setBackground(attributeMap, TBoardConstants.DEFAULT_BACKGROUND);

		// Set cell static image and other image properties
		if (textField.getText().equals(TLanguage.getString("TInterpreterExitAction.NAME"))){
			try {
				f = TFileHandler.importFile(exitFilePath);
				exitIcon = new ImageIcon(f.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			TBoardConstants.setIcon(attributeMap, exitIcon);
			TBoardConstants.setActionCode(attributeMap, TBoardConstants.EXIT_ACTION_CODE);
		}
		else if (textField.getText().equals(TLanguage.getString("TInterpreterUndoAction.NAME"))){
			try {
				f = TFileHandler.importFile(undoFilePath);
				undoIcon = new ImageIcon(f.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			TBoardConstants.setIcon(attributeMap, undoIcon);
			TBoardConstants.setActionCode(attributeMap, TBoardConstants.UNDO_ACTION_CODE);
		}
		else if (textField.getText().equals(TLanguage.getString("TInterpreterUndoAllAction.NAME"))){
			try {
				f = TFileHandler.importFile(undoAllFilePath);
				undoAllIcon = new ImageIcon(f.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			TBoardConstants.setIcon(attributeMap, undoAllIcon);
			TBoardConstants.setActionCode(attributeMap, TBoardConstants.UNDO_ALL_ACTION_CODE);
		}
		else if (textField.getText().equals(TLanguage.getString("TInterpreterReadAction.NAME"))){
			try {
				f = TFileHandler.importFile(readFilePath);
				readIcon = new ImageIcon(f.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			TBoardConstants.setIcon(attributeMap, readIcon);
			TBoardConstants.setActionCode(attributeMap, TBoardConstants.READ_ACTION_CODE);
		}
		else if (textField.getText().equals(TLanguage.getString("TInterpreterReturnAction.NAME"))){
			try {
				f = TFileHandler.importFile(returnFilePath);
				returnIcon = new ImageIcon(f.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			TBoardConstants.setIcon(attributeMap, returnIcon);
			TBoardConstants.setActionCode(attributeMap, TBoardConstants.RETURN_ACTION_CODE);
		}
		else if (textField.getText().equals(TLanguage.getString("TInterpreterHomeAction.NAME"))){
			try {
				f = TFileHandler.importFile(homeFilePath);
				homeIcon = new ImageIcon(f.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			TBoardConstants.setIcon(attributeMap, homeIcon);
			TBoardConstants.setActionCode(attributeMap, TBoardConstants.HOME_ACTION_CODE);
		}
		else if (textField.getText().equals(TLanguage.getString("TInterpreterStopAction.NAME"))){
			try {
				f = TFileHandler.importFile(stopFilePath);
				stopIcon = new ImageIcon(f.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			TBoardConstants.setIcon(attributeMap, stopIcon);
			TBoardConstants.setActionCode(attributeMap, TBoardConstants.STOP_ACTION_CODE);
		}
		else if (textField.getText().equals(TLanguage.getString("TInterpreterCopyAction.NAME"))){
			try {
				f = TFileHandler.importFile(copyFilePath);
				copyIcon = new ImageIcon(f.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			TBoardConstants.setIcon(attributeMap, copyIcon);
			TBoardConstants.setActionCode(attributeMap, TBoardConstants.COPY_ACTION_CODE);
		}
                
                
		else {
			try {
				f = TFileHandler.importFile(exitFilePath);
				exitIcon = new ImageIcon(f.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			TBoardConstants.setIcon(attributeMap, exitIcon);
		}
		
		TBoardConstants.setVerticalTextPosition(attributeMap, SwingConstants.BOTTOM);
			
			
		// Apply removal attributes
		TBoardConstants.setRemoveAttributes(attributeMap, removalAttributes.toArray());

		// Set cell id
		TBoardConstants.setId(attributeMap, idTextField.getText());
		
		
		// Set cell alternative sound file
		String alternativeSoundFile = alternativeSoundChooser.getSoundFilePath();
		if (alternativeSoundFile != null)
			TBoardConstants.setAlternativeSoundFile(attributeMap, alternativeSoundFile);
		else
			removalAttributes.add(TBoardConstants.SOUND_FILE);

		nested.put(getComponent(), attributeMap);

		return nested;
	}
	
	
	
	// Creates the alternative sound chooser panel
	private void createAlternativeSoundChooser() {
		Map map = getAttributeMap();

		alternativeSoundChooser = new TAlternativeSoundChooser();

		alternativeSoundChooser.setSoundFilePath(TBoardConstants.getAlternativeSoundFile(map));
	}

	
}
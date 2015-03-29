/*
 * File: TBoardDialog.java
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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import tico.board.TBoardConstants;
import tico.board.TBoardModel;
import tico.components.TIdTextField;
import tico.components.TSizeChooser;
import tico.configuration.TLanguage;
import tico.configuration.TSetup;
import tico.editor.TBoardContainer;
import tico.editor.TEditor;

/**
 * Dialog to change <code>TBoard</code> properties.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class newAndroidOptions extends TPropertiesDialog {

	

	protected TIdTextField nameTextField;
	
	private JPanel orientationPanel;

	private String selectedOrientation;
	
	private TSizeChooser sizeChooser;

	
	
	private TEditor myEditor;
	
	// Check if this is the first edit of the board. This is necesary to apply the
	// selected properties without exiting the dialog but maintaining the undo/redo
	// board's sequence
	private boolean firstEdit = true;

	/**
	 * Creates a new <code>TBoardDialog</code> to edit the <code>boardContainer</code>
	 * board's properties.
	 * 
	 * @param boardContainer The <code>boardContainer</code> which contains the board
	 * properties to be edited
	 */
	public newAndroidOptions(TBoardContainer boardContainer) {
		super(boardContainer.getEditor());
		
		myEditor = boardContainer.getEditor();
		
		setTitle(TLanguage.getString("newAndroidOptions.TITLE"));//////////////////////////////////////////////////////////////

		createTabbedPane();

		setVisible(true);
	}

	// Create the main dialog tabbed pane
	private void createTabbedPane() {
		getPropertiesPane().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
	
		JTabbedPane tabbedPane = new JTabbedPane();

		tabbedPane.addTab(TLanguage.getString("TBoardDialog.TAB_PROPERTIES"),
				createPropertiesPanel());
/*		tabbedPane.addTab(TLanguage.getString("TBoardDialog.TAB_BROWSE_ORDER"),
				createOrderPanel());*/

		/*c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 0;
		getPropertiesPane().add(nameFieldPanel, c);*/
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 1;
		getPropertiesPane().add(tabbedPane, c);
	}
	
	
	
	// Create the board properties panel
	private JPanel createPropertiesPanel() {
		JPanel propertiesPanel = new JPanel();

		GridBagConstraints c = new GridBagConstraints();

		propertiesPanel.setLayout(new GridBagLayout());

		createSizeChooser();
		createOrientationPanel();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 0;
		propertiesPanel.add(sizeChooser, c);

		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 1;
		propertiesPanel.add(orientationPanel, c);
		
		return propertiesPanel;
	}
	
	// Creates the board size chooser
	private void createSizeChooser() {
		Map map = ((TBoardModel)getBoard().getModel()).getAttributes();

		sizeChooser = new TSizeChooser();
		JLabel texto=new JLabel(TLanguage.getString("newAndroidOptions.SIZE_INSTRUCTIONS"));
		//JLabel texto=new JLabel("<html>Consulte el tamaño adecuado de pantalla  <br />en TICO4Android pulsando  <br />menú->acciones->ver resoluciones</html>");
		
		texto.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints c = new GridBagConstraints();
		//setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(0, 0, 0, 0);
		c.weightx = 0.5;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 1;
		
		c.gridwidth=4;
		sizeChooser.add(texto,c);
		
		sizeChooser.setSelectedSize(TBoardConstants.getSize(map));
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
		Map nested = new Hashtable();
		Map attributeMap = new Hashtable();
		Vector removalAttributes = new Vector();

		
		
		// Set dimension
		Dimension board_dimension=sizeChooser.getSelectedSize();
		TBoardConstants.setSize(attributeMap, board_dimension);//sizeChooser.getSelectedSize());
		TEditor.set_board_height(board_dimension.height);
		TEditor.set_board_width(board_dimension.width);
		TSetup.setBoardHeight(board_dimension.height);
		TSetup.setBoardWidth(board_dimension.width);
		
		
		
		//Set android orientation
		TEditor.set_android_orientation(selectedOrientation);
		TSetup.setOrientation(selectedOrientation);
		
		
		// Set the removal attributes
		TBoardConstants.setRemoveAttributes(attributeMap, removalAttributes
				.toArray());

		nested.put(getBoard().getModel(), attributeMap);

		

		getBoard().getGraphLayoutCache().edit(nested);

		firstEdit = false;

		getBoard().updateUI();
		
		
		//save the configuration file
		try {
			TSetup.save();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return true;
	}
}

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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.CellView;



import tico.board.TBoardConstants;
import tico.board.TBoardModel;
import tico.board.components.TCell;
import tico.board.components.TGridCell;
import tico.board.componentview.TCellView;
import tico.board.componentview.TComponentView;
import tico.board.componentview.TGridCellView;
import tico.components.TBackgroundSelectionPanel;
import tico.components.TBoardOrderPanel;
import tico.components.TIdTextField;
import tico.components.TImageChooser;
import tico.components.TSizeChooser;
import tico.components.TSoundChooser;
import tico.configuration.TLanguage;
import tico.editor.TBoardContainer;
import tico.editor.TEditor;

/**
 * Dialog to change <code>TBoard</code> properties.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TBoardDialog extends TPropertiesDialog {

	private JPanel nameFieldPanel;

	protected TIdTextField nameTextField;
	
	private TBackgroundSelectionPanel backgroundSelectionPanel;

	private TBoardOrderPanel boardOrderPanel;

	private TSizeChooser sizeChooser;

	private TImageChooser iconChooser;
	
	private TSoundChooser soundChooser;
	
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
	public TBoardDialog(TBoardContainer boardContainer) {
		super(boardContainer.getEditor());
		
		myEditor = boardContainer.getEditor();
		
		setTitle(TLanguage.getString("TBoardDialog.TITLE"));

		createTabbedPane();

		setVisible(true);
	}

	// Create the main dialog tabbed pane
	private void createTabbedPane() {
		getPropertiesPane().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		createNameField();
		JTabbedPane tabbedPane = new JTabbedPane();

		tabbedPane.addTab(TLanguage.getString("TBoardDialog.TAB_PROPERTIES"),
				createPropertiesPanel());
		tabbedPane.addTab(TLanguage.getString("TBoardDialog.TAB_BROWSE_ORDER"),
				createOrderPanel());

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 0;
		getPropertiesPane().add(nameFieldPanel, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 1;
		getPropertiesPane().add(tabbedPane, c);
	}
	
	// Creates the board name input field
	private void createNameField() {
		nameFieldPanel = new JPanel();

		nameFieldPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		nameFieldPanel.add(new JLabel(TLanguage.getString("TBoardDialog.BOARD_NAME")));

		nameTextField = new TIdTextField();
		nameFieldPanel.add(nameTextField);

		nameTextField.setText(getBoard().getBoardName());
	}
	
	// Create the board properties panel
	private JPanel createPropertiesPanel() {
		JPanel propertiesPanel = new JPanel();

		GridBagConstraints c = new GridBagConstraints();

		propertiesPanel.setLayout(new GridBagLayout());

		createSizeChooser();
		createBackgroundSelectionPanel();
		createIconChooser();
		createSoundChooser();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 0;
		propertiesPanel.add(sizeChooser, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 1;
		propertiesPanel.add(backgroundSelectionPanel, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 2;
		propertiesPanel.add(soundChooser, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 3;
		propertiesPanel.add(iconChooser, c);

		return propertiesPanel;
	}

	// Creates the board order panel
	private JPanel createOrderPanel() {
		Map map = ((TBoardModel)getBoard().getModel()).getAttributes();

		JPanel orderPanel = new JPanel();

		GridBagConstraints c = new GridBagConstraints();

		orderPanel.setLayout(new GridBagLayout());

		boardOrderPanel = new TBoardOrderPanel();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 0;
		orderPanel.add(boardOrderPanel, c);

		boardOrderPanel.setUnorderedList(TBoardConstants
				.getUnorderedCellList(map));
		boardOrderPanel.setOrderedList(TBoardConstants
				.getOrderedCellList(map));

		return orderPanel;
	}
	
	// Creates the board size chooser
	private void createSizeChooser() {
		Map map = ((TBoardModel)getBoard().getModel()).getAttributes();

		sizeChooser = new TSizeChooser();

		sizeChooser.setSelectedSize(TBoardConstants.getSize(map));
	}

	// Creates the background selection chooser
	private void createBackgroundSelectionPanel() {
		Map map = ((TBoardModel)getBoard().getModel()).getAttributes();

		backgroundSelectionPanel = new TBackgroundSelectionPanel(false);

		backgroundSelectionPanel.setBackgroundColor(TBoardConstants
				.getBackground(map));
		backgroundSelectionPanel.setGradientColor(TBoardConstants
				.getGradientColor(map));
	}
	private void createSoundChooser()
	{
		Map map = ((TBoardModel)getBoard().getModel()).getAttributes();
	
		soundChooser = new TSoundChooser();

		soundChooser.setSoundFilePath(TBoardConstants.getSoundFile(map));
		
		
	}
	// Creates the background image chooser
	private void createIconChooser() {
		Map map = ((TBoardModel)getBoard().getModel()).getAttributes();

		iconChooser = new TImageChooser(TImageChooser.RESIZE_STYLE_TYPE, myEditor);
		
		iconChooser.setIcon((ImageIcon)TBoardConstants.getIcon(map));

		iconChooser.setResizeStyle(TBoardConstants.getImageResizeStyle(map));
	}

	/* (non-Javadoc)
	 * @see tico.editor.dialogs.TPropertiesDialog#applyValues()
	 */
	protected boolean applyValues() {
		Map nested = new Hashtable();
		Map attributeMap = new Hashtable();
		Vector removalAttributes = new Vector();

		// Set the name
		String newName = nameTextField.getText();
		if (newName != null) {
			// An empty name can not exists
			if (newName.equals("")) {
				JOptionPane.showMessageDialog(null,
						TLanguage.getString("TBoardDialog.ERROR_EMPTY_NAME"),
						TLanguage.getString("WARNING") + "!",
						JOptionPane.WARNING_MESSAGE);
				return false;
			}

			// If the name is repeated
			if (getBoardContainer().getEditor().getProject()
					.isRepeatedName(getBoardContainer().getBoard(),newName)) {
				JOptionPane.showMessageDialog(null,
						TLanguage.getString("TBoardDialog.ERROR_REPEATED_NAME"),
						TLanguage.getString("WARNING") + "!",
						JOptionPane.WARNING_MESSAGE);
				return false;
			}
		}
		String oldBoardName = getBoard().getBoardName();
		getBoard().setBoardName(newName);
		
		// Change followingBoard attribute on cells which go to this board
		for (int i=0; i<myEditor.getBoardContainerCount(); i++){
			TBoardContainer boardContainer = myEditor.getBoardContainer(i);
			CellView[] components = boardContainer.getBoard().getGraphLayoutCache().getCellViews();
			for (int j=0; j<components.length; j++){
				if(components[j] instanceof TComponentView){//anyadido por gridcell
					TComponentView view = (TComponentView) components[j];
					if (view instanceof TCellView){
						
						TCell cell = (TCell) view.getCell();
						AttributeMap cellAttributes = cell.getAttributes();
						if (TBoardConstants.getFollowingBoardName(cellAttributes)!= null){
							if (TBoardConstants.getFollowingBoardName(cellAttributes)==oldBoardName){
								TBoardConstants.setFollowingBoard(cellAttributes, newName);
								cell.setAttributes(cellAttributes);
							}
						}
					}
					/*anyadido gridcellview*
					else if (view instanceof TGridCellView){
						//System.out.println("hay grid cell"+components.length);
						TGridCell cell = (TGridCell) view.getCell();
						AttributeMap cellAttributes = cell.getAttributes();
						if (TBoardConstants.getFollowingBoardName(cellAttributes)!= null){
							if (TBoardConstants.getFollowingBoardName(cellAttributes)==oldBoardName){
								TBoardConstants.setFollowingBoard(cellAttributes, newName);
								cell.setAttributes(cellAttributes);
							}
						}
					}
					*fin anyadido*/
				}
			}
		}
		
		// Set dimension
		Dimension board_dimension=sizeChooser.getSelectedSize();
		TBoardConstants.setSize(attributeMap, board_dimension);//sizeChooser.getSelectedSize());
		TEditor.set_board_height(board_dimension.height);
		TEditor.set_board_width(board_dimension.width);
		
		// Set background color
		Color background = backgroundSelectionPanel.getBackgroundColor();
		
		//anyadimos guardar color de fondo
		TEditor.set_board_background_color(background);
		
		if (background != null)
			TBoardConstants.setBackground(attributeMap, background);
		else
			removalAttributes.add(TBoardConstants.BACKGROUND);
		
		// Set sound
		
		String soundFile = soundChooser.getSoundFilePath();
		if (soundFile != null)
			TBoardConstants.setSoundFile(attributeMap, soundFile);
		else
			removalAttributes.add(TBoardConstants.SOUND_FILE);
		
		// Set gradient color
		Color gradient = backgroundSelectionPanel.getGradientColor();
		//anyadimos guardar color de fondo
		TEditor.set_board_gradient_color(gradient);
		if (gradient != null)
			TBoardConstants.setGradientColor(attributeMap, gradient);
		else
			removalAttributes.add(TBoardConstants.GRADIENTCOLOR);

		// Set icon
		ImageIcon icon = iconChooser.getIcon();
		if (icon != null){
			TBoardConstants.setIcon(attributeMap, icon);
		}
		else
			removalAttributes.add(TBoardConstants.ICON);

		// Set image resize style
		TBoardConstants.setImageResizeStyle(attributeMap, iconChooser
				.getResizeStyle());

		// Set order lists
		TBoardConstants.setOrderedCellList(attributeMap, boardOrderPanel
				.getOrderedList());
		TBoardConstants.setUnorderedCellList(attributeMap, boardOrderPanel
				.getUnorderedList());

		// Set the removal attributes
		TBoardConstants.setRemoveAttributes(attributeMap, removalAttributes
				.toArray());

		nested.put(getBoard().getModel(), attributeMap);

		if (!firstEdit)
			getBoardContainer().getUndoManager().undo();

		getBoard().getGraphLayoutCache().edit(nested);

		firstEdit = false;

		getBoard().updateUI();

		return true;
	}
}

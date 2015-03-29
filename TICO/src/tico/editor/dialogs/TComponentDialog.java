/*
 * File: TComponentDialog.java
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

import java.util.Iterator;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import tico.board.TBoardConstants;
import tico.board.TBoardModel;
import tico.board.components.TComponent;
import tico.configuration.TLanguage;
import tico.editor.TBoardContainer;
import tico.editor.TEditor;

/**
 * Dialog to change <code>TComponent</code> properties. This is a base abstract
 * dialog needed to create a specific component dialog.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
abstract public class TComponentDialog extends TPropertiesDialog {
	// The editing component
	private TComponent component;

	// Checks if the editing component has been already edited by this dialog This is
	// necesary to apply the selected properties without exiting the dialog but
	// maintaining the undo/redo board's sequence
	private boolean firstEdit = true;

	/**
	 * Creates a new <code>TComponentDialog</code> to edit the <code>component</code>
	 * properties.
	 * 
	 * @param boardContainer The <code>boardContainer</code> which contains the component
	 * to be edited
	 * @param title The <code>title</code> of the dialog
	 * @param component The <code>component</code> to be edited
	 */
	public TComponentDialog(TBoardContainer boardContainer, String title,
			TComponent component) {
		super(boardContainer.getEditor());

		this.component = component;

		setTitle(title);

		getPropertiesPane().add(setComponentPane(boardContainer.getEditor()));

		setVisible(true);
	}

	// Apply the values to the editing component
	protected boolean applyValues() {
		// If I have already applied values in this dialog I have to
		// to undo the previous edit
		if (!firstEdit)
			getBoardContainer().getUndoManager().undo();

		// Get the component attribute modification
		Map nested = newComponentsAttributeMap();

		// Check if the id that we are going to change is not repeated
		Iterator componentIterator = nested.entrySet().iterator();
		while (componentIterator.hasNext()) {
			Map.Entry entry = (Map.Entry)componentIterator.next();

			// Get the component and the new attributeMap
			TComponent component = (TComponent)entry.getKey();
			Map attributeMap = (Map)entry.getValue();

			// If the id is going to be changed or cleaned
			String newId = TBoardConstants.getId(attributeMap);
			if (newId != null) {
				// An empty id can not exists
				if (newId.equals("")) {
					JOptionPane.showMessageDialog(null,
							TLanguage.getString("TComponentDialog.ERROR_EMPTY_ID"),
							TLanguage.getString("WARNING") + "!",
							JOptionPane.WARNING_MESSAGE);
					return false;
				}

				// If the id is repeated
				if (((TBoardModel)getBoard().getGraphLayoutCache().getModel())
						.isRepeatedId(component, newId)) {
					JOptionPane.showMessageDialog(null,
							TLanguage.getString("TComponentDialog.ERROR_REPEATED_ID"),
							TLanguage.getString("WARNING") + "!",
							JOptionPane.WARNING_MESSAGE);
					return false;
				}
			}
		}

		// TODO Check if any change has been made

		// Apply the changes
		getBoard().getGraphLayoutCache().edit(nested);
		// From now, this is not the first edit in the dialog
		firstEdit = false;

		return true;
	}

	/**
	 * Returns the editing <code>component</code>.
	 * 
	 * @return The editing <code>component</code>
	 */
	protected TComponent getComponent() {
		return this.component;
	}

	/**
	 * Returns the editing component's <code>attributeMap</code>.
	 * 
	 * @return The editing component's <code>attributeMap</code>
	 */
	protected Map getAttributeMap() {
		return getComponent().getAttributes();
	}

	/**
	 * Hook method to allow subclasseres to set the component properties editing pane.
	 * 
	 * @return The component properties editing pane
	 */
	abstract protected JPanel setComponentPane(TEditor editor);

	/**
	 * Hook method to allow subclasseres to detemine which attribute changes are going
	 * to be made when appling the component changes.
	 * 
	 * @return A nested map who relates the components with their change attribute map
	 */
	abstract protected Map newComponentsAttributeMap();
}

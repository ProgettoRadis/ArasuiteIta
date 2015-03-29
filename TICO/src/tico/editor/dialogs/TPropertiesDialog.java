 /*
 * File: TPropertiesDialog.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Sep 3, 2006
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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import tico.board.TBoard;
import tico.components.TButton;
import tico.components.TDialog;
import tico.configuration.TLanguage;
import tico.editor.TBoardContainer;
import tico.editor.TEditor;

/**
 * DOCDO Add type documentation
 *
 *
 * @author bleras
 * @version 0.1 Sep 3, 2006
 */
abstract public class TPropertiesDialog extends TDialog {
	private TEditor editor;
	
	private JPanel mainPanel;

	/**
	 * DOCDO Add method documentation
	 * 
	 */
	public TPropertiesDialog(TEditor editor) {
		super(editor, true);
		
		this.editor = editor;
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		createPanels();
	}
	
	public void setVisible(boolean visible) {
		setResizable(true);
		pack();
		
		setLocationRelativeTo(editor);
		
		super.setVisible(visible);
	}
	
	private void createPanels() {
		mainPanel = new JPanel();
		JPanel buttonPanel = createButtonPanel();
		
		Container auxContainer= new Container();
		auxContainer.setLayout(new BorderLayout());

		auxContainer.add(mainPanel, BorderLayout.NORTH);
		auxContainer.add(buttonPanel, BorderLayout.SOUTH);
		
		
		JScrollPane jScrollPane = new JScrollPane(auxContainer,
	            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		getContentPane().add(jScrollPane);
	}
	
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		
		buttonPanel.setLayout(new FlowLayout());
		
		TButton accept = new TButton(TLanguage.getString("TPropertiesDialog.BUTTON_ACCEPT"));
		
		

		accept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (applyValues())
					dispose();
			}
		});
		
		buttonPanel.add(accept);


		TButton apply = new TButton(TLanguage.getString("TPropertiesDialog.BUTTON_APPLY"));
		
		apply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applyValues();
			}
		});
		buttonPanel.add(apply);

		TButton cancel = new TButton(TLanguage.getString("TPropertiesDialog.BUTTON_CANCEL"));
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPanel.add(cancel);
		
		return buttonPanel;
	}
	
	protected TBoard getBoard() {
		return editor.getCurrentBoard();
	}
	
	protected TBoardContainer getBoardContainer() {
		return editor.getCurrentBoardContainer();
	}
	
	protected TEditor getEditor() {
		return editor;
	}
	
	public JPanel getPropertiesPane() {
		return mainPanel;
	}
	
	abstract protected boolean applyValues();
}

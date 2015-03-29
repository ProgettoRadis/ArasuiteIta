/*
 * File: TProjectInterpreterDialog.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Antonio Rodríguez
 * 
 * Date: May 23, 2007
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

package tico.interpreter.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JPanel;

import tico.components.TButton;
import tico.components.TDialog;
import tico.configuration.TLanguage;

import tico.interpreter.TInterpreter;

/**
 * DOCDO Add type documentation
 */
abstract public class TProjectInterpreterDialog extends TDialog {

	private JPanel mainPanel;

	private TInterpreter interpreter;

	/**
	 * DOCDO Add method documentation
	 * 
	 */
	public TProjectInterpreterDialog(TInterpreter interpreter) {
		super(interpreter, true);

		this.interpreter = interpreter;

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		createPanels();
	}

	public void setVisible(boolean visible) {
		setResizable(false);
		pack();

		setLocationRelativeTo(interpreter);

		super.setVisible(visible);
	}

	private void createPanels() {
		mainPanel = new JPanel();
		JPanel buttonPanel = createButtonPanel();

		getContentPane().setLayout(new BorderLayout());

		getContentPane().add(mainPanel, BorderLayout.NORTH);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}

	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();

		buttonPanel.setLayout(new FlowLayout());

		TButton accept = new TButton(TLanguage
				.getString("TPropertiesDialog.BUTTON_ACCEPT"));
		accept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (applyValues())
					dispose();
			}
		});
		buttonPanel.add(accept);

		TButton apply = new TButton(TLanguage
				.getString("TPropertiesDialog.BUTTON_APPLY"));
		apply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applyValues();
			}
		});
		buttonPanel.add(apply);

		TButton cancel = new TButton(TLanguage
				.getString("TPropertiesDialog.BUTTON_CANCEL"));
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPanel.add(cancel);

		return buttonPanel;
	}

	protected TInterpreter getInterpreter() {
		return interpreter;
	}

	public JPanel getPropertiesPane() {
		return mainPanel;
	}

	abstract protected boolean applyValues();
}

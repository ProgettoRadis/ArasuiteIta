/*
 * File: TUsersAdminDialog.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Beatriz Mateo
 * 
 * Date: Jul 18, 2007
 * 
 * Company: Universidad de Zaragoza, CPS, DIIS, GIGA
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

package tico.rules.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;

import tico.components.TButton;
import tico.components.TComboBox;
import tico.components.TDialog;
import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.rules.TAttribute;
import tico.rules.TUser;
import tico.rules.database.TDeleteUser;
import tico.rules.database.TLoadLimitation;
import tico.rules.database.TLoadUser;
import tico.rules.database.TSaveUser;

/**
 * The users administration dialog class.
 * 
 * @author Beatriz Mateo
 * @version 0.1 Jul 18, 2007
 */
public class TUsersAdminDialog extends TValidationDialog {

	private TComboBox loadCombo;
	private TComboBox saveCombo;	
	
	private Vector<String> nameList;
	
	/**
	 * Creates a new <code>TUsersAdminDialog</code> for the specified
	 * <code>editor</code>.
	 * 
	 * @param editor The specified <code>editor</code>
	 */
	public TUsersAdminDialog(TEditor editor) {
		super(editor, TLanguage.getString("TUsersAdminDialog.TITLE"), true);
		createComponents(editor);
	}
	
	
	// Creates and places components
	private void createComponents(TEditor editor) {

		JTabbedPane tabbedPane = new JTabbedPane();
		// Create entry user panel
		tabbedPane.addTab(TLanguage.getString("TUsersAdminDialog.TAB"), 
				createEntryPanel());
		add(tabbedPane);

        // Place components
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(tabbedPane, BorderLayout.NORTH);
		getContentPane().add(createButtonPanel(editor), BorderLayout.SOUTH);

		// Display the dialog
		setDefaultCloseOperation(TDialog.DISPOSE_ON_CLOSE);

		setResizable(false);
		pack();

		setLocationRelativeTo(editor);
		setVisible(true);
	}
	
	
	private JPanel createEntryPanel() {	
		JPanel entryPanel = new JPanel();
		
		// Place the components
		GridBagConstraints c = new GridBagConstraints();
		entryPanel.setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 1, 10);
		c.gridx = 0;
		c.gridy = 0;
		entryPanel.add(createSpinnerPanel(), c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 1, 10);
		c.gridx = 0;
		c.gridy = 1;
		entryPanel.add(createComboPanel(), c);

        return entryPanel;
	}

	
	private JPanel createComboPanel() {	
		JPanel comboPanel = new JPanel();
		
		// Create comboBoxes
		TLoadUser load_u = new TLoadUser();
		Vector<TUser> userList = new Vector<TUser>();
		userList = load_u.loadAllUsers();
		
		nameList = new Vector<String>();
		nameList.add("");
		for (int i=0; i<userList.size(); i++)
			nameList.add(userList.get(i).getName());
        loadCombo = new TComboBox(nameList);
        loadCombo.setSelectedIndex(0);
        
        saveCombo = new TComboBox(nameList);
        saveCombo.setSelectedIndex(0);
        saveCombo.setEditable(true);
        
		//create user buttons
		TButton loadButton = new TButton(TLanguage.
				getString("TUsersAdminDialog.LOAD_USER_BUTTON"));
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TLoadUser load_u = new TLoadUser();
				String userName=(String)loadCombo.getSelectedItem();
				if (userName!="") {
					TUser user = new TUser();
					user = load_u.loadUserByName(userName);
				
					// Get the values of XML
					
					// reset spinner list
					for (int j=0; j<spinnerList.size(); j++) {
						JSpinner spinner = spinnerList.get(j);
						spinner.setValue(0);
						spinnerList.set(j, spinner);
					}
					for (int j=0; j<user.getAttributeCount(); j++) {
						JSpinner spinner = spinnerList.get(j);
						spinner.setValue(user.getAttribute(j).getValue());
						spinnerList.set(j, spinner);
					}
				}
			}
		});
		
		TButton saveButton = new TButton(TLanguage.
				getString("TUsersAdminDialog.SAVE_USER_BUTTON"));
		
		saveButton.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent e) {
				// Get limitations name
				Vector<String> limitName = new Vector<String>();
				TLoadLimitation load_l = new TLoadLimitation();
				limitName = load_l.getLimitationsName();
				Vector<String> list = new Vector<String>();				
				for (int i=0; i<limitName.size(); i++)
					if (!list.contains(limitName.get(i))){
						list.add(limitName.get(i));
					}
				// Create or modify user		
				TSaveUser save_u = new TSaveUser();
				String userName=(String)saveCombo.getSelectedItem();
				if (userName!=null && userName.compareTo("")!=0) {
					TUser user = new TUser(userName);
					// Get the values of sliders
					for (int j=0; j<spinnerList.size(); j++) {
						JSpinner spinner = spinnerList.elementAt(j);
						user.addAttribute(new TAttribute(list.get(j),
								list.get(j), spinner.getValue()));
					}
					int ret = save_u.saveUser(user);
					if (ret!=-1)
						JOptionPane.showMessageDialog(null,
								TLanguage.getString("TUsersAdminDialog.MESSAGE"), 
								TLanguage.getString("INFO"),
								JOptionPane.INFORMATION_MESSAGE);
					else
						JOptionPane.showMessageDialog(null,
								TLanguage.getString("TUsersAdminDialog.ERROR_MESSAGE"), 
								TLanguage.getString("ERROR"),
								JOptionPane.ERROR_MESSAGE);
					if (nameList.contains(userName)==false) {
						nameList.add(userName);
						saveCombo.updateUI();
						loadCombo.updateUI();
					}
					saveCombo.setSelectedIndex(0);
					loadCombo.setSelectedIndex(0);
				}
			}
		});
		
		TButton deleteButton = new TButton(TLanguage.
				getString("TUsersAdminDialog.DELETE_USER_BUTTON"));
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String userName=(String)loadCombo.getSelectedItem();
				if (userName!=null && userName.compareTo("")!=0) {
					Object[] options = {TLanguage.getString(
							"TUsersAdminDialog.ACCEPT_OPTION"),
							TLanguage.getString(
							"TUsersAdminDialog.CANCEL_OPTION")};
				
					int n = JOptionPane.showOptionDialog(null,
							TLanguage.getString("TUsersAdminDialog.DELETE_MESAGE"),
							TLanguage.getString("WARNING"),
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.WARNING_MESSAGE,
							null,
							options,
							options[1]);
				
					if (n == JOptionPane.YES_OPTION) {
						TDeleteUser delete_u = new TDeleteUser();
						int ret = delete_u.deleteUserByName(userName);
						if (ret!=-1)
							JOptionPane.showMessageDialog(null,
									TLanguage.getString("TUsersAdminDialog.DELETE_OK_MESSAGE"), 
									TLanguage.getString("INFO"),
									JOptionPane.INFORMATION_MESSAGE);
						else
							JOptionPane.showMessageDialog(null,
									TLanguage.getString("TUsersAdminDialog.DELETE_ERROR_MESSAGE"), 
									TLanguage.getString("ERROR"),
									JOptionPane.ERROR_MESSAGE);
					
						// Delete item
						nameList.remove(userName);
						saveCombo.updateUI();
						loadCombo.updateUI();
						
						saveCombo.setSelectedIndex(0);
						loadCombo.setSelectedIndex(0);
					}
				}
			}
		});
		
		// Place the components
		GridBagConstraints c = new GridBagConstraints();
		comboPanel.setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 1, 10);
		c.gridx = 0;
		c.gridy = 0;
		comboPanel.add(loadCombo, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 15, 1, 20);
		c.gridx = 1;
		c.gridy = 0;
		comboPanel.add(saveCombo, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 1, 10);
		c.gridx = 0;
		c.gridy = 1;
		comboPanel.add(loadButton, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 15, 1, 20);
		c.gridx = 1;
		c.gridy = 1;
		comboPanel.add(saveButton, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 15, 10);
		c.gridx = 0;
		c.gridy = 2;
		comboPanel.add(deleteButton, c);
		
        return comboPanel;
	}
	
	
	private JPanel createButtonPanel(final TEditor editor) {		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		// button close dialog
		TButton closeButton = new TButton(TLanguage.
				getString("TUsersAdminDialog.CLOSE_BUTTON"));
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPanel.add(closeButton);
		
		return buttonPanel;
	}
}
/*
 * File: TAboutDialog.java
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import tico.components.TButton;
import tico.components.TDialog;
import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;
import tico.interpreter.TInterpreter;


	public class TAboutDialog extends TDialog {
		
		public TAboutDialog (TInterpreter interpreter) {
			super(null, TLanguage.getString("TAboutDialog.TITLE"), true);

			// Create components
			Icon logo = TResourceManager.getImageIcon("tico-logo.png");
			JLabel logoLabel = new JLabel(logo);

			JLabel firstTextLabel = createFirstTextLabel();
			JLabel sectondTextLabel = createSecondTextLabel();
			
			JLabel others = new JLabel();
			String textOthers = 
				"<html><body>" +
	            "<ul><li>" + TLanguage.getString("TAboutDialog.OTHER_DEVELOPERS") + "</li></ul>" +
	            "</body></html>";
			others.setText(textOthers);
			
			others.addMouseListener(new TAboutDialogOthersListener(this));
			
			TButton acceptButton = new TButton(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			acceptButton.setText(TLanguage.getString("TAboutDialog.BUTTON_ACCEPT")); 

			// Place components
			GridBagConstraints c = new GridBagConstraints();
			getContentPane().setLayout(new GridBagLayout());

			c.fill = GridBagConstraints.BOTH;
			c.insets = new Insets(0, 0, 0, 0);
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 0.0;
			c.weighty = 0.0;
			getContentPane().add(logoLabel, c);

			c.insets = new Insets(0, 15, 0, 15);
			c.gridx = 0;
			c.gridy = 1;
			getContentPane().add(firstTextLabel, c);
		
			c.fill = GridBagConstraints.NONE;
			c.anchor = GridBagConstraints.WEST;
			c.insets = new Insets(0, 15, 0, 15);
			c.gridx = 0;
			c.gridy = 2;
			getContentPane().add(others, c);
			
			c.fill = GridBagConstraints.BOTH;
			c.insets = new Insets(0, 15, 10, 15);
			c.gridx = 0;
			c.gridy = 3;
			getContentPane().add(sectondTextLabel, c);

			c.fill = GridBagConstraints.NONE;
			c.anchor = GridBagConstraints.CENTER;
			c.insets = new Insets(10, 10, 10, 10);
			c.gridx = 0;
			c.gridy = 4;
			getContentPane().add(acceptButton, c);

			// Display the dialog
			setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

			setResizable(false);
			pack();

			setLocationRelativeTo(interpreter);
			setVisible(true);
		}

		// Creates the text label
		private JLabel createFirstTextLabel() {
	        JLabel textLabel = new JLabel();
	        textLabel.setHorizontalAlignment(SwingConstants.LEFT);
	        
	        String aboutText =
				"<html><body>" +
	            "<h2>" + TLanguage.getString("TAboutDialog.INTERPRETER_VERSION") + "</h2>" +
	            "<p>" + TLanguage.getString("TAboutDialog.DEVELOPERS") + "</p>" +
	            "<ul><li>Carolina Palacio Julián - 2009</li></ul>" +
	            "</body></html>";
			
	        textLabel.setText(aboutText);

	        return textLabel;
	    }
		
		private JLabel createSecondTextLabel() {
	        JLabel textLabel = new JLabel();
	        textLabel.setHorizontalAlignment(SwingConstants.LEFT);

			String aboutText =
				"<html><body>" +
	            "<p>" + TLanguage.getString("TAboutDialog.DIRECTOR") + "</p>" +
	            "<ul><li>Joaquín Ezpeleta (CPS - Universidad de Zaragoza)</li></ul>" +
	            "<par>" + TLanguage.getString("TAboutDialog.COLLABORATORS") + "</p>"  +
	            "<ul><li>César Canalis (CPEE Alborada - Zaragoza)</li>" +
	            "    <li>José Manuel Marcos (CPEE Alborada - Zaragoza)</li></ul>" +
	            "<p>" + TLanguage.getString("TAboutDialog.ORGANIZATIONS") + "</p>" +
	            "<ul><li>Universidad de Zaragoza, CPS, I3A</li>" +
	            "    <li>CPEE Alborada, Zaragoza</li></ul>" +
	            "<p>" + TLanguage.getString("TAboutDialog.YEAR") + ": 2010</p>" +
	            "<p>" + TLanguage.getString("TAboutDialog.LICENSE") + ": General Public License, version 3</p>" +
	            "</body></html>";
			
	        textLabel.setText(aboutText);

	        return textLabel;
	    }
	}
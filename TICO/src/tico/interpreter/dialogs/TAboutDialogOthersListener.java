/*
 * File: TAboutDialogoOthersListener.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Carolina Palacio
 * 
 * Date: Dec, 2009
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

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import tico.components.TButton;
import tico.configuration.TLanguage;

	public class TAboutDialogOthersListener implements MouseListener {

		Color defaultColor;
		JDialog otherDialog;
		TAboutDialog aboutDialog;
		
		public TAboutDialogOthersListener(TAboutDialog about){
			aboutDialog = about;			
		}
		
		public void mouseClicked(MouseEvent arg0) {
			Point location = arg0.getPoint();
			SwingUtilities.convertPointToScreen(location, (Component) arg0.getSource());
			JDialog othersDialog = new JDialog(aboutDialog);
			
			JPanel backgroundPanel = new JPanel();
			backgroundPanel.setBorder(new LineBorder(new Color (99,130,191), 1));
			backgroundPanel.setBackground(new Color(184,207,229));
			othersDialog.setContentPane(backgroundPanel);

			otherDialog = othersDialog;
			JLabel othersLabel = new JLabel();

			String otherDevelopers = 
				"<html><body>" +
	            "<p>Fernando Negré y David Ramos (Tico versión 0.0) - 2005</p>" +
	            "<p>Antonio Rodríguez (Intérprete Tico versión 0.1) - 2007</p>" +
	            "<p>Sandra Baldassarri y Beatriz Mateo (Utilidad Validación) - 2007</p>" +
	            "<p>Isabel González (Tico versión e1.0) - 2009" +
	            "</body></html>";
			othersLabel.setText(otherDevelopers);
			
			TButton acceptButton = new TButton(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {		
					otherDialog.dispose();
				}
			});
			acceptButton.setText(TLanguage.getString("TAboutDialog.BUTTON_ACCEPT")); 
			
			GridBagConstraints c = new GridBagConstraints();
			othersDialog.getContentPane().setLayout(new GridBagLayout());
			
			c.fill = GridBagConstraints.NONE;
			c.anchor = GridBagConstraints.WEST;
			c.insets = new Insets(0, 0, 0, 0);
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 0.0;
			c.weighty = 0.0;
			othersDialog.getContentPane().add(othersLabel, c);
			
			c.anchor = GridBagConstraints.CENTER;
			c.insets = new Insets(10, 10, 10, 10);
			c.gridx = 0;
			c.gridy = 1;
			othersDialog.getContentPane().add(acceptButton, c);

			othersDialog.setModal(true);
			othersDialog.setLocation(location);
			othersDialog.setUndecorated(true);
			othersDialog.setSize(360, 120);
			othersDialog.setVisible(true);
		}
		public void mouseEntered(MouseEvent arg0) {
			Cursor cursorBar = new Cursor(Cursor.HAND_CURSOR);					
			aboutDialog.setCursor(cursorBar);
			defaultColor = ((JLabel)arg0.getSource()).getForeground();
			((JLabel)arg0.getSource()).setForeground(new Color(4,4,153));
		}

		public void mouseExited(MouseEvent arg0) {
			Cursor cursorBar = new Cursor(Cursor.DEFAULT_CURSOR);					
			aboutDialog.setCursor(cursorBar);
			((JLabel)arg0.getSource()).setForeground(defaultColor);
		}

		public void mousePressed(MouseEvent arg0) {
		}

		public void mouseReleased(MouseEvent arg0) {
		}
		
	}
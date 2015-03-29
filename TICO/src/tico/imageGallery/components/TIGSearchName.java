/*
 * File: TIGSearchName.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Patricia M. Jaray
 * 
 * Date: Feb 1, 2008
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

package tico.imageGallery.components;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import tico.components.TButton;
import tico.configuration.TLanguage;
import tico.imageGallery.dataBase.TIGDataBase;
import tico.imageGallery.dialogs.TIGDeleteImagesDialog;
import tico.imageGallery.dialogs.TIGExportDBDialog;

/*
 * This class displays the panel that searches images from its name
 */
public class TIGSearchName extends JPanel{
		
	private JTextField texto;
	
	private Vector<Vector<String>> result;
	
	private TIGThumbnails thumbnails;
	
	private TIGDeleteImagesDialog deleteImages;
	
	private TIGExportDBDialog exportDB;
	
	public TIGSearchName(TIGThumbnails thumbnailsDialog){
		thumbnails = thumbnailsDialog;
		
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), 
				TLanguage.getString("TIGSearchNameDialog.NAME_SEARCH")));
		
		JLabel searchLabel = new JLabel(TLanguage.getString("TIGSearchNameDialog.SEARCH"));
		texto = new JTextField(30);
		
		texto.addKeyListener(new java.awt.event.KeyAdapter(){
			public void keyReleased(java.awt.event.KeyEvent e){
				if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (texto.getText().compareTo("") != 0)){
					result = new Vector();	
					result = TIGDataBase.imageSearchByName(texto.getText());	
					if (result.size()==0){
						// There are no results for that search
						thumbnails.updateThumbnailsPanel(result,0);
						JOptionPane.showConfirmDialog(null,
								TLanguage.getString("TIGSearchNameDialog.NO_RESULTS"),
								TLanguage.getString("TIGSearchNameDialog.NAME_RESULT"),
								JOptionPane.CLOSED_OPTION,JOptionPane.WARNING_MESSAGE);
					}
					else
						thumbnails.updateThumbnailsPanel(result,0);
				}
			}
		});
		
		TButton searchButton = new TButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				result = new Vector();
				if ((texto.getText().compareTo("") != 0)){
					result = TIGDataBase.imageSearchByName(texto.getText());
					if (result.size()==0){
						thumbnails.updateThumbnailsPanel(result,0);
						// There are no results for that search
						JOptionPane.showConfirmDialog(null,
								TLanguage.getString("TIGSearchNameDialog.NO_RESULTS"),
								TLanguage.getString("TIGSearchNameDialog.NAME_RESULT"),
								JOptionPane.CLOSED_OPTION,JOptionPane.WARNING_MESSAGE);
					}
					else
						thumbnails.updateThumbnailsPanel(result,0);
				}				
			}
		});
		
		searchButton.setText(TLanguage.getString("TIGSearchNameDialog.SEARCH_BUTTON"));
		
		searchButton.addKeyListener(new java.awt.event.KeyAdapter(){
			public void keyReleased(java.awt.event.KeyEvent e){
				if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (texto.getText().compareTo("") != 0)){
					result = new Vector();	
					result = TIGDataBase.imageSearchByName(texto.getText());	
					if (result.size()==0){
						thumbnails.updateThumbnailsPanel(result,0);
						// There are no results for that search
						JOptionPane.showConfirmDialog(null,
								TLanguage.getString("TIGSearchNameDialog.NO_RESULTS"),
								TLanguage.getString("TIGSearchNameDialog.NAME_RESULT"),
								JOptionPane.CLOSED_OPTION,JOptionPane.WARNING_MESSAGE);
					}
					else
						thumbnails.updateThumbnailsPanel(result,0);
				}
			}
		});
		
		// Place components
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 0;
		add(searchLabel, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5,5,5,5);
		c.gridx = 1;
		c.gridy = 0;
		add(texto, c);
		
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(5,5,5,5);
		c.gridx = 2;
		c.gridy = 0;
		add(searchButton, c);
	}
	
	public TIGSearchName(TIGThumbnails thumbnailsDialog, TIGDeleteImagesDialog deleteImagesDialog){
		
		deleteImages = deleteImagesDialog;
		
		thumbnails = thumbnailsDialog;
		
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), 
				TLanguage.getString("TIGSearchNameDialog.NAME_SEARCH")));
		
		JLabel searchLabel = new JLabel(TLanguage.getString("TIGSearchNameDialog.SEARCH"));
		texto = new JTextField(30);
		
		texto.addKeyListener(new java.awt.event.KeyAdapter(){
			public void keyReleased(java.awt.event.KeyEvent e){
				if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (texto.getText().compareTo("") != 0)){
					result = new Vector();	
					result = TIGDataBase.imageSearchByName(texto.getText());	
					if (result.size()==0){
						// There are no results for that search
						deleteImages.update(result);
						JOptionPane.showConfirmDialog(null,
								TLanguage.getString("TIGSearchNameDialog.NO_RESULTS"),
								TLanguage.getString("TIGSearchNameDialog.NAME_RESULT"),
								JOptionPane.CLOSED_OPTION,JOptionPane.WARNING_MESSAGE);
					}
					else
						deleteImages.update(result);
				}
			}
		});
		
		TButton searchButton = new TButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				result = new Vector();
				if ((texto.getText().compareTo("") != 0)){ //&& 
					result = TIGDataBase.imageSearchByName(texto.getText());
					if (result.size()==0){
						deleteImages.update(result);
						// There are no results for that search
						JOptionPane.showConfirmDialog(null,
								TLanguage.getString("TIGSearchNameDialog.NO_RESULTS"),
								TLanguage.getString("TIGSearchNameDialog.NAME_RESULT"),
								JOptionPane.CLOSED_OPTION,JOptionPane.WARNING_MESSAGE);
					}
					else
						deleteImages.update(result);
				}				
			}
		});
		
		searchButton.setText(TLanguage.getString("TIGSearchNameDialog.SEARCH_BUTTON"));
		
		searchButton.addKeyListener(new java.awt.event.KeyAdapter(){
			public void keyReleased(java.awt.event.KeyEvent e){
				if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (texto.getText().compareTo("") != 0)){
					result = new Vector();	
					result = TIGDataBase.imageSearchByName(texto.getText());	
					if (result.size()==0){
						deleteImages.update(result);
						// There are no results for that search
						JOptionPane.showConfirmDialog(null,
								TLanguage.getString("TIGSearchNameDialog.NO_RESULTS"),
								TLanguage.getString("TIGSearchNameDialog.NAME_RESULT"),
								JOptionPane.CLOSED_OPTION,JOptionPane.WARNING_MESSAGE);
					}
					else
						deleteImages.update(result);
				}
			}
		});
		
		// Place components
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 0;
		add(searchLabel, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5,5,5,5);
		c.gridx = 1;
		c.gridy = 0;
		add(texto, c);
		
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(5,5,5,5);
		c.gridx = 2;
		c.gridy = 0;
		add(searchButton, c);
	}
	
	public TIGSearchName(TIGThumbnails thumbnailsDialog, TIGExportDBDialog exportDBDialog){
		
		exportDB = exportDBDialog;
		
		thumbnails = thumbnailsDialog;
		
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), 
				TLanguage.getString("TIGSearchNameDialog.NAME_SEARCH")));
		
		JLabel searchLabel = new JLabel(TLanguage.getString("TIGSearchNameDialog.SEARCH"));
		texto = new JTextField(30);
		
		texto.addKeyListener(new java.awt.event.KeyAdapter(){
			public void keyReleased(java.awt.event.KeyEvent e){
				if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (texto.getText().compareTo("") != 0)){	
					result = TIGDataBase.imageSearchByName(texto.getText());	
					if (result.size()==0){
						// There are no results for that search
						exportDB.update(result);
						JOptionPane.showConfirmDialog(null,
								TLanguage.getString("TIGSearchNameDialog.NO_RESULTS"),
								TLanguage.getString("TIGSearchNameDialog.NAME_RESULT"),
								JOptionPane.CLOSED_OPTION,JOptionPane.WARNING_MESSAGE);
					}
					else
						exportDB.update(result);
				}
			}
		});
		
		TButton searchButton = new TButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				result = new Vector();
				if ((texto.getText().compareTo("") != 0)){ //&& 
					result = TIGDataBase.imageSearchByName(texto.getText());
					if (result.size()==0){
						exportDB.update(result);
						// There are no results for that search
						JOptionPane.showConfirmDialog(null,
								TLanguage.getString("TIGSearchNameDialog.NO_RESULTS"),
								TLanguage.getString("TIGSearchNameDialog.NAME_RESULT"),
								JOptionPane.CLOSED_OPTION,JOptionPane.WARNING_MESSAGE);
					}
					else
						exportDB.update(result);
				}				
			}
		});
		
		searchButton.setText(TLanguage.getString("TIGSearchNameDialog.SEARCH_BUTTON"));
		
		searchButton.addKeyListener(new java.awt.event.KeyAdapter(){
			public void keyReleased(java.awt.event.KeyEvent e){
				if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (texto.getText().compareTo("") != 0)){
					result = new Vector();	
					Vector<Vector<String>> result = TIGDataBase.imageSearchByName(texto.getText());	
					if (result.size()==0){
						exportDB.update(result);
						// There are no results for that search
						JOptionPane.showConfirmDialog(null,
								TLanguage.getString("TIGSearchNameDialog.NO_RESULTS"),
								TLanguage.getString("TIGSearchNameDialog.NAME_RESULT"),
								JOptionPane.CLOSED_OPTION,JOptionPane.WARNING_MESSAGE);
					}
					else
						exportDB.update(result);
				}
			}
		});
		
		// Place components
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 0;
		add(searchLabel, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5,5,5,5);
		c.gridx = 1;
		c.gridy = 0;
		add(texto, c);
		
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(5,5,5,5);
		c.gridx = 2;
		c.gridy = 0;
		add(searchButton, c);
	}
	
}


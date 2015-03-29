/*
 * File: TIGSearchKeyWord.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Carolina Palacio
 * 
 * Date: Feb 5, 2010
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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
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
 * This class displays a panel for searching images in the Data Base associated and, or 
 * or not to the key words selected. To see more detailed explanation go to the user manual.
 */
public class TIGSearchKeyWord extends JPanel{
	
	protected JTextField textKeyWord1;
	
	protected JTextField textKeyWord2;
	
	protected JTextField textKeyWord3;
	
	protected String keyWord1;
	
	protected String keyWord2;
	
	protected String keyWord3;
	
	protected JComboBox options1;
	
	protected JComboBox options2;
	
	private final static int SEARCH_OPTIONS_AND = 1;
	
	private final static int SEARCH_OPTIONS_OR = 2;
	
	private int searchOptions1;
	
	private int searchOptions2;
	
	private TIGThumbnails thumbnails;
	
	public Vector result;
	
	private TIGDeleteImagesDialog deleteImages;
	
	private TIGExportDBDialog exportDB;
	
	public TIGSearchKeyWord(TIGThumbnails thumbnailsDialog){ 
		
		thumbnails = thumbnailsDialog;
						
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), 
				TLanguage.getString("TIGKeyWordSearchDialog.CATEGORY_SEARCH")));
		
		//First keyWord text field
		textKeyWord1 = new JTextField();
		textKeyWord1.setEditable(true);
		textKeyWord1.setPreferredSize(new Dimension (125, 25));
		textKeyWord1.addKeyListener(new KeyAdapter(){
			public void keyReleased(java.awt.event.KeyEvent e){
				String text = textKeyWord1.getText().trim();
				if (!text.equals("")){
					options1.setEnabled(true);
					if (options1.getSelectedIndex()!=0){
						textKeyWord2.setEnabled(true);
						options2.setEnabled(true);
					}
					if (options2.getSelectedIndex()!=0 && options2.isEnabled()){
						textKeyWord3.setEnabled(true);
					}
				}else{
					options1.setEnabled(false);
					textKeyWord2.setEnabled(false);
					options2.setEnabled(false);
					textKeyWord3.setEnabled(false);
				}
			}
		});	
		
		//Second keyWord text field
		textKeyWord2 = new JTextField();		
		textKeyWord2.setEditable(true);
		textKeyWord2.setEnabled(false);
		textKeyWord2.setPreferredSize(new Dimension (125, 25));		
		textKeyWord2.addKeyListener(new KeyAdapter(){
			public void keyReleased(java.awt.event.KeyEvent e){
				String text = textKeyWord2.getText().trim();
				if (!text.equals("")){
					options2.setEnabled(true);
				}else{
					options2.setEnabled(false);
					textKeyWord3.setEnabled(false);
				}
			}
		});
		
		//Third keyWord text field
		textKeyWord3 = new JTextField();
		textKeyWord3.setEditable(true);
		textKeyWord3.setEnabled(false);
		textKeyWord3.setPreferredSize(new Dimension (125, 25));
		
		//Create combo box options
		String[] optionList = { "",
				TLanguage.getString("TIGKeyWordSearchDialog.AND"),
				TLanguage.getString("TIGKeyWordSearchDialog.OR")};

		//First combo box
        options1 = new JComboBox(optionList);
        options1.setSelectedIndex(0);
		options1.setEnabled(false);
		options1.setPreferredSize(new Dimension (50, 25));
		options1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				 JComboBox cb = (JComboBox)arg0.getSource();
				 if (cb.getSelectedIndex()!=0){
					 textKeyWord2.setEnabled(true);
					 if (!textKeyWord2.getText().trim().equals("")){
						 options2.setEnabled(true);
					 }
					 if (options2.getSelectedIndex()!=0){
						 textKeyWord3.setEnabled(true);
					 }
				 }else{
					 textKeyWord2.setEnabled(false);
					 options2.setEnabled(false);
					 textKeyWord3.setEnabled(false);
				 }
			}			
		});

		//Second combo box
		options2 = new JComboBox(optionList);
	    options2.setSelectedIndex(0);	        
		options2.setEnabled(false);
		options2.setPreferredSize(new Dimension (50, 25));
		options2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				 JComboBox cb = (JComboBox)arg0.getSource();
				 if (cb.getSelectedIndex()!=0){
					 textKeyWord3.setEnabled(true);
				 }else{
					 textKeyWord3.setEnabled(false);
				 }
			}			
		});		
		
		//Create button
		TButton searchButton = new TButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				result = new Vector();
				keyWord1 = getKeyWord1();
				if (!keyWord1.equals("")){
					keyWord2 = getKeyWord2();
					keyWord3 = getKeyWord3();;
					searchOptions1 = getSearchOptions1();
					//La b�squeda se gu�a por la selecci�n de los combos para saber que palabras clave
					//debe buscar. Puede ocurrir que el segundo combo tenga valor pero est� deshabilitado.
					//En ese caso no deber�a tenerse en cuenta su valor.
					if (!options2.isEnabled()){
						searchOptions2 = 0;
					}else{
						searchOptions2 = getSearchOptions2();
					}					
					result = TIGDataBase.imageSearchByKeyWords(keyWord1,searchOptions1,keyWord2,searchOptions2,keyWord3);
					if (result.size()==0){
						// There are no results for that search
						thumbnails.updateThumbnailsPanel(result,0);
						JOptionPane.showConfirmDialog(null,
								TLanguage.getString("TIGKeyWordSearchDialog.NO_RESULTS"),
								TLanguage.getString("TIGKeyWordSearchDialog.NAME"),
								JOptionPane.CLOSED_OPTION,JOptionPane.WARNING_MESSAGE);
					}
					else
						thumbnails.updateThumbnailsPanel(result,0);
				}else{
					//There are no categories to search
					JOptionPane.showConfirmDialog(null,
							TLanguage.getString("TIGKeyWordSearchDialog.NO_CATEGORIES"),
							TLanguage.getString("TIGKeyWordSearchDialog.NAME"),
							JOptionPane.CLOSED_OPTION,JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		searchButton.setText(TLanguage.getString("TIGKeyWordSearchDialog.SEARCH"));
		
		// Place components
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 0;
		add(textKeyWord1, c);
			
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 1;
		c.gridy = 0;
		add(options1, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 2;
		c.gridy = 0;
		add(textKeyWord2, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 3;
		c.gridy = 0;
		add(options2, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 4;
		c.gridy = 0;
		add(textKeyWord3, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 5;
		c.gridy = 0;
		add(searchButton, c);
	}
	
	public TIGSearchKeyWord(TIGThumbnails thumbnailsDialog, TIGDeleteImagesDialog deleteImagesDialog){ 
		
		deleteImages = deleteImagesDialog;
		
		thumbnails = thumbnailsDialog;
						
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), 
				TLanguage.getString("TIGKeyWordSearchDialog.CATEGORY_SEARCH")));
		
		//First keyWord text field
		textKeyWord1 = new JTextField();
		textKeyWord1.setEditable(true);
		textKeyWord1.setPreferredSize(new Dimension (125, 25));
		textKeyWord1.addKeyListener(new KeyAdapter(){
			public void keyReleased(java.awt.event.KeyEvent e){
				String text = textKeyWord1.getText().trim();
				if (!text.equals("")){
					options1.setEnabled(true);
					if (options1.getSelectedIndex()!=0){
						textKeyWord2.setEnabled(true);
						options2.setEnabled(true);
					}
					if (options2.getSelectedIndex()!=0){
						textKeyWord3.setEnabled(true);
					}
				}else{
					options1.setEnabled(false);
					textKeyWord2.setEnabled(false);
					options2.setEnabled(false);
					textKeyWord3.setEnabled(false);
				}
			}
		});	
		
		//Second keyWord text field
		textKeyWord2 = new JTextField();		
		textKeyWord2.setEditable(true);
		textKeyWord2.setEnabled(false);
		textKeyWord2.setPreferredSize(new Dimension (125, 25));		
		textKeyWord2.addKeyListener(new KeyAdapter(){
			public void keyReleased(java.awt.event.KeyEvent e){
				String text = textKeyWord2.getText().trim();
				if (!text.equals("")){
					options2.setEnabled(true);
				}else{
					options2.setEnabled(false);
					textKeyWord3.setEnabled(false);
				}
			}
		});
		
		//Third keyWord text field
		textKeyWord3 = new JTextField();
		textKeyWord3.setEditable(true);
		textKeyWord3.setEnabled(false);
		textKeyWord3.setPreferredSize(new Dimension (125, 25));
		
		//Create combo box options
		String[] optionList = { "",
				TLanguage.getString("TIGKeyWordSearchDialog.AND"),
				TLanguage.getString("TIGKeyWordSearchDialog.OR")};

		//First combo box
        options1 = new JComboBox(optionList);
        options1.setSelectedIndex(0);
		options1.setEnabled(false);
		options1.setPreferredSize(new Dimension (50, 25));
		options1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				 JComboBox cb = (JComboBox)arg0.getSource();
				 if (cb.getSelectedIndex()!=0){
					 textKeyWord2.setEnabled(true);
					 if (!textKeyWord2.getText().trim().equals("")){
						 options2.setEnabled(true);
					 }
					 if (options2.getSelectedIndex()!=0){
						 textKeyWord3.setEnabled(true);
					 }
				 }else{
					 textKeyWord2.setEnabled(false);
					 options2.setEnabled(false);
					 textKeyWord3.setEnabled(false);
				 }
			}			
		});

		//Second combo box
		options2 = new JComboBox(optionList);
	    options2.setSelectedIndex(0);	        
		options2.setEnabled(false);
		options2.setPreferredSize(new Dimension (50, 25));
		options2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				 JComboBox cb = (JComboBox)arg0.getSource();
				 if (cb.getSelectedIndex()!=0){
					 textKeyWord3.setEnabled(true);
				 }else{
					 textKeyWord3.setEnabled(false);
				 }
			}			
		});		
		
		//Create button
		TButton searchButton = new TButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				result = new Vector();
				keyWord1 = getKeyWord1();
				if (!keyWord1.equals("")){
					keyWord2 = getKeyWord2();
					keyWord3 = getKeyWord3();;
					searchOptions1 = getSearchOptions1();
					//La b�squeda se gu�a por la selecci�n de los combos para saber que palabras clave
					//debe buscar. Puede ocurrir que el segundo combo tenga valor pero est� deshabilitado.
					//En ese caso no deber�a tenerse en cuenta su valor.
					if (!options2.isEnabled()){
						searchOptions2 = 0;
					}else{
						searchOptions2 = getSearchOptions2();
					}					
					result = TIGDataBase.imageSearchByKeyWords(keyWord1,searchOptions1,keyWord2,searchOptions2,keyWord3);
					if (result.size()==0){
						deleteImages.update(result);
						// There are no results for that search
						JOptionPane.showConfirmDialog(null,
								TLanguage.getString("TIGKeyWordSearchDialog.NO_RESULTS"),
								TLanguage.getString("TIGKeyWordSearchDialog.NAME"),
								JOptionPane.CLOSED_OPTION,JOptionPane.WARNING_MESSAGE);
					}
					else
						deleteImages.update(result);
				}else{
					//There are no categories to search
					JOptionPane.showConfirmDialog(null,
							TLanguage.getString("TIGKeyWordSearchDialog.NO_CATEGORIES"),
							TLanguage.getString("TIGKeyWordSearchDialog.NAME"),
							JOptionPane.CLOSED_OPTION,JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		searchButton.setText(TLanguage.getString("TIGKeyWordSearchDialog.SEARCH"));
		
		// Place components
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 0;
		add(textKeyWord1, c);
			
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 1;
		c.gridy = 0;
		add(options1, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 2;
		c.gridy = 0;
		add(textKeyWord2, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 3;
		c.gridy = 0;
		add(options2, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 4;
		c.gridy = 0;
		add(textKeyWord3, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 5;
		c.gridy = 0;
		add(searchButton, c);
	}
	
	public TIGSearchKeyWord(TIGThumbnails thumbnailsDialog, TIGExportDBDialog exportDBDialog){ 
		
		exportDB = exportDBDialog;
		
		thumbnails = thumbnailsDialog;
						
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), 
				TLanguage.getString("TIGKeyWordSearchDialog.CATEGORY_SEARCH")));
		
		//First keyWord text field
		textKeyWord1 = new JTextField();
		textKeyWord1.setEditable(true);
		textKeyWord1.setPreferredSize(new Dimension (125, 25));
		textKeyWord1.addKeyListener(new KeyAdapter(){
			public void keyReleased(java.awt.event.KeyEvent e){
				String text = textKeyWord1.getText().trim();
				if (!text.equals("")){
					options1.setEnabled(true);
					if (options1.getSelectedIndex()!=0){
						textKeyWord2.setEnabled(true);
						options2.setEnabled(true);
					}
					if (options2.getSelectedIndex()!=0){
						textKeyWord3.setEnabled(true);
					}
				}else{
					options1.setEnabled(false);
					textKeyWord2.setEnabled(false);
					options2.setEnabled(false);
					textKeyWord3.setEnabled(false);
				}
			}
		});	
		
		//Second keyWord text field
		textKeyWord2 = new JTextField();		
		textKeyWord2.setEditable(true);
		textKeyWord2.setEnabled(false);
		textKeyWord2.setPreferredSize(new Dimension (125, 25));		
		textKeyWord2.addKeyListener(new KeyAdapter(){
			public void keyReleased(java.awt.event.KeyEvent e){
				String text = textKeyWord2.getText().trim();
				if (!text.equals("")){
					options2.setEnabled(true);
				}else{
					options2.setEnabled(false);
					textKeyWord3.setEnabled(false);
				}
			}
		});
		
		//Third keyWord text field
		textKeyWord3 = new JTextField();
		textKeyWord3.setEditable(true);
		textKeyWord3.setEnabled(false);
		textKeyWord3.setPreferredSize(new Dimension (125, 25));
		
		//Create combo box options
		String[] optionList = { "",
				TLanguage.getString("TIGKeyWordSearchDialog.AND"),
				TLanguage.getString("TIGKeyWordSearchDialog.OR")};

		//First combo box
        options1 = new JComboBox(optionList);
        options1.setSelectedIndex(0);
		options1.setEnabled(false);
		options1.setPreferredSize(new Dimension (50, 25));
		options1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				 JComboBox cb = (JComboBox)arg0.getSource();
				 if (cb.getSelectedIndex()!=0){
					 textKeyWord2.setEnabled(true);
					 if (!textKeyWord2.getText().trim().equals("")){
						 options2.setEnabled(true);
					 }
					 if (options2.getSelectedIndex()!=0){
						 textKeyWord3.setEnabled(true);
					 }
				 }else{
					 textKeyWord2.setEnabled(false);
					 options2.setEnabled(false);
					 textKeyWord3.setEnabled(false);
				 }
			}			
		});

		//Second combo box
		options2 = new JComboBox(optionList);
	    options2.setSelectedIndex(0);	        
		options2.setEnabled(false);
		options2.setPreferredSize(new Dimension (50, 25));
		options2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				 JComboBox cb = (JComboBox)arg0.getSource();
				 if (cb.getSelectedIndex()!=0){
					 textKeyWord3.setEnabled(true);
				 }else{
					 textKeyWord3.setEnabled(false);
				 }
			}			
		});		
		
		//Create button
		TButton searchButton = new TButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				result = new Vector();
				keyWord1 = getKeyWord1();
				if (!keyWord1.equals("")){
					keyWord2 = getKeyWord2();
					keyWord3 = getKeyWord3();;
					searchOptions1 = getSearchOptions1();
					//La b�squeda se gu�a por la selecci�n de los combos para saber que palabras clave
					//debe buscar. Puede ocurrir que el segundo combo tenga valor pero est� deshabilitado.
					//En ese caso no deber�a tenerse en cuenta su valor.
					if (!options2.isEnabled()){
						searchOptions2 = 0;
					}else{
						searchOptions2 = getSearchOptions2();
					}					
					result = TIGDataBase.imageSearchByKeyWords(keyWord1,searchOptions1,keyWord2,searchOptions2,keyWord3);
					if (result.size()==0){
						exportDB.update(result);
						// There are no results for that search
						JOptionPane.showConfirmDialog(null,
								TLanguage.getString("TIGKeyWordSearchDialog.NO_RESULTS"),
								TLanguage.getString("TIGKeyWordSearchDialog.NAME"),
								JOptionPane.CLOSED_OPTION,JOptionPane.WARNING_MESSAGE);
					}
					else
						exportDB.update(result);
				}else{
					//There are no categories to search
					JOptionPane.showConfirmDialog(null,
							TLanguage.getString("TIGKeyWordSearchDialog.NO_CATEGORIES"),
							TLanguage.getString("TIGKeyWordSearchDialog.NAME"),
							JOptionPane.CLOSED_OPTION,JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		searchButton.setText(TLanguage.getString("TIGKeyWordSearchDialog.SEARCH"));
		
		// Place components
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 0;
		add(textKeyWord1, c);
			
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 1;
		c.gridy = 0;
		add(options1, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 2;
		c.gridy = 0;
		add(textKeyWord2, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 3;
		c.gridy = 0;
		add(options2, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 4;
		c.gridy = 0;
		add(textKeyWord3, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 5;
		c.gridy = 0;
		add(searchButton, c);
	}

	public String getKeyWord1(){
		return textKeyWord1.getText().trim();
	}
	
	public String getKeyWord2(){
		return textKeyWord2.getText().trim();
	}	
	
	public String getKeyWord3(){
		return textKeyWord3.getText().trim();
	}	
	
	public int getSearchOptions1(){
		switch (options1.getSelectedIndex())
		{
			case 0: searchOptions1 = 0;
					break;
			case 1: searchOptions1 = SEARCH_OPTIONS_AND;
					break;
			case 2: searchOptions1 = SEARCH_OPTIONS_OR;
		}		
		return searchOptions1;
	}	
	
	public int getSearchOptions2(){
		switch (options2.getSelectedIndex())
		{
			case 0: searchOptions2 = 0;
					break;
			case 1: searchOptions2 = SEARCH_OPTIONS_AND;
					break;
			case 2: searchOptions2 = SEARCH_OPTIONS_OR;
		}		
		return searchOptions2;
	}

}

/*
 * File: TLanguageComboBox.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Nov 7, 2006
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

package tico.components;

import java.awt.Dimension;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultComboBoxModel;

import tico.configuration.TLanguage;

/**
 * Combo box to choose a language with the possible language files present
 * in the directory <i>lang</i>.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TLanguageComboBox extends TComboBox {
	protected static final int DEFAULT_WIDTH = 120;
	
	// The posible languages
	private Map languagesMap = TLanguage.getLanguages();

	public TLanguageComboBox() {
		this(null);
	}
	
	/**
	 * Creates a new <code>TLanguageComboBox</code>.
	 */
	public TLanguageComboBox(String language) {
		super();
		// Creates the model
		setModel(createLanguagesModel());
		// Set the selected language
		if (language != null)
			setLanguage(language);
	}

	/**
	 * Create the component model using the languages in
	 * <code>languagesMap</code>.
	 * 
	 * @return The created <code>comboBoxModel</code>
	 */
	protected DefaultComboBoxModel createLanguagesModel() {
		// Create empty combo box model
		DefaultComboBoxModel languagesModel = new DefaultComboBoxModel();
		
		Iterator languages = languagesMap.entrySet().iterator();
		
		while (languages.hasNext()) {
			Entry languageEntry = (Entry)languages.next();
			String currentLanguage = (String)languageEntry.getKey();
			languagesModel.addElement(currentLanguage);
		}

		return languagesModel;
	}

	/**
	 * Returns the selected <code>language</code>.
	 * 
	 * @return The selected <code>language</code>
	 */
	public String getLanguage() {
		return (String)getSelectedItem();
	}

	/**
	 * Set the <code>language</code>.
	 * 
	 * @param language The <code>language</code> to set
	 */
	public void setLanguage(String language) {
		setSelectedItem(language);
	}
	
	/**
	 * Returns the <code>file</code> of the selected language.
	 * 
	 * @return The <code>file</code> of the selected language
	 */
	public File getLanguageFile() {
		return (File)languagesMap.get((String)getSelectedItem());
	}

	/**
	 * Sets the <code>TFontFaceComboBox</code> default size. To change it
	 * this function must be overriden. 
	 */
	protected void setDefaultSize() {
		setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAUL_HEIGHT));
		setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAUL_HEIGHT));
	}
}

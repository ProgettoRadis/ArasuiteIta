/*
 * File: TLanguage.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Feb 19, 2006
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

package tico.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;

import tico.components.resources.TFileUtils;

/**
 * Static class that manages the application language files placed
 * in the <i>lang</i> directory. All the language files must have the <i>.lang</i>
 * extension and must be encoded in "UTF-8" charset.
 *
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TLanguage {
	private static String LANGUAGE_DIRECTORY = "lang";
	
	protected static String currentLanguage = TSetup.getLanguage();
	// Languages map between its names and its file
	protected static Map languagesMap = getLanguages();
	// ResourceBundle with the currentLanguage file
	protected static TResourceBundle LANGUAGE_BUNDLE = null;

	/**
	 * Inits the language file loading the file of the specified <code>language</code>
	 * to the <code>LANGUAGE_BUNDLE</code>.
	 * 
	 * @param language The current <code>language</code>
	 * @throws IOException
	 */
	public static void initLanguage(String language) throws IOException  {
		languagesMap = getLanguages();
		if ((languagesMap == null) || languagesMap.isEmpty())
			throw new FileNotFoundException();
		
		currentLanguage = language;
		if ((currentLanguage == null) ||
				!languagesMap.containsKey(currentLanguage)){
			// Set the first language on the map
			Iterator languagesIterator = languagesMap.entrySet().iterator();
			Map.Entry languageEntry = (Map.Entry)languagesIterator.next();
			currentLanguage = (String)languageEntry.getKey();
		}

		InputStream i = new FileInputStream((File)languagesMap
				.get(currentLanguage));
		LANGUAGE_BUNDLE = new TResourceBundle(i);
	}

	/**
	 * Determines if exists the <code>language</code> file.
	 * 
	 * @param language The <code>language</code> file to check
	 * @return <i>true</i> if the <code>language</code> file exists
	 */
	public static boolean languageExists(String language) {
		Map languageMap = getLanguages();
		
		return languageMap.containsKey(language);
	}
	
	/**
	 * Generates a <code>map</code> that contains all the language files of
	 * <i>lang</i> directory and its corresponding language names.
	 * 
	 * @return The generated <code>map</code>
	 */
	public static Map getLanguages() {
		Map languagesMap = new Hashtable();

		File languageDirectory = new File(LANGUAGE_DIRECTORY);
		File[] fileList = languageDirectory.listFiles();
		
		for (int i = 0; i < fileList.length; i++) {
			try {
				if (TFileUtils.getExtension(fileList[i]).equals("lang")) {
					TResourceBundle currentLanguageBundle = new TResourceBundle(
							new FileInputStream(fileList[i]));
					languagesMap.put(currentLanguageBundle
							.getString("LANG_NAME"), fileList[i]);
				}
			} catch (IOException e) {
			}
		}

		return languagesMap;
	}	

	/**
	 * Returns the string of the specified <code>key</code> in the current
	 * language resource bundle.
	 * 
	 * @param key The specified <code>key</code>
	 * @return The string of the specified <code>key</code>
	 */
	public static String getString(String key) {
		try {
			return LANGUAGE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '¡' + key + '!';
		} catch (NullPointerException e) {
			return '¡' + key + '!';
		}
	}
	
	public static String getString(String language, String key){
		
		String keyValue = null;
		
		File languageDirectory = new File(LANGUAGE_DIRECTORY);
		File[] fileList = languageDirectory.listFiles();
		
		for (int i = 0; i < fileList.length; i++) {
			try {
				if (TFileUtils.getExtension(fileList[i]).equals("lang")) {
					TResourceBundle currentLanguageBundle = new TResourceBundle(
							new FileInputStream(fileList[i]));
					if (language.equals(currentLanguageBundle.getString("LANG_NAME"))){
						keyValue = currentLanguageBundle.getString(key);
					}
				}
			} catch (IOException e) {
			}
		}
		return keyValue;
		
	}
}

/*
 * File: TWriteLanguageFile.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Beatriz Mateo
 * 
 * Date: Oct 22, 2007
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

package tico.rules.database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Map.Entry;


import tico.configuration.TLanguage;

/**
 * The <code>TWriteLanguageFile</code> writes in the language file the
 * message of a new rule.
 *
 * @author Beatriz Mateo
 * @version 1.0 Oct 22, 2007
 */

public class TWriteLanguageFile extends TLanguage {
	
	
	public TWriteLanguageFile(String message, String position) {
		writeMessage(message, position);
		writeInOtherLanguages(position);
	}
	
	
	// Writes message in the current language file
	public void writeMessage(String message, String position) {
		if (TLanguage.getString("TShowResults." + position).startsWith("!")) {
			// message not exists
			try {
				BufferedWriter pw = new BufferedWriter(new OutputStreamWriter(
			    		new FileOutputStream((File)languagesMap
								.get(currentLanguage), true), "UTF8")); 
				pw.write("\n" + "TShowResults." + position + "=" + message);
				pw.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		} else { 
			// message exists
			removeLineFromFile((languagesMap.get(currentLanguage)).toString(), 
					"TShowResults." + position+ "=" + 
					TLanguage.getString("TShowResults." + position));
			try {
				BufferedWriter pw = new BufferedWriter(new OutputStreamWriter(
			    		new FileOutputStream((File)languagesMap
								.get(currentLanguage), true), "UTF8")); 
				pw.write("\n" + "TShowResults." + position + "=" + message);
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	
	
	// Writes message in the rest of language files
	private void writeInOtherLanguages (String position) {
		Iterator languages = languagesMap.entrySet().iterator();
		String langAux = currentLanguage;
		while (languages.hasNext()) {
			Entry languageEntry = (Entry)languages.next();
			String currentLanguage = (String)languageEntry.getKey();
		
			if (currentLanguage.compareTo(langAux)!=0) {
				if (TLanguage.getString("TShowResults." + position).startsWith("!")) {
					// message not exists
					try {
						BufferedWriter pw = new BufferedWriter(new OutputStreamWriter(
								new FileOutputStream((File)languagesMap
										.get(currentLanguage), true), "UTF8")); 
						pw.write("\n" + "TShowResults." + position + "=" + "******");
						pw.close();
					} catch (IOException e) {
						
						e.printStackTrace();
					}
				} else { 
					// 	message exists
					removeLineFromFile(((File)languagesMap
							.get(currentLanguage)).toString(), "TShowResults." 
							+ position+ "=" + "******");
					try {
						BufferedWriter pw = new BufferedWriter(new OutputStreamWriter(
								new FileOutputStream((File)languagesMap
										.get(currentLanguage), true), "UTF8")); 
						pw.write("\n" + "TShowResults." + position + "=" + "******");
						pw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		currentLanguage = langAux;
	}
	

	public void removeLineFromFile(String file, String lineToRemove) {

		try {
		    File inFile = new File(file);
		      
			File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
		      
		    BufferedReader br = new BufferedReader(new InputStreamReader(
		    		new FileInputStream(inFile), "UTF8"));
		    BufferedWriter pw = new BufferedWriter(new OutputStreamWriter(
		    		new FileOutputStream(tempFile), "UTF8")); 
		      
		    String line = null;
		    while ((line = br.readLine()) != null) {		        
		    	if (!line.equals(lineToRemove)) {
		    		pw.write(line);
		    		pw.write("\n");
		    		pw.flush();
		    	}
		    }		    
		    pw.close();
		    br.close();
		    
		    copy(tempFile, inFile);
		    tempFile.delete();
		    }
		    catch (FileNotFoundException ex) {
		    	ex.printStackTrace();
		    }
		    catch (IOException ex) {
		    	ex.printStackTrace();
		    }
	}
	

    void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
    
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

}


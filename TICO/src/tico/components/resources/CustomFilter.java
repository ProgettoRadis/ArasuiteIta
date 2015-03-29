/*
 * File: CustomFilter.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Aug 27, 2006
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

package tico.components.resources;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import tico.configuration.TLanguage;

/**
 * Filter that can be set on a <code>JFileChooser</code> to display only
 * files with the specified <code>extension</code> and directories.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class CustomFilter extends FileFilter {
	private String extension;
	
	/**
	 * Creates a new <code>CustomFilter</code> with the specified
	 * <code>extension</code>.
	 * 
	 * @param extension The specified <code>extension</code>
	 */
	public CustomFilter(String extension) {
		this.extension = extension.toLowerCase();
	}
	
    // Accept all directories and the specified extension
    /* (non-Javadoc)
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String fExtension = TFileUtils.getExtension(f);
        if (fExtension != null)
        	return fExtension.toLowerCase().equals(extension);
        else return false;
    }

    // The description of this filter
    /* (non-Javadoc)
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    public String getDescription() {
        return TLanguage.getString("CustomFilter.DESCRIPTION") + " " +
        	extension.toUpperCase();
    }
}

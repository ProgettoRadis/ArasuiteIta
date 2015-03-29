/*
 * File: VideoFilter.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Carolina Palacio
 * 
 * Date: Oct 22, 2009
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
 * video files (avi, mpg, wmv and flv) and directories.
 * 
 * @author Carolina Palacio
 * @version e1.0 Oct 22, 2009
 */
public class VideoFilter extends FileFilter {
    // Accept all directories and all avi and mov
    /* (non-Javadoc)
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        return TFileUtils.isVideoFile(f);
    }

    // The description of this filter
    /* (non-Javadoc)
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    public String getDescription() {
        return TLanguage.getString("VideoFilter.DESCRIPTION");
    }
}

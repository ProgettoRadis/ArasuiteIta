/*
 * File: TResourceBundle.java
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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.PropertyResourceBundle;

/**
 * Implementation of <code>PropertyResourceBundle</code> that works with
 * "UTF-8" character encoded files.
 *
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TResourceBundle extends PropertyResourceBundle {
    /**
     * Creates a new <code>TResourceBundle</code> from the specified
     * <code>stream</code>.
     * 
     * @param stream The property <code>stream</code> to read from.
     */
	public TResourceBundle(InputStream stream) throws IOException {
		super(stream);
	}

	/* (non-Javadoc)
	 * @see java.util.ResourceBundle#handleGetObject(java.lang.String)
	 */
	public Object handleGetObject(String key) {
		String value = (String)super.handleGetObject(key);
		try {
			return new String(value.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// Shouldn't fail - but should we still add logging message?
			return null;
		}
	}
}
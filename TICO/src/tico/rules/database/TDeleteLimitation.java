/* File: TDeleteLimitation.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Author: Beatriz Mateo
 * 
 * Date: Oct 13, 2007
 * 
 * Company: Universidad de Zaragoza, CPS, DIIS, GIGA
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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * The <code>TDeleteLimitation</code> deletes a limitation by its name,
 * in the XML file. 
 *
 * @author Beatriz Mateo
 * @version 1.0 Oct 13, 2007
 */

public class TDeleteLimitation {
	
	// The XML file
	private static String PARAMS_FILE_PATH = "conf" + File.separator
	+ "params_resolution.xml";
	private static File PARAMS_FILE = new File(PARAMS_FILE_PATH);
	
	
	public TDeleteLimitation() {
	}

	
	// Deletes limitation by name
	public int deleteLimitationByName(String limitName) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Document doc;
		SAXBuilder builder = new SAXBuilder ();
		try{
			doc = builder.build(PARAMS_FILE);
			// Get the root element
			Element root=doc.getRootElement();
			// List of resolution elements
			List resolution=root.getChildren("res");
			Iterator i = resolution.iterator();
			Element e;
			int val;
			// Select the element with the same value as the system resolution
			do {
				e = (Element)i.next();
				val = Integer.parseInt(e.getAttributeValue("type"));
			} while (i.hasNext() && val!=screenSize.width);
			// get the limitations list	
			List limitations = e.getChildren(limitName);
			Iterator i_lim = limitations.iterator();
			if (i_lim.hasNext()) {
				do {
					i_lim.next();
					i_lim.remove();
				} while (i_lim.hasNext());
			}
			XMLOutputter xml = new XMLOutputter(Format.getPrettyFormat());
			FileOutputStream file = new FileOutputStream(PARAMS_FILE);
			xml.output(doc,file);
		} catch (JDOMException e) {
			return -1;
		} catch (IOException e) {
			return -1;
		}
		return 0;
	}
}
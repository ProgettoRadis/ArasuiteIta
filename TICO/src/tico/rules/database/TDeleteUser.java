/*
 * File: TDeleteUser.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Beatriz Mateo
 * 
 * Date: Jul 25, 2007
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * The <code>TDeleteUser</code> deletes a user by its name,
 * in the XML file. 
 *
 * @author Beatriz Mateo
 * @version 1.0 Jul 25, 2007
 */

public class TDeleteUser {
	
	// The XML file
	private static String USERS_FILE_PATH = "conf" + File.separator	+ "users.xml";
	private static File USERS_FILE = new File(USERS_FILE_PATH);
	
	
	public TDeleteUser() {
	}
	
	
	// Deletes user by name
	public int deleteUserByName(String userName) {
		
		Document doc;
		SAXBuilder builder = new SAXBuilder ();
		try {
			doc = builder.build(USERS_FILE);
			// Get the root element
			Element root=doc.getRootElement();
			// List of user features
			List users=root.getChildren("user");
			Iterator i = users.iterator();
			Element e;
			String name;
			// Select the element with the same name as userName
			if (i.hasNext()) {
				do {
					e = (Element)i.next();
					name = e.getAttributeValue("name");
				} while (i.hasNext() && (name.compareTo(userName)!=0));
				if (name.compareTo(userName)==0) { // user exist
					// Delete the child
					i.remove();
				}
				XMLOutputter xml = new XMLOutputter(Format.getPrettyFormat());
				FileOutputStream file = new FileOutputStream(USERS_FILE);
				xml.output(doc,file);
			}
		} catch (JDOMException e) {
			return -1;
		} catch (IOException e) {
			return -1;
		}	
		return 0;
	}
    
}
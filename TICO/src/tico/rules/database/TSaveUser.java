/*
 * File: TSaveUser.java
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

import tico.rules.TUser;

/**
 * The <code>TSaveUser</code> saves a user in the XML file. 
 *
 * @author Beatriz Mateo
 * @version 1.0 Jul 25, 2007
 */

public class TSaveUser {
	
	// The XML file
	private static String USERS_FILE_PATH = "conf" + File.separator	+ "users.xml";
	private static File USERS_FILE = new File(USERS_FILE_PATH);
	
	
	public TSaveUser() {
	}
	
	
	// Saves a user
	public int saveUser(TUser user) {
		
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
				} while (i.hasNext() && (name.compareTo(user.getName())!=0));
			
				if (name.compareTo(user.getName())==0) { // user exist
					// Remove the user and create new user
					i.remove();
					Element usr=new Element("user");
					usr.setAttribute("name", user.getName());
					for (int j=0; j<user.getAttributeCount(); j++)
						usr.addContent(new Element(user.getAttribute(j).getNameXML())
							.setText(String.valueOf(user.getAttribute(j).getValue())));
					root.addContent(usr);
				}
				else { // new user
					Element usr=new Element("user");
					usr.setAttribute("name", user.getName());
					for (int j=0; j<user.getAttributeCount(); j++)
						usr.addContent(new Element(user.getAttribute(j).getNameXML())
							.setText(String.valueOf(user.getAttribute(j).getValue())));
					root.addContent(usr);
				}
			} else { // there are no elements
				Element usr=new Element("user");
				usr.setAttribute("name", user.getName());
				for (int j=0; j<user.getAttributeCount(); j++)
					usr.addContent(new Element(user.getAttribute(j).getNameXML())
						.setText(String.valueOf(user.getAttribute(j).getValue())));
				root.addContent(usr);
			}	
			XMLOutputter xml = new XMLOutputter(Format.getPrettyFormat());
			FileOutputStream file = new FileOutputStream(USERS_FILE);
	   		xml.output(doc,file);
		} catch (JDOMException e) {
			return -1;
		} catch (IOException e) {
			return -1;
		}	
		return 0;
	}
}
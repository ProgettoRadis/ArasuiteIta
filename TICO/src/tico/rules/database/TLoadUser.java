/*
 * File: TLoadUser.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Beatriz Mateo
 * 
 * Date: Jul 24, 2007
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
import java.io.IOException;
import java.util.*;

import org.jdom.*;
import org.jdom.input.SAXBuilder;

import tico.rules.TAttribute;
import tico.rules.TUser;

/**
 * The <code>TLoadUser</code> loads all users in the XML file or 
 * loads users by its name.
 *
 * @author Beatriz Mateo
 * @version 1.0 Jul 24, 2007
 */

public class TLoadUser {
	
	// The XML file
	private static String USERS_FILE_PATH = "conf" + File.separator + "users.xml";
	private static File USERS_FILE = new File(USERS_FILE_PATH);
	
	
	public TLoadUser() {
	}
	
	
	// Loads users for name
	public TUser loadUserByName(String userName) {
		TUser user = new TUser(userName);
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
			
				// Get all the values of limitations
				List limitations = e.getChildren();
				Iterator i_lim = limitations.iterator();
				Element e_lim;
				if (i_lim.hasNext()) {
					do {
						e_lim = (Element)i_lim.next();
						name = e_lim.getName();
						user.addAttribute(new TAttribute(name, name, 
								Integer.parseInt(e_lim.getText())));
					} while (i_lim.hasNext());				
				}
			}
		} catch (JDOMException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return user;	
	}
	
	
	// Loads all users in XML file
	public Vector<TUser> loadAllUsers() {
		Vector<TUser> usersList = new Vector<TUser>();
		// Get the names in the document
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
			// Select all the elements
			if (i.hasNext()) {
				do {
					e = (Element)i.next();
					name = e.getAttributeValue("name");
					usersList.addElement(new TUser(name));
				} while (i.hasNext());
			}
		} catch (JDOMException e) {
			return null;
		} catch (IOException e) {
			return null;
		}	
		return usersList;
	}
    
}
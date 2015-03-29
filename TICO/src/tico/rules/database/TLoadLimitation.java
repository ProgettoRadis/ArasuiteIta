/*
 * File: TLoadLimitation.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Beatriz Mateo
 * 
 * Date: Oct 2, 2007
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

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import org.jdom.*;
import org.jdom.input.SAXBuilder;

import tico.configuration.TLanguage;
import tico.rules.TAttribute;
import tico.rules.TLimitation;

/**
 * The <code>TLoadLimitation</code> allows to load all limitations or
 * the names of the list of limitations in the XML file.
 *
 * @author Beatriz Mateo
 * @version 1.0 Oct 2, 2007
 */

public class TLoadLimitation {
	
	// The XML file
	private static String PARAMS_FILE_PATH = "conf" + File.separator 
			+ "params_resolution.xml";
	private static File PARAMS_FILE = new File(PARAMS_FILE_PATH);
	
	
	public TLoadLimitation() {
	}

	
	// Loads all the limitations
	public Vector<TLimitation> loadAllLimitations() {
		// Get the limitations in the document
		Vector<TLimitation> limitationList = new Vector<TLimitation>();
		
		limitationList.addElement(new TLimitation("",""));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Document doc;
		SAXBuilder builder = new SAXBuilder ();
		try {
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
			String name;	
			List limitations = e.getChildren();
			Iterator i_lim = limitations.iterator();
			if (i_lim.hasNext()) {
				do {
					e = (Element)i_lim.next();
					name = e.getName();
					if (name.compareTo(limitationList.lastElement().getNameXML())!=0) {
						TLimitation limitation;
						// get limitation name
						if ((TLanguage.getString("Rules." 
								+ name.toUpperCase())).startsWith("!"))
							limitation = new TLimitation(name, name);
						else
							limitation = new TLimitation(TLanguage.getString(
								"Rules."+name.toUpperCase()), name);					
						List attributes = e.getChildren();
						Iterator i_atr = attributes.iterator();
						Element e_atr;
						// get the attribute list
						if (i_atr.hasNext()) {
							do {
								e_atr = (Element)i_atr.next();
								name = e_atr.getName();
								if (name.compareTo(limitationList.get(limitationList.indexOf(
										limitationList.lastElement())).getNameXML())!=0) {
									if ((TLanguage.getString(
											"Rules."+name.toUpperCase())).startsWith("!"))
										limitation.addAttribute(new TAttribute(name,name));
									else
										limitation.addAttribute(new TAttribute(TLanguage.getString(
											"Rules."+name.toUpperCase()),name));
								}
							} while(i_atr.hasNext());
						}
						// add limitation to limitation list
						limitationList.add(limitation);
					}
				} while (i_lim.hasNext());				
			}
		} catch (JDOMException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return limitationList;		
	}

	
	// Gets all names of limitations
	public Vector<String> getLimitationsName(){
		Vector<String> limitationsName = new Vector<String>();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Document doc;
		SAXBuilder builder = new SAXBuilder ();
		try {
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
			
			List limitations = e.getChildren();
			Iterator i_lim = limitations.iterator();
			String name;
			Element e_lim;
			if (i_lim.hasNext()) {
				do {
					e_lim = (Element)i_lim.next();
					name = e_lim.getName();
						limitationsName.add(name);
				} while (i_lim.hasNext());				
			}
		} catch (JDOMException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return limitationsName;
	}
}
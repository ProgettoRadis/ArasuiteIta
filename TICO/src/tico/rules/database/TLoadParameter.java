/*
 * File: TLoadParameter.java
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
import java.util.Vector;

import org.jdom.*;
import org.jdom.input.SAXBuilder;

import tico.configuration.TLanguage;
import tico.rules.TAttribute;
import tico.rules.TLimitation;

/**
 * The <code>TLoadParameter</code> loads all the parameters that are
 * necessary for the validation process.
 *
 * @author Beatriz Mateo
 * @version 1.0 Oct 2, 2007
 */

public class TLoadParameter {
	
	private static String PARAMS_FILE_PATH = "conf" + File.separator 
			+ "params_resolution.xml";
	private static File PARAMS_FILE = new File(PARAMS_FILE_PATH);
	
	// List of limitations with all its values
	public static Vector<TLimitation> limitationList = new Vector<TLimitation>();
	
	
	public TLoadParameter() {
	}
	
	// Gets all parameters for the validation process
	public Vector<TLimitation> getParametersForValidation(Vector<String> limitNames, Vector<Integer> limitValues) {
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
			
			// Get all the values of limitations
			for (int j=0; j<limitNames.size(); j++) {
			
				List limitations = e.getChildren(limitNames.elementAt(j));
				Iterator i_lim = limitations.iterator();
				Element e_lim;
				if (i_lim.hasNext()) {
					do {
						// Limitations
						e_lim = (Element)i_lim.next();
						val = Integer.parseInt(e_lim.getAttributeValue("value"));
					} while (i_lim.hasNext() && val!=limitValues.elementAt(j));
					
					// Add the limitation and all its components
					TLimitation limitation;
					if (TLanguage.getString("Rules." + limitNames.elementAt(j).toUpperCase()).startsWith("!"))
						limitation = new TLimitation (limitNames.elementAt(j),
								limitNames.elementAt(j), val);
					else
						limitation = new TLimitation (TLanguage.
							getString("Rules."+limitNames.elementAt(j).toUpperCase()),
							limitNames.elementAt(j), val);
					// Get all the attributes
					List attributes = e_lim.getChildren();
					Iterator i_atr = attributes.iterator();
					if (i_atr.hasNext()) {
						Element e_atr;
						do {
							e_atr = (Element)i_atr.next();
							String text = e_atr.getText();
							TAttribute attribute = new TAttribute();
							// set the value
							if (text.compareTo("true")==0) {
								if (TLanguage.getString("Rules." + e_atr.getName().toUpperCase()).startsWith("!"))
									// attribute name not exists in language file
									attribute.setName(e_atr.getName());
								else
									attribute.setName(TLanguage.getString("Rules."
										+ e_atr.getName().toUpperCase()));
								attribute.setNameXML(e_atr.getName());
								attribute.setValue((Object)new Boolean(true));
							} else if (text.compareTo("false")==0) {
								if (TLanguage.getString("Rules." + e_atr.getName().toUpperCase()).startsWith("!"))
									// attribute name not exists in language file
									attribute.setName(e_atr.getName());
								else
									attribute.setName(TLanguage.getString("Rules."
										+ e_atr.getName().toUpperCase()));
								attribute.setNameXML(e_atr.getName());
								attribute.setValue((Object)new Boolean(false));
							} else {
								if (TLanguage.getString("Rules." + e_atr.getName().toUpperCase()).startsWith("!"))
									// attribute name not exists in language file
									attribute.setName(e_atr.getName());
								else
									attribute.setName(TLanguage.getString("Rules."
										+ e_atr.getName().toUpperCase()));
								attribute.setNameXML(e_atr.getName());
								attribute.setValue((Object)Integer.valueOf(text).intValue());
							}
							limitation.addAttribute(attribute);
						} while (i_atr.hasNext());
					}		
					limitationList.add(limitation);	
				}
			}
		} catch (JDOMException e) {
			return null;
		} catch (IOException e) {
			return null;
		}	
		return limitationList;
	}
	
	
	// loads all parameters by the XML file
	public Vector<TAttribute> loadAllParameters() {
		Vector<String> limitationNames = new Vector<String>();
		Vector<String> attributeNames = new Vector<String>();
		Vector<TAttribute> parameterList = new Vector<TAttribute>();
		
		attributeNames.add("");
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
			
			// Get all the attributes of limitations
			List limitations = e.getChildren();
			Iterator i_lim = limitations.iterator();
			Element e_lim;
			String name;
			if (i_lim.hasNext()) {				
				do {
					// Limitations
					e_lim = (Element)i_lim.next();
					name = e_lim.getName();
					String value;
					// get the limitation name
					if (!limitationNames.contains(name)) {
						limitationNames.add(name);
						List attributes = e_lim.getChildren();
						Iterator i_atr = attributes.iterator();
						// get the attributes of each limitation
						if (i_atr.hasNext()) {
							Element e_atr;
							do {
								e_atr = (Element)i_atr.next();
								name = e_atr.getName();
								value = (String) e_atr.getText();
								String text = e_atr.getText();
								TAttribute attribute = new TAttribute();
								if (value.compareTo("true")==0) {
									if (TLanguage.getString("Rules." + name.toUpperCase()).startsWith("!"))
										// attribute name not exists in language file
										attribute.setName(name);
									else
										attribute.setName(TLanguage.getString("Rules."
											+ name.toUpperCase()));
									attribute.setNameXML(name);
									attribute.setValue((Object)new Boolean(true));
								} else if (text.compareTo("false")==0) {
									if (TLanguage.getString("Rules." + name.toUpperCase()).startsWith("!"))
										// attribute name not exists in language file
										attribute.setName(name);
									else
										attribute.setName(TLanguage.getString("Rules."
											+ name.toUpperCase()));
									attribute.setNameXML(e_atr.getName());
									attribute.setValue((Object)new Boolean(false));
								} else {
									if (TLanguage.getString("Rules." + name.toUpperCase()).startsWith("!"))
										// attribute name not exists in language file
										attribute.setName(name);
									else
										attribute.setName(TLanguage.getString("Rules."
											+ name.toUpperCase()));
									attribute.setNameXML(e_atr.getName());
									attribute.setValue((Object)Integer.valueOf(text).intValue());
								}
								if (TLanguage.getString("Rules." + name.toUpperCase()).startsWith("!"))
									// attribute name not exists in language file
									attributeNames.add(name);
								else
									attributeNames.add(TLanguage.getString("Rules." 
										+ name.toUpperCase()));
								parameterList.add(attribute);
							} while (i_atr.hasNext());
						}
					}
				} while (i_lim.hasNext());
			}
		} catch (JDOMException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return parameterList;
	}
	
	
	// returns the list of limitations
	public Vector<TLimitation> getLimitationList() {
		return limitationList;
	} 
}
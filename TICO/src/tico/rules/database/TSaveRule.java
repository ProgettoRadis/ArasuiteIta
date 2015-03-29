/*
 * File: TSaveRule.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Author: Beatriz Mateo
 * 
 * Date: Oct 20, 2007
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import tico.rules.TRule;

/**
 * The <code>TSaveRule</code> saves a rule in the XML file.
 *
 * @author Beatriz Mateo
 * @version 1.0 Oct 20, 2007
 */

public class TSaveRule {
	
	// The XML file
	private static String RULES_FILE_PATH = "conf" + File.separator + "rules.xml";
	private static File RULES_FILE = new File(RULES_FILE_PATH);
	
	
	public TSaveRule() {
	}
	
	// Saves a rule
	public int saveRule(TRule rule) {
		
		Document doc;
		SAXBuilder builder = new SAXBuilder ();
		try {
			doc = builder.build(RULES_FILE);
			// Get the root element
			Element root=doc.getRootElement();
			// List of user features
			List users=root.getChildren("rule");
			Iterator i = users.iterator();
			Element e;
			String name;
			// Select the element with the same name as ruleName
			if (i.hasNext()) {
				do {
					e = (Element)i.next();
					Element e_name = e.getChild("name");
					name = e_name.getText();
				} while(i.hasNext() && name.compareTo(rule.getNameXML())!=0);
				if (name.compareTo(rule.getNameXML())==0) { // rule exist
					// delete the rule and create a new rule
					i.remove();
					
					Element ruleElement = new Element("rule");
					ruleElement.setAttribute("type", rule.getType());
					ruleElement.addContent(new Element("name")
						.setText(rule.getNameXML()));
					if (rule.getFunctionXML().compareTo("lightness")==0) {
						ruleElement.addContent(new Element("atr")
							.setText(rule.getAttribute().getNameXML()));
						ruleElement.addContent(new Element("atr2")
						.setText(rule.getAttribute2().getNameXML()));
					} else 
						ruleElement.addContent(new Element("atr")
							.setText(rule.getAttribute().getNameXML()));
					ruleElement.addContent(new Element("function")
						.setText(rule.getFunctionXML()));
					ruleElement.addContent(new Element("param")
						.setText(rule.getParameter().getNameXML()));
					ruleElement.addContent(new Element("text")
						.setText(rule.getMessageXML()));
					
					root.addContent(ruleElement);
				}
				else { // new rule
					Element ruleElement = new Element("rule");
					ruleElement.setAttribute("type", rule.getType());
					ruleElement.addContent(new Element("name")
						.setText(rule.getNameXML()));
					if (rule.getFunctionXML().compareTo("lightness")==0) {
						ruleElement.addContent(new Element("atr")
							.setText(rule.getAttribute().getNameXML()));
						ruleElement.addContent(new Element("atr2")
						.setText(rule.getAttribute2().getNameXML()));
					} else 
						ruleElement.addContent(new Element("atr")
							.setText(rule.getAttribute().getNameXML()));
					ruleElement.addContent(new Element("function")
						.setText(rule.getFunctionXML()));
					ruleElement.addContent(new Element("param")
						.setText(rule.getParameter().getNameXML()));
					ruleElement.addContent(new Element("text")
						.setText(rule.getMessageXML()));
					
					root.addContent(ruleElement);
				}
			} else { // there are no elements
				Element ruleElement = new Element("rule");
				ruleElement.setAttribute("type", rule.getType());
				ruleElement.addContent(new Element("name")
					.setText(rule.getNameXML()));
				if (rule.getFunctionXML().compareTo("lightness")==0) {
					ruleElement.addContent(new Element("atr")
						.setText(rule.getAttribute().getNameXML()));
					ruleElement.addContent(new Element("atr2")
					.setText(rule.getAttribute2().getNameXML()));
				} else 
					ruleElement.addContent(new Element("atr")
						.setText(rule.getAttribute().getNameXML()));
				ruleElement.addContent(new Element("function")
					.setText(rule.getFunctionXML()));
				ruleElement.addContent(new Element("param")
					.setText(rule.getParameter().getNameXML()));
				ruleElement.addContent(new Element("text")
					.setText(rule.getMessageXML()));
				
				root.addContent(ruleElement);
			}	
			XMLOutputter xml = new XMLOutputter(Format.getPrettyFormat());
			FileOutputStream file = new FileOutputStream(RULES_FILE);
	   		xml.output(doc,file);
			
		} catch (JDOMException e) {
			return -1;
		} catch (IOException e) {
			return -1;
		}	
		return 0;
	}
}
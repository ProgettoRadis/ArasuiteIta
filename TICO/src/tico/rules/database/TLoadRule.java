/* File: TLoadRule.java
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

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.jdom.*;
import org.jdom.input.SAXBuilder;

import tico.configuration.TLanguage;
import tico.rules.TAttribute;
import tico.rules.TLimitation;
import tico.rules.TRule;

/**
 * The <code>TLoadRule</code> load all rules or rules by type in the XML file. 
 *
 * @author Beatriz Mateo
 * @version 1.0 Oct 13, 2007
 */

public class TLoadRule {
	
	// The XML file
	private static String RULES_FILE_PATH = "conf" + File.separator + "rules.xml";
	private static File RULES_FILE = new File(RULES_FILE_PATH);
	
	
	public TLoadRule() {
	}


	// Loads rules by the type element they analyze
	public Vector<TRule> loadRulesByType(String option, Vector<TAttribute> attributeList) {
		Vector<TRule> ruleList = new Vector<TRule>();
		Vector<TLimitation> limitationList = new Vector<TLimitation>();
		
		TLoadParameter load_p = new TLoadParameter();
		limitationList = load_p.getLimitationList();
		
		Document doc;
		SAXBuilder builder = new SAXBuilder ();
		try {
			doc = builder.build(RULES_FILE);
			// Get the root element
			Element root=doc.getRootElement();
			// List of resolution features
			List rules=root.getChildren("rule");
			Iterator i = rules.iterator();
			Element e;
			String val;
			// Select all the elements
			if (i.hasNext()) {
				do {
					TRule rule = new TRule();
					e = (Element)i.next();
					val = e.getAttributeValue("type");
					// rule type "project", "board", "cell" or "interpreter"
					if (val.compareTo(option)==0) {
						List features = e.getChildren();
						Iterator i_feat = features.iterator();
						
						if (i_feat.hasNext()) {
							do {
								e = (Element)i_feat.next();
								rule.setType(val);
								// child "name"
								if (e.getName().compareTo("name")==0)
									rule.setNameXML(e.getText());
								// child "atr"
								else if (e.getName().compareTo("atr")==0) {
									for (int j=0; j<attributeList.size(); j++)
										if (attributeList.elementAt(j).getNameXML().compareTo(e.getText())==0) {
											rule.setAttribute(new TAttribute(attributeList.elementAt(j).getName(), 
													attributeList.elementAt(j).getNameXML(), attributeList.elementAt(j).getValue()));
										}
								}
								// child "atr2"
								else if (e.getName().compareTo("atr2")==0) {
									for (int j=0; j<attributeList.size(); j++)
										if (attributeList.elementAt(j).getNameXML().compareTo(e.getText())==0) {
											rule.setAttribute2(new TAttribute(attributeList.elementAt(j).getName(), 
													attributeList.elementAt(j).getNameXML(), attributeList.elementAt(j).getValue()));
										}
								}
								// child "param"
								else if (e.getName().compareTo("param")==0) {
									for (int j=0; j<limitationList.size(); j++)
										for (int k=0; k<limitationList.get(j).getAttributeCount(); k++)
											if (limitationList.elementAt(j).getAttribute(k).getNameXML().compareTo(e.getText())==0) {
												rule.setParameter(new TAttribute(limitationList.elementAt(j).getAttribute(k).getName(), 
														limitationList.elementAt(j).getAttribute(k).getNameXML(), 
														limitationList.elementAt(j).getAttribute(k).getValue()));
												rule.getParameter().setLimitType(limitationList.get(j).getNameXML());
											}
								}
								// child "function"
								else if (e.getName().compareTo("function")==0)
									rule.setFunctionXML(e.getText());
								// child "text"
								else if (e.getName().compareTo("text")==0)
									rule.setMessageXML(e.getText());
								
							} while (i_feat.hasNext());
						}
						ruleList.add(rule);
					}
					
				} while (i.hasNext());
			}
		} catch (JDOMException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return ruleList;
	}

	
	// loads all rules in the XML file
	public Vector<TRule> loadAllRules() {
		Vector<TRule> ruleList = new Vector<TRule>();
		Document doc;
		SAXBuilder builder = new SAXBuilder ();
		try {
			doc = builder.build(RULES_FILE);
			// Get the root element
			Element root = doc.getRootElement();
			// List of resolution features
			List rules = root.getChildren("rule");
			Iterator i = rules.iterator();
			Element e;
			// Select all elements
			if (i.hasNext()) {
				do {
					e = (Element)i.next();
					List features = e.getChildren();
					Iterator i_feat = features.iterator();
					TRule rule = new TRule();
					rule.setType(e.getAttributeValue("type"));
					if (i_feat.hasNext()) {
						do {
							e = (Element)i_feat.next();
							// child "name"
							if (e.getName().compareTo("name")==0) {
								rule.setNameXML(e.getText());
								String[] name = e.getText().split("_");
								rule.setName(TLanguage.getString("TRulesAdminDialog.RULE")
												+ "_" + name[1]);
							}
							// child "atr"
							if (e.getName().compareTo("atr")==0) {
								rule.setAttribute(new TAttribute(TLanguage.getString("Rules."
										+ e.getText().toUpperCase()),e.getText()));
							}
							// child "atr2"
							if (e.getName().compareTo("atr2")==0) {
								rule.setAttribute2(new TAttribute(TLanguage.getString("Rules."
										+ e.getText().toUpperCase()),e.getText()));
							}
							// child "param"
							if (e.getName().compareTo("param")==0) {
								if (TLanguage.getString("Rules." + e.getText().toUpperCase()).startsWith("!"))
									rule.setParameter(new TAttribute(e.getText(), e.getText()));
								else
									rule.setParameter(new TAttribute(TLanguage.getString("Rules."
										+ e.getText().toUpperCase()), e.getText()));
							}
							// child "function"
							if (e.getName().compareTo("function")==0) {
								rule.setFunctionXML(e.getText());
								rule.setFunction(TLanguage.getString("TRulesAdminDialog."
										+ e.getText().toUpperCase()));
							}
							// child "text"
							if (e.getName().compareTo("text")==0){
								rule.setMessageXML(e.getText());
								rule.setMessage(TLanguage.getString("TShowResults."
										+ e.getText()));
							}
						} while (i_feat.hasNext());
						ruleList.add(rule);
					}
				} while (i.hasNext());
			}
		} catch (JDOMException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return ruleList;
	}
	
}
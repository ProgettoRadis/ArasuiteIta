package araword.configuration;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import araword.G;

/**
 * Static class that manages the application configuration. It allows to
 * write and read from a XML file
 */

public class TSetup {
	private static String CONFIGURATION_SYSTEM_FILE_PATH = "." + File.separator + "conf" + File.separator + "AWSystem.conf";
	private static String CONFIGURATION_USER_FILE_PATH = "." + File.separator + "conf" + File.separator + "AWUserDefaults.conf";
	

	private static File CONFIGURATION_SYSTEM_FILE = new File(CONFIGURATION_SYSTEM_FILE_PATH);
	private static File CONFIGURATION_USER_FILE = new File(CONFIGURATION_USER_FILE_PATH);
	
	/**
	 * Loads the configuration files
	 */
	public static void load() {
		
		// System config.
		
		File fileXML = CONFIGURATION_SYSTEM_FILE;
		SAXBuilder builder = new SAXBuilder(false);      
        try { 	
			Document docXML = builder.build(fileXML);
			Element root = docXML.getRootElement();

			List<Element> languages = root.getChild("applicationLanguages").getChildren("language");
			Iterator<Element> j = languages.iterator();
			G.applicationLanguages = new String[languages.size()];
			int i=0;
			while (j.hasNext()){
				Element e = j.next();
				G.applicationLanguages[i] = e.getText();
				i++;
			}

			languages = root.getChild("documentLanguages").getChildren("language");
			j = languages.iterator();
			G.documentLanguages = new String[languages.size()];
			i=0;
			while (j.hasNext()){
				Element e = j.next();
				G.documentLanguages[i] = e.getText();
				i++;
			}

			List<Element> wordTypes = root.getChild("wordTypeColors").getChildren("wordtype");
			j = wordTypes.iterator();
			while (j.hasNext()){
				Element e = j.next();
				String key = e.getAttributeValue("id");
				Element color = e.getChild("color");
				Element r = color.getChild("r");
				int red = Integer.parseInt(r.getText());
				Element g = color.getChild("g");
				int green = Integer.parseInt(g.getText());
				Element b = color.getChild("b");
				int blue = Integer.parseInt(b.getText());
				// Introduce type-border pair on hashmap.
//				System.out.println("asdfasdf: "+key+" "+red+" "+green+" "+blue);
				G.borders.put(key, new CompoundBorder(new EmptyBorder(5,5,5,5),BorderFactory.createLineBorder(new Color(red,green,blue),3)));
			}

			Element databaseName = root.getChild("databaseName");
			G.databaseName = databaseName.getText();

        }
        catch (Exception exc) {System.out.println(exc);}
        
        // User defaults
        
        fileXML = CONFIGURATION_USER_FILE;
		builder = new SAXBuilder(false); 
        try {
        	Document docXML = builder.build(fileXML);
			Element root = docXML.getRootElement();
			Element preferencesElement = root.getChild("preferences");

			List list = preferencesElement.getChildren("applicationLanguage");
       	 	Iterator l = list.iterator();
       	 	while(l.hasNext()){
       	 		Element element = (Element)l.next();
	       	 	String language = element.getText();
				if (language != null) {				
					if (TLanguage.languageExists(language)) {
//						setLanguage(language);
						G.applicationLanguage = language;
					}
				}
       	 	}

       	 	list = preferencesElement.getChildren("documentLanguage");
    	 	l = list.iterator();
    	 	while(l.hasNext()){
    	 		Element element = (Element)l.next();
	       	 	String language = element.getText();
				if (language != null) {
					G.defaultDocumentLanguage = language;
				}
    	 	}

    	 	list = preferencesElement.getChildren("imagesSize");
    	 	l = list.iterator();
    	 	while(l.hasNext()){
    	 		Element element = (Element)l.next();
	       	 	String language = element.getText();
				if (language != null) {
					G.defaultImagesSize = Integer.parseInt(language);
				}
    	 	}
    	 	
    	 	list = preferencesElement.getChildren("classicIcons");
    	 	l = list.iterator();
    	 	while(l.hasNext()){
    	 		Element element = (Element)l.next();
	       	 	String language = element.getText();
				if (language != null) {
					G.classicIcons = language.equals("yes");
				}
    	 	}
    	 	
    	 	list = preferencesElement.getChildren("iconsSize");
    	 	l = list.iterator();
    	 	while(l.hasNext()){
    	 		Element element = (Element)l.next();
	       	 	String language = element.getText();
				if (language != null) {
					G.iconsSize = Integer.parseInt(language);
				}
    	 	}

    	 	list = preferencesElement.getChildren("maxLengthCompoundWords");
    	 	l = list.iterator();
    	 	while(l.hasNext()){
    	 		Element element = (Element)l.next();
	       	 	String language = element.getText();
				if (language != null) {
					G.maxLengthCompoundWords = Integer.parseInt(language);
				}
    	 	}

    	 	list = preferencesElement.getChildren("maxUndoLevel");
    	 	l = list.iterator();
    	 	while(l.hasNext()){
    	 		Element element = (Element)l.next();
	       	 	String language = element.getText();
				if (language != null) {
					G.maxUndoLevel = Integer.parseInt(language);
				}
    	 	}

    	 	// Obsolete??
//    	 	String whichOS = System.getProperty("os.name");
//    	 	if (whichOS.toUpperCase().indexOf("WINDOWS")!=-1) {
//    	 		list = preferencesElement.getChildren("pictogramsPathWindows");
//        	 	l = list.iterator();
//        	 	while(l.hasNext()){
//        	 		Element element = (Element)l.next();
//    	       	 	String language = element.getText();
//    				if (language != null) {
//    					G.pictogramsPath = language;
//    				}
//        	 	}
//    	 	}
//    	 	else { //correct for Linux and MacOS
//	    	 	list = preferencesElement.getChildren("pictogramsPathLinux");
//	    	 	l = list.iterator();
//	    	 	while(l.hasNext()){
//	    	 		Element element = (Element)l.next();
//		       	 	String language = element.getText();
//					if (language != null) {
//						G.pictogramsPath = language;
//					}
//	    	 	}
//    	 	}

    	 	list = preferencesElement.getChildren("font");
    	 	l = list.iterator();
    	 	while(l.hasNext()){
    	 		String name = "";
    	 		int size = 12;
    	 		boolean bold = false;
    	 		boolean italic = false;
    	 		
    	 		Element element = (Element)l.next();
    	 		
    	 		List list2 = element.getChildren("name");
        	 	Iterator m = list2.iterator();
        	 	while(m.hasNext()){
        	 		Element element2 = (Element)m.next();
		       	 	String name2 = element2.getText();
					if (name2 != null) {
						name = name2;
					}
        	 	}
        	 	
        	 	list2 = element.getChildren("size");
        	 	m = list2.iterator();
        	 	while(m.hasNext()){
        	 		Element element2 = (Element)m.next();
		       	 	String name2 = element2.getText();
					if (name2 != null) {
						size = Integer.parseInt(name2);
					}
        	 	}
        	 	
        	 	list2 = element.getChildren("bold");
        	 	m = list2.iterator();
        	 	while(m.hasNext()){
        	 		Element element2 = (Element)m.next();
		       	 	String name2 = element2.getText();
					if (name2 != null) {
						bold = name2.equals("yes");
					}
        	 	}
        	 	
        	 	list2 = element.getChildren("italic");
        	 	m = list2.iterator();
        	 	while(m.hasNext()){
        	 		Element element2 = (Element)m.next();
		       	 	String name2 = element2.getText();
					if (name2 != null) {
						italic = name2.equals("yes");
					}
        	 	}
        	 	
        	 	int style = Font.PLAIN;
        	 	if (bold) style ^= Font.BOLD;
        	 	if (italic) style ^= Font.ITALIC;
        	 	
        	 	G.defaultFont = new Font(name,style,size);
    	 	}
    	 	
    	 	list = preferencesElement.getChildren("color");
    	 	l = list.iterator();
    	 	while(l.hasNext()){
				Element e = (Element)l.next();
				Element r = e.getChild("r");
				int red = Integer.parseInt(r.getText());
				Element g = e.getChild("g");
				int green = Integer.parseInt(g.getText());
				Element b = e.getChild("b");
				int blue = Integer.parseInt(b.getText());
				// Introduce type-border pair on hashmap.
				G.defaultColor = new Color(red,green,blue);
			}
    	 	
    	 	list = preferencesElement.getChildren("textBelowPictogram");
    	 	l = list.iterator();
    	 	while(l.hasNext()){
    	 		Element element = (Element)l.next();
	       	 	String language = element.getText();
				if (language != null) {
					G.defaultTextBelowPictogram = language.equals("yes");
				}
    	 	}
			
		}
        catch (Exception exc) {System.out.println(exc);}
	}
	
	/**
	 * Saves the configuration file (user defaults)
	 * 
	 */
	public static void save() {
		
		try {		
			Element config = new Element("config");
			
			Element preferences = new Element("preferences");
			
			Element applicationLanguage = new Element("applicationLanguage");
			applicationLanguage.addContent(G.applicationLanguage);
			preferences.addContent(applicationLanguage);
			
			Element documentLanguage = new Element("documentLanguage");
			documentLanguage.addContent(G.defaultDocumentLanguage);
			preferences.addContent(documentLanguage);
			
			Element imagesSize = new Element("imagesSize");
			imagesSize.addContent(Integer.toString(G.defaultImagesSize));
			preferences.addContent(imagesSize);
			
			Element classicIcons = new Element("classicIcons");
			if (G.classicIcons) classicIcons.addContent("yes");
			preferences.addContent(classicIcons);
			
			Element iconsSize = new Element("iconsSize");
			iconsSize.addContent(Integer.toString(G.iconsSize));
			preferences.addContent(iconsSize);
			
			Element maxLengthCompoundWords = new Element("maxLengthCompoundWords");
			maxLengthCompoundWords.addContent(Integer.toString(G.maxLengthCompoundWords));
			
			preferences.addContent(maxLengthCompoundWords);
			
			Element maxUndoLevel = new Element("maxUndoLevel");
			maxUndoLevel.addContent(Integer.toString(G.maxUndoLevel));
			
			preferences.addContent(maxUndoLevel);
			
//			Element pictogramsPathWindows = new Element("pictogramsPathWindows");			
//					//Cambio:
//			//pictogramsPathWindows.addContent(G.pictogramsPath.replaceAll("/", "\\"));			
//			pictogramsPathWindows.addContent(G.pictogramsPath.replaceAll("/", "\\\\"));				
//			preferences.addContent(pictogramsPathWindows);
//			Element pictogramsPathLinux = new Element("pictogramsPathLinux");
//			// Two slashes for the compiler ( \\\\ --> \\) and two for the regex engine (\\, escaped \). Java is easy :)
//			pictogramsPathLinux.addContent(G.pictogramsPath.replaceAll("\\\\", "/"));
//			
//			preferences.addContent(pictogramsPathLinux);
			
			Element font = new Element("font");
	
			Element name = new Element("name");
			name.addContent(G.defaultFont.getName());
			font.addContent(name);
			
			Element size = new Element("size");
			size.addContent(Integer.toString(G.defaultFont.getSize()));
			font.addContent(size);
			
			Element bold = new Element("bold");
			if (G.defaultFont.isBold()) bold.addContent("yes");
			else bold.addContent("no");
			font.addContent(bold);
			
			Element italic = new Element("italic");
			if (G.defaultFont.isItalic()) italic.addContent("yes");
			else italic.addContent("no");
			font.addContent(italic);
			
			preferences.addContent(font);
			
			Element color = new Element("color");
			
			Element r = new Element("r");
			r.addContent(Integer.toString(G.defaultColor.getRed()));
			color.addContent(r);
			
			Element g = new Element("g");
			g.addContent(Integer.toString(G.defaultColor.getGreen()));
			color.addContent(g);
			
			Element b = new Element("b");
			b.addContent(Integer.toString(G.defaultColor.getBlue()));
			color.addContent(b);
			
			preferences.addContent(color);
			
			Element textBelowPictogram = new Element("textBelowPictogram");
			if (G.defaultTextBelowPictogram) textBelowPictogram.addContent("yes");
			else textBelowPictogram.addContent("no");
			
			preferences.addContent(textBelowPictogram);
			
			config.addContent(preferences);
			
			Document doc = new Document(config);
			//New DB
			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
			FileOutputStream f = new FileOutputStream(CONFIGURATION_USER_FILE_PATH);
			
			out.output(doc,f);
			f.flush();
			f.close();
		}
		catch (Exception exc) {System.out.println(exc);}
	}
	
	public static String getImagesFolder() {
		return G.pictogramsPath;
	}
}

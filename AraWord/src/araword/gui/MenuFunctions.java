// Menu functions: Cut, copy and paste; undo and redo functions; find function; open file, save, export to PDF...

package araword.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultStyledDocument;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

import araword.utils.TFileHandler;
import araword.G;
import araword.classes.AWElement;
import araword.classes.AWTextField;
import araword.configuration.TLanguage;
import araword.db.DBManagement;
import araword.utils.ScreenImage;
import araword.utils.TextUtils;

import com.itextpdf.text.Header;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import database.DB;
import database.DBTasks;
import dialogs.mainFrame;

import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class MenuFunctions {
	

	class Task extends SwingWorker<Void, Void> {
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            java.util.Random random = new java.util.Random();
            int progress = 0;
            //Initialize progress property.
            setProgress(0);
            while (progress < 100) {
                //Sleep for up to one second.
                try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException ignore) {}
                //Make random progress.
                progress += random.nextInt(10);
                setProgress(Math.min(progress, 100));
                System.out.println("background");
            }
            return null;
        }
 
        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            System.out.println("Done!\n");
        }
    }

	public static void fileOpen() {
		try {
			
            JFileChooser fc = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(TLanguage.getString("ARAWORD_DOCUMENTS"), "awd", "awz");
            fc.setFileFilter(filter);
            fc.setAcceptAllFileFilterUsed(false);
            if (fc.showOpenDialog(G.textZone) == JFileChooser.APPROVE_OPTION) {
                G.elementList.clear();
                G.textZone.setText("");
                File fileXML = fc.getSelectedFile();
                String extension = fileXML.getName().substring(fileXML.getName().length()-4, fileXML.getName().length());
                System.out.println("****FILE OPEN****");
                System.out.println("extension="+extension);
                if (extension.equals(".awd")) fileOpenAWD(fileXML);
                if (extension.equals(".awz")) fileOpenAWZ(fileXML, tempDirectory);
            } 
            
		} catch (Exception exc) {System.out.println(exc);}
	}
	
	
	
	
	public static void fileOpenAWZ(File srcFile, File dstDir) throws IOException {
		System.out.println("OPEN AWZ");
		
		// Create buffer
				byte[] buffer = new byte[ZIP_BUFFER_SIZE];
				int bytes;
				// Get zip file entries
				ZipFile zipFile = new ZipFile(srcFile);
				Enumeration entries = zipFile.entries();
				// For each entry
				while (entries.hasMoreElements()) {
					ZipEntry entry = (ZipEntry)entries.nextElement();
					// If the entry is a directory, create it
					if (entry.isDirectory()) {
						File newDirectory = new File(entry.getName());
						if (!newDirectory.exists())
							newDirectory.mkdirs();
						continue;
					}
					// If the entry is a file, get it
					File newFile = new File(dstDir, entry.getName().replace('\\', '/'));
					// Check if its directory exists and create it if necessary
					File newFileDir = newFile.getParentFile();
					if (!newFileDir.exists())
						newFileDir.mkdirs();
					// Create the input and output streams
					InputStream in = zipFile.getInputStream(entry);
					OutputStream out = new BufferedOutputStream(new FileOutputStream(
							newFile));
					// Copy one into the other
					while ((bytes = in.read(buffer)) >= 0)
						out.write(buffer, 0, bytes);
					// Close input and output streams
					in.close();
					out.close();
				}
				// Close the zip file
				zipFile.close();
				G.activeDocumentFileName=srcFile.getAbsolutePath();
				
				File AWDFile= new File(dstDir, "base.awd");
				System.out.println("UNZIP en ="+ AWDFile.getAbsolutePath());
				fileOpenAWD(AWDFile);
				
				
	}
	
	
	
	
	
	 public static void fileOpenAWD(File fileXML) {
              
            	  
            	  //create progressbar gui     
		          final JProgressBar pb;
            	  pb = new JProgressBar(0,100);
            	  pb.setPreferredSize(new Dimension(175,20));
            	  pb.setString(TLanguage.getString("ESPERA"));
            	  pb.setStringPainted(true);
            	  pb.setValue(0);
            	  


            	  javax.swing.JLabel label = new javax.swing.JLabel(TLanguage.getString("ESPERA"));
            	  javax.swing.JPanel center_panel = new javax.swing.JPanel();
            	  center_panel.add(label);
            	  center_panel.add(pb);

            	  final javax.swing.JDialog dialog = new javax.swing.JDialog((javax.swing.JFrame)null, TLanguage.getString("ESPERA"));
            	  dialog.getContentPane().add(center_panel, BorderLayout.CENTER);
            	  dialog.pack();
            	  dialog.setVisible(true);

            	  dialog.setLocationRelativeTo(null); // center on screen
            	  dialog.setLocation(150,125); // position by coordinates
            	  dialog.toFront(); // raise above other java windows
          		  
            	  
            	  final File f=fileXML;
            	  
            	  SwingWorker worker = new SwingWorker<Void, Void>() {
            		    @Override
            		    public Void doInBackground() {
            		    	System.out.println("****** BackGround");     	                
            	                
            		    	backgroundOpenAWD(f, pb); 
            		    	
            		    	return null;
            		    	
            		    }

            		    @Override
            		    public void done() {
            		    	Toolkit.getDefaultToolkit().beep();            	            
            	            System.out.println("******* Done!");
            	            dialog.dispose();
            	            deleteTempDirectory();
            	            
            	            try {
            	                for (AWElement elem2: G.elementList) {
            	                    if (elem2.getType()==0) // Pictogram
            	                        G.textZone.insertComponent(elem2);
            	                    else
            	                    	
            	                        G.textZone.getStyledDocument().insertString(G.textZone.getStyledDocument().getLength(), elem2.getSeparator(), null);
            	                }
            	                G.textZone.revalidate();
            	                G.elementList.get(0).getTextField().requestFocusInWindow();
            	                
            	                
            	                } catch (Exception exc) {System.out.println(exc);}
            		    }
            		};
            	worker.execute();
	 }
      
	 public static void backgroundOpenAWD(File fileXML, JProgressBar pb) {
            	try{
            	  
            		System.out.println("PATH="+fileXML.getAbsolutePath());
            		
	                SAXBuilder builder = new SAXBuilder(false);
	                InputSource is = new InputSource("file:///"+fileXML.getCanonicalPath());
	                is.setEncoding("UTF-8");
	                Document docXML = builder.build(is);

	    			Element root = docXML.getRootElement();
	    			Element preferencesElement = root.getChild("preferences");

	           	 	List list = preferencesElement.getChildren("documentLanguage");
	        	 	Iterator l = list.iterator();
	        	 	while(l.hasNext()){
	        	 		Element element = (Element)l.next();
	    	       	 	String language = element.getText();
	    				if (language != null) {
	    					G.documentLanguage = language;
	    				}
	        	 	}

	        	 	list = preferencesElement.getChildren("imagesSize");
	        	 	l = list.iterator();
	        	 	while(l.hasNext()){
	        	 		Element element = (Element)l.next();
	    	       	 	String language = element.getText();
	    				if (language != null) {
	    					G.imagesSize = Integer.parseInt(language);
	    					ImageIcon image2 = new ImageIcon("resources/404.jpg");
	    					G.notFound = new ImageIcon(image2.getImage().getScaledInstance(1,G.imagesSize,0));
	    				}
	        	 	}

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
	            	 	
	            	 	G.font = new Font(name,style,size);
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
//	    				System.out.println("asdfasdf: "+key+" "+red+" "+green+" "+blue);
	    				G.color = new Color(red,green,blue);
	    			}
	        	 	
	        	 	list = preferencesElement.getChildren("textBelowPictogram");
	        	 	l = list.iterator();
	        	 	while(l.hasNext()){
	        	 		Element element = (Element)l.next();
	    	       	 	String language = element.getText();
	    				if (language != null) {
	    					G.textBelowPictogram = language.equals("yes");
	    				}
	        	 	}
	        	 	
	        	 	//*******************************
	        	 	//import customs pictos if needed
	        	 	//*******************************
	                		                	
	                	
	                	String input=fileXML.getParent()+File.separator+"exportbbdd";
	                	DBManagement.importDB2(input, pb, false);
	                
	        	 	
	        	 	// Document itself.
	        	 	
	        	 	int pos = 0;
	        	 	Element contentElement = root.getChild("content");
	           	 	list = contentElement.getChildren("AWElement");
	        	 	l = list.iterator();
	        	 	while(l.hasNext()){
	        	 		Element element = (Element)l.next();
	        	 		String type = element.getAttributeValue("type");
	        	 		if (type.equals("pictogram")) {
	        	 			AWElement newElem = null;
	        	 			String word = "";
		        	 		int numPictogram = 0;
		        	 		boolean visibleImage = true;
		        	 		boolean visibleBorder = true;
		        	 		String wordSearchBD = "";
		        	 		
	        	 			List list2 = element.getChildren("word");
	        	 			if (list2.isEmpty()) word = "";
		            	 	else word = ((Element)list2.iterator().next()).getText();
	        	 			
	        	 			
	        	 			
	        	 			// All of the below ones are optional, with default values if not present. This reduces disk space needed for AraWord documents.
		            	 	
		            	 	list2 = element.getChildren("numPictogram");
		            	 	if (list2.isEmpty()) numPictogram = 0;
		            	 	else numPictogram = Integer.parseInt(((Element)list2.iterator().next()).getText());

		            	 	list2 = element.getChildren("visibleImage");
		            	 	if (list2.isEmpty()) visibleImage = true;
		            	 	else visibleImage = ((Element)list2.iterator().next()).getText().equals("no");
		            	 	
		            	 	list2 = element.getChildren("visibleBorder");
		            	 	if (list2.isEmpty()) visibleBorder = true;
		            	 	else visibleBorder = ((Element)list2.iterator().next()).getText().equals("no");
		            	 	
		            	 	list2 = element.getChildren("wordSearchBD");
		            	 	if (list2.isEmpty()) wordSearchBD = "";
		            	 	else wordSearchBD = ((Element)list2.iterator().next()).getText();
		            	 	
		            	 	//image file
	        	 			//it has to be translated into num pictogram
	        	 			list2 = element.getChildren("namePicto");
	        	 			String namePicto="";
	        	 			if (!list2.isEmpty()) {
	        	 				
	        	 				namePicto=((Element)list2.iterator().next()).getText();
	        	 				
	        	 				if (wordSearchBD.equals("")) 
	        	 					numPictogram=DBManagement.searchnumPictoOnDB(word, namePicto);
	        	 				else
	        	 					numPictogram=DBManagement.searchnumPictoOnDB(wordSearchBD, namePicto);
	        	 				
	        	 				
	        	 			} else {
	        	 				
	        	 			}
		            	 	
			            	 	if (wordSearchBD.equals("")) newElem = AWElement.createPictogramElement(pos,word,numPictogram,visibleImage,visibleBorder,true,"");
			            	 	else newElem = AWElement.createPictogramElement(pos,word,numPictogram,visibleImage,visibleBorder,false,wordSearchBD);
		            	 	
		            	 	G.elementList.add(newElem);
		            	 	pos++;
	        	 		}
	        	 		else if (type.equals("separator")) {

	        	 			AWElement newElem = null;
	        	 			String whichSeparator = element.getText();
	        	 			if (whichSeparator.equals("ws")) newElem = AWElement.createSeparatorElement(pos,G.separatorSpace);
	        	 			else if (whichSeparator.equals("nl")) newElem = AWElement.createSeparatorElement(pos,"\n");
	        	 			else if (whichSeparator.equals("tab")) newElem = AWElement.createSeparatorElement(pos,"\t");
	        	 			else System.out.println("Something wrong loading file, pos: "+pos);
	        	 			G.elementList.add(newElem);
		            	 	pos++;
	        	 		}
	        	 		else System.out.println("Something wrong loading file, pos: "+pos);
	        	 	}
	        	 	
	        	 	
	        	 	
	        	 	 
              } catch (Exception exc) {System.out.println(exc);}

			
}
	
	
	
	private static void fileSaveCommonAWZ(File zipFile) throws IOException {
		
		 //create progressbar gui     
        final JProgressBar pb;
  	  pb = new JProgressBar(0,100);
  	  pb.setPreferredSize(new Dimension(175,20));
  	  pb.setString(TLanguage.getString("ESPERA"));
  	  pb.setStringPainted(true);
  	  pb.setValue(0);
  	  


  	  javax.swing.JLabel label = new javax.swing.JLabel(TLanguage.getString("ESPERA"));
  	  javax.swing.JPanel center_panel = new javax.swing.JPanel();
  	  center_panel.add(label);
  	  center_panel.add(pb);

  	  final javax.swing.JDialog dialog = new javax.swing.JDialog((javax.swing.JFrame)null, TLanguage.getString("ESPERA"));
  	  dialog.getContentPane().add(center_panel, BorderLayout.CENTER);
  	  dialog.pack();
  	  dialog.setVisible(true);

  	  dialog.setLocationRelativeTo(null); // center on screen
  	  dialog.setLocation(150,125); // position by coordinates
  	  dialog.toFront(); // raise above other java windows
		  
  	  
  	  final File zipFileBG=zipFile;
  	  
  	  SwingWorker worker = new SwingWorker<Void, Void>() {
  		    @Override
  		    public Void doInBackground() {
  		    	System.out.println("****** BackGround");   		
				System.out.println("GRABAR ZIP");
				System.out.println("TEMP DIR="+tempDirectoryPath);
				System.out.println("FileName="+ zipFileBG.getName());
				cleanTempDirectory();
				File fileInTemp = new File(tempDirectoryPath, "base");
				
				fileSaveCommonAWD(fileInTemp, pb);
				//zip temp folder
				System.out.println("voy a zipear");
				try {
					zip(tempDirectory, zipFileBG);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				deleteTempDirectory();
				return null;
  		    	
  		    }

  		    @Override
  		    public void done() {
  		    	Toolkit.getDefaultToolkit().beep();            	            
  	            System.out.println("******* Done!");
  	            dialog.dispose();
  		    }
  		};
  	worker.execute();
	}
	
	private static void fileSaveCommonAWD(File file, JProgressBar pb) {             
  	                
  		    	
		System.out.println("=============== FILESAVECOMMONAWD =============");
		try {
			
			
			
			
			Element document = new Element("document");
			
			Element preferences = new Element("preferences");
			
			Element documentLanguage = new Element("documentLanguage");
			documentLanguage.addContent(G.documentLanguage);
			preferences.addContent(documentLanguage);
			
			Element imagesSize = new Element("imagesSize");
			imagesSize.addContent(Integer.toString(G.imagesSize));
			preferences.addContent(imagesSize);
			
			Element font = new Element("font");
			
			Element name = new Element("name");
			name.addContent(G.font.getName());
			font.addContent(name);
			
			Element size = new Element("size");
			size.addContent(Integer.toString(G.font.getSize()));
			font.addContent(size);
			
			Element bold = new Element("bold");
			if (G.font.isBold()) bold.addContent("yes");
			else bold.addContent("no");
			font.addContent(bold);
			
			Element italic = new Element("italic");
			if (G.font.isItalic()) italic.addContent("yes");
			else italic.addContent("no");
			font.addContent(italic);
			
			preferences.addContent(font);
			
			Element color = new Element("color");
			
			Element r = new Element("r");
			r.addContent(Integer.toString(G.color.getRed()));
			color.addContent(r);
			
			Element g = new Element("g");
			g.addContent(Integer.toString(G.color.getGreen()));
			color.addContent(g);
			
			Element b = new Element("b");
			b.addContent(Integer.toString(G.color.getBlue()));
			color.addContent(b);
			
			preferences.addContent(color);
			
			Element textBelowPictogram = new Element("textBelowPictogram");
			if (G.textBelowPictogram) textBelowPictogram.addContent("yes");
			else textBelowPictogram.addContent("no");
			
			preferences.addContent(textBelowPictogram);
			
			//to export  pictos in AWZ
			List<String> customs = new ArrayList<String>();
			List<String> customsWords = new ArrayList<String>();		
			System.out.println("**CReACION LISTAS CUSTOMS*****");
			
			
			// Document itself.
			
			Element content = new Element("content");
			
			for (AWElement elem: G.elementList) {
				Element AWElement = new Element("AWElement");
                if (elem.getType()==0) { // Pictogram
                	AWElement.setAttribute("type","pictogram");
                	
                	Element word = new Element("word");
                	word.setText(elem.getTextField().getText());
                	AWElement.addContent(word); // The only one compulsory.
                	
                	
                	
                	
                		
                		if (elem.getImageName().length()>0) {
	                		Element namePictogram = new Element("namePicto");
	                    	namePictogram.setText(elem.getImageName());
	                    	AWElement.addContent(namePictogram);
                		}
                	
                	if (!elem.isVisibleImage()) {
                    	Element visibleImage = new Element("visibleImage");
                    	visibleImage.setText("no");
                    	AWElement.addContent(visibleImage);
                	}
                	if (!elem.isVisibleBorder()) {
                		Element visibleBorder = new Element("visibleBorder");
                		visibleBorder.setText("no");
                		AWElement.addContent(visibleBorder);
                	}
                	
                	//custom image
            		
            		            		
            		if (elem.getImageName().length()>0)
            			customs.add(elem.getImageName());
            		
                	if (!elem.isSearchImageOnBD()) {
                		System.out.println("***SEARCH IMAGE on DB");
                		System.out.println(elem.getWordSearchBD());
                		System.out.println(elem.getInfinitive());
                		if (elem.getInfinitive()!=null){
                			Element wordSearchBD = new Element("wordSearchBD");
	                		wordSearchBD.setText(elem.getWordSearchBD());
	                		AWElement.addContent(wordSearchBD);
                			//add BBDD word for custom image
                    		if (elem.getImageName().length()>0) customsWords.add(araword.classes.AWElement.filter(elem.getInfinitive()));
                		} else {
	                		Element wordSearchBD = new Element("wordSearchBD");
	                		wordSearchBD.setText(elem.getWordSearchBD());
	                		AWElement.addContent(wordSearchBD);
	                		//add BBDD word for custom image
	                		if (elem.getImageName().length()>0) customsWords.add(araword.classes.AWElement.filter(elem.getWordSearchBD()));
                		}
                	} else if (elem.getInfinitive()!=null){
                		
                		//add BBDD word for custom image
                		if (elem.getImageName().length()>0) customsWords.add(araword.classes.AWElement.filter(elem.getInfinitive()));
                	} else {
                		//add BBDD word for custom image
                		if (elem.getImageName().length()>0) customsWords.add(araword.classes.AWElement.filter(elem.getTextField().getText()));
                	}
                }
                else {
                	AWElement.setAttribute("type","separator");
                	if (elem.getSeparator().equals(G.separatorSpace)) AWElement.setText("ws");
                	else if (elem.getSeparator().equals("\n")) AWElement.setText("nl");
                	else if (elem.getSeparator().equals("\t")) AWElement.setText("tab");
                	else System.out.println("Problem saving file");
                }
                content.addContent(AWElement);
            }
			
			document.addContent(preferences);
			document.addContent(content);
			System.out.println("aqui ocurre el error");
			Document doc = new Document(document);
			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
			FileOutputStream f = null;
			if (file.getCanonicalPath().endsWith(".awd"))
				f = new FileOutputStream(file.getCanonicalPath());
			else f = new FileOutputStream(file.getCanonicalPath()+".awd");
			out.output(doc,f);
			f.flush();
			f.close();
			System.out.println("ya ha ocurrido el error");
			
			if (customs.size()>0) {
				// ************* Export  pictos library in XML
				System.out.println("exportar base de datos");
				System.out.println("Customs="+ customs);
				System.out.println("CustomsWords="+ customsWords);
				DBManagement.exportPictosAWZ(file.getParent()+File.separator+"exportbbdd", G.documentLanguage, customs, customsWords, pb);
			}		
			 
		}
		catch (Exception e) {System.out.println(e); e.printStackTrace();}
	}
	
	private static void fileSaveCommon() {
		try {
			JFileChooser fc = new JFileChooser();
	        FileNameExtensionFilter filter1 = new FileNameExtensionFilter(TLanguage.getString("ARAWORD_DOCUMENTS"), "awz");
	        fc.addChoosableFileFilter(filter1);
	        fc.setFileFilter(filter1);
	        fc.setAcceptAllFileFilterUsed(false);
	        if (fc.showSaveDialog(G.textZone) == JFileChooser.APPROVE_OPTION) {
	        	
	        	String withExtension = fc.getSelectedFile().getAbsolutePath();
	        	if( !withExtension.toLowerCase().endsWith( ".awz" ) )
	        		   withExtension += ".awz";
	        	File file = new File( withExtension );
	        	G.activeDocumentFileName=file.getAbsolutePath(); 
	        	
	            //fileSaveCommonAWD(file);  
	            fileSaveCommonAWZ(file); 
	        }
		}
	    catch (Exception exc) {System.out.println(exc);}
	}
	
	public static void fileSave() {
		try {
            boolean isValid = false;
            // Avoid problems with empty or separator-only files. Don't let save them.
            for (AWElement elem2: G.elementList) {
                if (elem2.getType()==0) { // pictograma normal
                    if (!elem2.getTextField().getText().equals("")) {
                        isValid = true;
                        break;
                    }
                }
            }
            if (!isValid) JOptionPane.showMessageDialog(null, TLanguage.getString("FILE_MENU_SAVE_UNABLE_SAVE_EMPTY_FILE"));
            else {
            	if (G.activeDocumentFileName.equals("")) {
            		// Not saved yet
            		fileSaveCommon();
            	}
            	else {
            		// Save as AWD with same name.
            		System.out.println("****** BARRA DE TITULO*******");
            		System.out.println(G.activeDocumentFileName);
            		fileSaveCommonAWZ(new File(G.activeDocumentFileName));
            	}
            }
        }
        catch (Exception exc) {System.out.println(exc);}
	}
	
	public static void fileSaveAs() {
		try {
            boolean isValid = false;
            // Avoid problems with empty or separator-only files. Don't let save them.
            
            for (AWElement elem2: G.elementList) {
                if (elem2.getType()==0) { // pictograma normal
                    if (!elem2.getTextField().getText().equals("")) {
                        isValid = true;
                        break;
                    }
                }
            }
            if (!isValid) JOptionPane.showMessageDialog(null, TLanguage.getString("FILE_MENU_SAVE_UNABLE_SAVE_EMPTY_FILE"));
            else {
                fileSaveCommon();
            }
        }
        catch (Exception exc) {System.out.println(exc);}
	}
	
	public static void fileExport() {
		try {
            boolean isValid = false;
            // Avoid problems with empty or separator-only files. Don't let save them.
            for (AWElement elem2: G.elementList) {
                if (elem2.getType()==0) { // pictograma normal
                    if (!elem2.getTextField().getText().equals("")) {
                        isValid = true;
                        break;
                    }
                }
            }
            if (!isValid) JOptionPane.showMessageDialog(null, TLanguage.getString("FILE_MENU_SAVE_UNABLE_SAVE_EMPTY_FILE"));
            else {
            	try {
        			JFileChooser fc = new JFileChooser();
        	        FileNameExtensionFilter filter2 = new FileNameExtensionFilter(TLanguage.getString("PDF_FILES"), "pdf");
        	        fc.addChoosableFileFilter(filter2);
        	        FileNameExtensionFilter filter3 = new FileNameExtensionFilter(TLanguage.getString("BMP_FILES"), "bmp");
        	        fc.addChoosableFileFilter(filter3);
        	        FileNameExtensionFilter filter4 = new FileNameExtensionFilter(TLanguage.getString("JPG_FILES"), "jpg");
        	        fc.addChoosableFileFilter(filter4);
        	        fc.setFileFilter(filter4);
        	        fc.setAcceptAllFileFilterUsed(false);
        	        if (fc.showSaveDialog(G.textZone) == JFileChooser.APPROVE_OPTION) {
        	            File file = fc.getSelectedFile();
        	            if (fc.getFileFilter()==filter2) {
        					com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4);
        					PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file.getCanonicalPath()+"Pag1.pdf"));
        					document.open();
        					PdfContentByte cb = writer.getDirectContent();
        	               
        	               	JTextPane tempTP = new JTextPane();
        					tempTP.setSize((int)PageSize.A4.getWidth(), (int)PageSize.A4.getHeight());
        					tempTP.setMinimumSize(new Dimension((int)PageSize.A4.getWidth(), (int)PageSize.A4.getHeight()));
        					tempTP.setMaximumSize(new Dimension((int)PageSize.A4.getWidth(), (int)PageSize.A4.getHeight()));
        					tempTP.setPreferredSize(new Dimension((int)PageSize.A4.getWidth(), (int)PageSize.A4.getHeight()));
        					tempTP.setMargin(new java.awt.Insets(10, 10, 10, 10));
        					int numPixelsHor = (int) PageSize.A4.getWidth();
        					int numPixelsVer = (int) PageSize.A4.getHeight();
        					int numPixelsHorConsumed = 0;
        					int numPixelsVerConsumed = (int)G.elementList.get(0).getSize().getHeight();
        					int pxPerTab = 450 / 8; // Very rough estimation.
        					int pxPerSpace = 10;
        					int numPages = 1;
        					boolean justReadNewLine = false;
        					// If two or more \n are present in the text, the first one consumes elem.getHeight() pixels, but the second,
        					// third... consume only about 15 pixels.
        					int pxPerNewLine = 15;
        					
        					for (AWElement elem: G.elementList) {
        	//					System.out.println("Nuevo elemento: "+numPages+" "+numPixelsVerConsumed+" "+numPixelsHorConsumed);
        						if (elem.getType()==0) { // Pictogram
        							justReadNewLine = false;
        							numPixelsHorConsumed += elem.getSize().getWidth();
        							if (numPixelsHorConsumed>numPixelsHor) {
        								numPixelsVerConsumed += elem.getSize().getHeight();
        								numPixelsHorConsumed = (int)elem.getSize().getWidth();
        							}
        							if (numPixelsVerConsumed>numPixelsVer) {
        								numPixelsVerConsumed = (int)elem.getSize().getHeight();
        								Graphics2D g2 = cb.createGraphics(PageSize.A4.getWidth(), PageSize.A4.getHeight(), true, 0.95f);
        								
        								JDialog dlg = new JDialog();
        								JScrollPane scrollPane = new JScrollPane(tempTP);
        								dlg.getContentPane().add(scrollPane, BorderLayout.CENTER);
        								dlg.pack();
        								
        								tempTP.print(g2);
        								g2.dispose();
        								document.close();
        								
        								numPages++;
        								document = new com.itextpdf.text.Document(PageSize.A4);
        								writer = PdfWriter.getInstance(document, new FileOutputStream(file.getCanonicalPath()+"Pag"+numPages+".pdf"));
        								document.open();
        								cb = writer.getDirectContent();
        								tempTP.setText("");
        								tempTP.setStyledDocument(new DefaultStyledDocument());
        							}
        							tempTP.insertComponent((AWElement)TextUtils.copy(elem));
        						}
        						else { // Separator
        	                    	if (elem.getSeparator().equals("\n")) {
        	                    		if (justReadNewLine) numPixelsVerConsumed += pxPerNewLine;
        	                    		else numPixelsVerConsumed += elem.getSize().getHeight();
        	                    		justReadNewLine = true;
        								numPixelsHorConsumed = 0;
        								if (numPixelsVerConsumed>numPixelsVer) {
        									numPixelsVerConsumed = (int)elem.getSize().getHeight();
        									Graphics2D g2 = cb.createGraphics(PageSize.A4.getWidth(), PageSize.A4.getHeight(), true, 0.95f);
        									
        									JDialog dlg = new JDialog();
        									JScrollPane scrollPane = new JScrollPane(tempTP);
        									dlg.getContentPane().add(scrollPane, BorderLayout.CENTER);
        									dlg.pack();
        									
        									tempTP.print(g2);
        									g2.dispose();
        									document.close();
        									
        									numPages++;
        									document = new com.itextpdf.text.Document(PageSize.A4);
        									writer = PdfWriter.getInstance(document, new FileOutputStream(file.getCanonicalPath()+"Pag"+numPages+".pdf"));
        									document.open();
        									cb = writer.getDirectContent();
        									tempTP.setText("");
        									tempTP.setStyledDocument(new DefaultStyledDocument());
        								}
        	                    	}  
        	                        else if (elem.getSeparator().equals("\t")) {
        	                        	numPixelsHorConsumed += pxPerTab;
        	                        	if (numPixelsHorConsumed>numPixelsHor) {
        									numPixelsVerConsumed += elem.getSize().getHeight();
        									numPixelsHorConsumed = pxPerTab;
        								}
        								if (numPixelsVerConsumed>numPixelsVer) {
        									numPixelsVerConsumed = (int)elem.getSize().getHeight();
        									Graphics2D g2 = cb.createGraphics(PageSize.A4.getWidth(), PageSize.A4.getHeight(), true, 0.95f);
        									
        									JDialog dlg = new JDialog();
        									JScrollPane scrollPane = new JScrollPane(tempTP);
        									dlg.getContentPane().add(scrollPane, BorderLayout.CENTER);
        									dlg.pack();
        									
        									tempTP.print(g2);
        									g2.dispose();
        									document.close();
        									
        									numPages++;
        									document = new com.itextpdf.text.Document(PageSize.A4);
        									writer = PdfWriter.getInstance(document, new FileOutputStream(file.getCanonicalPath()+"Pag"+numPages+".pdf"));
        									document.open();
        									cb = writer.getDirectContent();
        									tempTP.setText("");
        									tempTP.setStyledDocument(new DefaultStyledDocument());
        								}
        	                        }
        	                        else if (elem.getSeparator().equals(G.separatorSpace)) {
        	                        	numPixelsHorConsumed+=pxPerSpace;
        	                        	if (numPixelsHorConsumed>numPixelsHor) {
        	                        		numPixelsVerConsumed += elem.getSize().getHeight();
        									numPixelsHorConsumed = pxPerSpace;
        	                        	}
        	                        	if (numPixelsVerConsumed>numPixelsVer) {
        	                        		numPixelsVerConsumed = (int)elem.getSize().getHeight();
        									Graphics2D g2 = cb.createGraphics(PageSize.A4.getWidth(), PageSize.A4.getHeight(), true, 0.95f);
        									
        									JDialog dlg = new JDialog();
        									JScrollPane scrollPane = new JScrollPane(tempTP);
        									dlg.getContentPane().add(scrollPane, BorderLayout.CENTER);
        									dlg.pack();
        									
        									tempTP.print(g2);
        									g2.dispose();
        									document.close();
        									
        									numPages++;
        									document = new com.itextpdf.text.Document(PageSize.A4);
        									writer = PdfWriter.getInstance(document, new FileOutputStream(file.getCanonicalPath()+"Pag"+numPages+".pdf"));
        									document.open();
        									cb = writer.getDirectContent();
        									tempTP.setText("");
        									tempTP.setStyledDocument(new DefaultStyledDocument());
        	                        	}
        	                        }
        	                        else System.out.println("Wrong element saving PDF");
        	                    	tempTP.getStyledDocument().insertString(tempTP.getStyledDocument().getLength(), elem.getSeparator(), null);	
        	                    }
        					}
        					Graphics2D g2 = cb.createGraphics(PageSize.A4.getWidth(), PageSize.A4.getHeight());
        					JDialog dlg = new JDialog();
        					JScrollPane scrollPane = new JScrollPane(tempTP);
        					dlg.getContentPane().add(scrollPane, BorderLayout.CENTER);
        					dlg.pack();
        					tempTP.print(g2);
        					g2.dispose();
        					document.close();		
        					// Merge PDF files
        					try {
        					      List<InputStream> pdfs = new ArrayList<InputStream>();
        					      for (int i=1;i<=numPages;i++) {	
        					    	  String withExtension = file.getCanonicalPath();
        					    		if( withExtension.toLowerCase().endsWith( ".pdf" ) )
        					        		   withExtension = withExtension.substring(0,withExtension.length()-4);
        					    	  pdfs.add(new FileInputStream(withExtension+"Pag"+i+".pdf"));
        					      }
        					      String withExtension = file.getCanonicalPath();
        				          if( !withExtension.toLowerCase().endsWith( ".pdf" ) )
        				        		   withExtension += ".pdf";        				        	
        					      OutputStream output = new FileOutputStream(withExtension);
        					      TextUtils.concatPDFs(pdfs, output, true);
        					      for (int i=1;i<=numPages;i++) {
        					    	  pdfs.get(i-1).close();
        					    	  withExtension = file.getCanonicalPath();
            				          if( withExtension.toLowerCase().endsWith( ".pdf" ) )
            				        	  withExtension = withExtension.substring(0,withExtension.length()-4);  
        					    	  File f = new File( withExtension+"Pag"+i+".pdf");
        					    	  f.delete();
        					      }
        				    } catch (Exception e) {
        				        e.printStackTrace();
        				    }
        	            }
        	            else if (fc.getFileFilter()==filter3) {
        	            	String withExtension = file.getCanonicalPath();
        		        	if( !withExtension.toLowerCase().endsWith( ".bmp" ) )
        		        		   withExtension += ".bmp";        		        	
        	            	ScreenImage.writeImage(ScreenImage.createImage(G.textZone),withExtension);
        	            }
        	            else if (fc.getFileFilter()==filter4) {
        	            	String withExtension = file.getCanonicalPath();
        	            	if( !withExtension.toLowerCase().endsWith( ".jpg" ) )
     		        		   withExtension += ".jpg";     		        	
        	            	ScreenImage.writeImage(ScreenImage.createImage(G.textZone),withExtension);
        	            }
        	            else {
        	            	System.out.println("fileExport problem: wrong filter selected");
        	            }
        	        }
        		}
        	    catch (Exception exc) {System.out.println(exc);}
            }
        }
        catch (Exception exc) {System.out.println(exc);}
	}
	
	
	
	public static void filePDFExport() {
		System.out.println("@@@ entro en file PDFExport");
		double pictoRatio=1.5;
		try {
            boolean isValid = false;
            // Avoid problems with empty or separator-only files. Don't let save them.
            for (AWElement elem2: G.elementList) {
                if (elem2.getType()==0) { // pictograma normal
                    if (!elem2.getTextField().getText().equals("")) {
                        isValid = true;
                        break;
                    }
                }
            }
            if (!isValid) JOptionPane.showMessageDialog(null, TLanguage.getString("FILE_MENU_SAVE_UNABLE_SAVE_EMPTY_FILE"));
            else {
            	try {
        			JFileChooser fc = new JFileChooser();
        	        FileNameExtensionFilter filter2 = new FileNameExtensionFilter(TLanguage.getString("PDF_FILES"), "pdf");
        	        fc.addChoosableFileFilter(filter2);
        	        
        	        fc.setFileFilter(filter2);
        	        fc.setAcceptAllFileFilterUsed(false);
        	        if (fc.showSaveDialog(G.textZone) == JFileChooser.APPROVE_OPTION) {
        	            
        	            String withExtension = fc.getSelectedFile().getAbsolutePath();
        	        	if( withExtension.toLowerCase().endsWith( ".pdf" ) )
        	        		   withExtension = withExtension.substring(0,withExtension.length()-4);
        	        	File file = new File( withExtension );
        	            if (fc.getFileFilter()==filter2) {
        					com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4);
        					PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file.getCanonicalPath()+"Pag1.pdf"));
        					araword.classes.HeaderFooter event = new araword.classes.HeaderFooter();
        					writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
        					writer.setPageEvent(event);
        					document.open();
        					
        					PdfContentByte cb = writer.getDirectContent();
        	               
        	               	JTextPane tempTP = new JTextPane();
        					tempTP.setSize((int)PageSize.A4.getWidth(), (int)PageSize.A4.getHeight());
        					tempTP.setMinimumSize(new Dimension((int)PageSize.A4.getWidth(), (int)PageSize.A4.getHeight()));
        					tempTP.setMaximumSize(new Dimension((int)PageSize.A4.getWidth(), (int)PageSize.A4.getHeight()));
        					tempTP.setPreferredSize(new Dimension((int)PageSize.A4.getWidth(), (int)PageSize.A4.getHeight()));
        					tempTP.setMargin(new java.awt.Insets(20, 20, 10, 10));
        					int numPixelsHor = (int) PageSize.A4.getWidth();
        					int numPixelsVer = (int) PageSize.A4.getHeight()-100;
        					System.out.println("A4: altura="+ numPixelsHor + "* anchura="+numPixelsVer);
        					
        					// ****** ratio from prev area to A4
        					G.imagesSize=(int) (G.imagesSize*2.0);
        					
        					
        					int pictoSize=(int)G.imagesSize;
        					int picLine=0;
        					int numPixelsHorConsumed = 0;
        					int numPixelsVerConsumed = (int)(pictoSize*pictoRatio);
        					int pxPerTab = 450 / 8; // Very rough estimation.
        					int pxPerSpace = 10;
        					int numPages = 1;
        					boolean justReadNewLine = false;
        					// If two or more \n are present in the text, the first one consumes elem.getHeight() pixels, but the second,
        					// third... consume only about 15 pixels.
        					int pxPerNewLine = 15;    
        					
        					System.out.println("TamaÃ±o pico="+pictoSize);
        					int numElements=G.elementList.size();
        					for (int i=0; i<numElements; i++) {
        				 		AWElement elem=G.elementList.get(i);
        	//					System.out.println("Nuevo elemento: "+numPages+" "+numPixelsVerConsumed+" "+numPixelsHorConsumed);
        						if (elem.getType()==0) { // Pictogram
        							justReadNewLine = false;
        							numPixelsHorConsumed += pictoSize;
        							picLine++;
        							if ((numPixelsHorConsumed>numPixelsHor) || (picLine>G.numPictosLinePDF)) {
        								numPixelsVerConsumed += pictoSize*pictoRatio;
        								numPixelsHorConsumed = pictoSize;
        								picLine=1;
        							}
        							if (numPixelsVerConsumed>numPixelsVer) {
        								numPixelsVerConsumed = pictoSize;
        								numPixelsHorConsumed = pictoSize;
        								picLine=1;
        								Graphics2D g2 = cb.createGraphics(PageSize.A4.getWidth(), PageSize.A4.getHeight(), true, 0.95f);
        								
        								JDialog dlg = new JDialog();
        								JScrollPane scrollPane = new JScrollPane(tempTP);
        								dlg.getContentPane().add(scrollPane, BorderLayout.CENTER);
        								dlg.pack();
        								
        								tempTP.print(g2);
        								g2.dispose();
        								document.close();
        								
        								numPages++;
        								document = new com.itextpdf.text.Document(PageSize.A4);
        								writer = PdfWriter.getInstance(document, new FileOutputStream(file.getCanonicalPath()+"Pag"+numPages+".pdf"));
        								
        	        					writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
        	        					writer.setPageEvent(event);
        								document.open();
        								cb = writer.getDirectContent();
        								tempTP.setText("");
        								tempTP.setStyledDocument(new DefaultStyledDocument());
        							}
        							//tempTP.insertComponent((AWElement)TextUtils.copy(elem));
        							//create new element with different size
        			            	AWElement newElem = null;
        			            	newElem = AWElement.createPDFPrevPictogramElement(i,elem.getTextField().getText(),elem.getNumPictogram(),elem.isVisibleImage(),elem.isVisibleBorder(),elem.isSearchImageOnBD(),elem.getWordSearchBD(), false);
        			            	tempTP.insertComponent(newElem);
        						}
        						else { // Separator
        	                    	if (elem.getSeparator().equals("\n")) {
        	                    		if (justReadNewLine) numPixelsVerConsumed += pxPerNewLine;
        	                    		else numPixelsVerConsumed += G.imagesSize*pictoRatio;
        	                    		justReadNewLine = true;
        								numPixelsHorConsumed = 0; picLine=0;
        								if (numPixelsVerConsumed>numPixelsVer) {
        									numPixelsVerConsumed = (int)(G.imagesSize*pictoRatio);
        									Graphics2D g2 = cb.createGraphics(PageSize.A4.getWidth(), PageSize.A4.getHeight(), true, 0.95f);
        									
        									JDialog dlg = new JDialog();
        									JScrollPane scrollPane = new JScrollPane(tempTP);
        									dlg.getContentPane().add(scrollPane, BorderLayout.CENTER);
        									dlg.pack();
        									
        									tempTP.print(g2);
        									g2.dispose();
        									document.close();
        									
        									numPages++;
        									document = new com.itextpdf.text.Document(PageSize.A4);
        									writer = PdfWriter.getInstance(document, new FileOutputStream(file.getCanonicalPath()+"Pag"+numPages+".pdf"));
        									
        		        					writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
        		        					writer.setPageEvent(event);
        									document.open();
        									cb = writer.getDirectContent();
        									tempTP.setText("");
        									tempTP.setStyledDocument(new DefaultStyledDocument());
        								}
        	                    	}  
        	                        else if (elem.getSeparator().equals("\t")) {
        	                        	numPixelsHorConsumed += pxPerTab;
        	                        	if (numPixelsHorConsumed>numPixelsHor) {
        									numPixelsVerConsumed += G.imagesSize*pictoRatio;
        									numPixelsHorConsumed += pxPerTab;
        								}
        								if (numPixelsVerConsumed>numPixelsVer) {
        									numPixelsVerConsumed = (int)(G.imagesSize*pictoRatio);
        									picLine=0;
        									Graphics2D g2 = cb.createGraphics(PageSize.A4.getWidth(), PageSize.A4.getHeight(), true, 0.95f);
        									
        									JDialog dlg = new JDialog();
        									JScrollPane scrollPane = new JScrollPane(tempTP);
        									dlg.getContentPane().add(scrollPane, BorderLayout.CENTER);
        									dlg.pack();
        									
        									tempTP.print(g2);
        									g2.dispose();
        									document.close();
        									
        									numPages++;
        									document = new com.itextpdf.text.Document(PageSize.A4);
        									writer = PdfWriter.getInstance(document, new FileOutputStream(file.getCanonicalPath()+"Pag"+numPages+".pdf"));
        									
        		        					writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
        		        					writer.setPageEvent(event);
        									document.open();
        									cb = writer.getDirectContent();
        									tempTP.setText("");
        									tempTP.setStyledDocument(new DefaultStyledDocument());
        								}
        	                        }
        	                        else if (elem.getSeparator().equals(G.separatorSpace)) {
        	                        	numPixelsHorConsumed+=pxPerSpace;
        	                        	if (numPixelsHorConsumed>numPixelsHor) {
        	                        		numPixelsVerConsumed += G.imagesSize*pictoRatio;
        									numPixelsHorConsumed = pxPerSpace;
        									picLine=0;
        	                        	}
        	                        	if (numPixelsVerConsumed>numPixelsVer) {
        	                        		numPixelsVerConsumed = (int)(G.imagesSize*pictoRatio);
        									Graphics2D g2 = cb.createGraphics(PageSize.A4.getWidth(), PageSize.A4.getHeight(), true, 0.95f);
        									picLine=0;
        									JDialog dlg = new JDialog();
        									JScrollPane scrollPane = new JScrollPane(tempTP);
        									dlg.getContentPane().add(scrollPane, BorderLayout.CENTER);
        									dlg.pack();
        									
        									tempTP.print(g2);
        									g2.dispose();
        									document.close();
        									
        									numPages++;
        									document = new com.itextpdf.text.Document(PageSize.A4);
        									writer = PdfWriter.getInstance(document, new FileOutputStream(file.getCanonicalPath()+"Pag"+numPages+".pdf"));
        									
        		        					writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
        		        					writer.setPageEvent(event);
        									document.open();
        									cb = writer.getDirectContent();
        									tempTP.setText("");
        									tempTP.setStyledDocument(new DefaultStyledDocument());
        	                        	}
        	                        }
        	                        else System.out.println("Wrong element saving PDF");
        	                    	tempTP.getStyledDocument().insertString(tempTP.getStyledDocument().getLength(), elem.getSeparator(), null);	
        	                    }
        					}
        					Graphics2D g2 = cb.createGraphics(PageSize.A4.getWidth(), PageSize.A4.getHeight());
        					JDialog dlg = new JDialog();
        					JScrollPane scrollPane = new JScrollPane(tempTP);
        					dlg.getContentPane().add(scrollPane, BorderLayout.CENTER);
        					
        					dlg.pack();
        					tempTP.print(g2);
        					g2.dispose();
        					
        					
        					document.close();		
        					// Merge PDF files
        					try {
        					      List<InputStream> pdfs = new ArrayList<InputStream>();
        					      for (int i=1;i<=numPages;i++) {
        					    	  pdfs.add(new FileInputStream(file.getCanonicalPath()+"Pag"+i+".pdf"));
        					      }
        					      OutputStream output = new FileOutputStream(file.getCanonicalPath()+".pdf");
        					      TextUtils.concatPDFs(pdfs, output, true);
        					      for (int i=1;i<=numPages;i++) {
        					    	  pdfs.get(i-1).close();
        					    	  File f = new File(file.getCanonicalPath()+"Pag"+i+".pdf");
        					    	  f.delete();
        					      }
        				    } catch (Exception e) {
        				        e.printStackTrace();
        				    }
        	            }
        	            
        	            else {
        	            	System.out.println("fileExport problem: wrong filter selected");
        	            }
        	        }
        		}
        	    catch (Exception exc) {System.out.println(exc);}
            }
        }
        catch (Exception exc) {System.out.println(exc);}
	}
	
	public static void editUndo() {
		if (G.undoList.size()==0) {
    		JOptionPane.showMessageDialog(null,TLanguage.getString("EDIT_MENU_UNDO_UNABLE_MORE_UNDO"),
    				TLanguage.getString("WARNING"),JOptionPane.INFORMATION_MESSAGE);
        	return;
    	}
    	G.redoList.add(0,TextUtils.ElementList2String(G.elementList));
    	if (G.redoList.size()>(2*G.maxUndoLevel)) {
    		// Basic cleanup of redoElementList. Sets size to G.maxUndoLevels again, deleting old junk values.
    		int aux = G.redoList.size();
    		for (int i=G.maxUndoLevel;i<aux;i++) G.redoList.remove(G.maxUndoLevel);
    		G.redoList.trimToSize();
    	}
    	G.elementList = TextUtils.String2ElementList(G.undoList.get(0));
    	G.undoList.remove(0);
    	TextUtils.regenerateTextZone();
    	G.elementList.get(0).getTextField().requestFocusInWindow();
    }
    
    public static void editRedo() {
    	if (G.redoList.size()==0) {
    		JOptionPane.showMessageDialog(null,TLanguage.getString("EDIT_MENU_REDO_UNABLE_MORE_REDO"),
    				TLanguage.getString("WARNING"),JOptionPane.INFORMATION_MESSAGE);
        	return;
    	}
    	G.undoList.add(0,TextUtils.ElementList2String(G.elementList));
    	G.elementList = TextUtils.String2ElementList(G.redoList.get(0));
    	G.redoList.remove(0);
    	TextUtils.regenerateTextZone();
    	G.elementList.get(0).getTextField().requestFocusInWindow();
    }
	
	public static void editCut() {
		if (G.selectionState==2) {
			// Don't allow cut of the whole text (unable to paste it again somewhere,
			// quite problematic).
			boolean fullSelection = true;
			for (int i=0;i<G.indexSelectionFrom;i++) {
				if (G.elementList.get(i).getType()==0) {
					fullSelection = false;
					break;
				}
			}
			if (fullSelection) {
				for (int i=G.elementList.size()-1;i>G.indexSelectionTo;i--) {
					if (G.elementList.get(i).getType()==0) {
						fullSelection = false;
						break;
					}
				}
			}
			if (fullSelection) {
	            JOptionPane.showMessageDialog(null,TLanguage.getString("EDIT_MENU_CUT_UNABLE_CUT_ALL"),
	            		TLanguage.getString("WARNING"),JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				String str = "";
				TextUtils.updateUndo();
				G.selectionState = 0;
				ArrayList<AWElement> eL = new ArrayList<AWElement>();			
				for (int i=G.indexSelectionFrom; i<=G.indexSelectionTo; i++) { // Add to temp list.
					eL.add(G.elementList.get(i));
				}
				// Copy to system clipboard.
				str = TextUtils.ElementList2String(eL);
				StringSelection ss = new StringSelection(str);
			    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
				if (G.indexSelectionTo==G.elementList.size()-1) { // Until last element.
					for (int i=G.indexSelectionFrom; i<=G.indexSelectionTo; i++) { // Remove from main list.
						G.elementList.remove(G.indexSelectionFrom);
					}
				}
				else {
					for (int i=G.indexSelectionFrom; i<=G.indexSelectionTo+1; i++) { // Remove from main list, also next separator.
						G.elementList.remove(G.indexSelectionFrom);
					}
				}
				for (int count=G.indexSelectionFrom;count<G.elementList.size();count++) { // Adjust positions.
                    AWElement tmpE = G.elementList.get(count);
                    tmpE.setPosition(count);
                }
				TextUtils.regenerateTextZone();
				if (!(G.activeElement.getTextField().requestFocusInWindow())) {
					// If we cut "active element", transfer focus to first element visible in the textZone.
					Component[] myComps = G.textZone.getComponents();
					Component realComp = ((Container)myComps[0]).getComponent(0);
					((AWElement)realComp).getTextField().requestFocusInWindow();
				}
			}
		}
    }
    
    public static void editCopy() {
    	if (G.selectionState==2) {
    		for (int i=G.indexSelectionFrom; i<=G.indexSelectionTo; i++) {
				G.elementList.get(i).setBackground(Color.WHITE);
			}
    		String str = "";
			TextUtils.updateUndo();
			G.selectionState = 0;
			ArrayList<AWElement> eL = new ArrayList<AWElement>();				
			for (int i=G.indexSelectionFrom; i<=G.indexSelectionTo; i++) { // Add to temp list.
				eL.add(G.elementList.get(i));
			}
			// Copy to system clipboard.
			str = TextUtils.ElementList2String(eL);
			StringSelection ss = new StringSelection(str);
		    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
    	}
    }
    
    public static void editPaste(int pastePos) {
		G.selectionState = 0;
		Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
	    try {
	        if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
	            String text = (String)t.getTransferData(DataFlavor.stringFlavor);
	            ArrayList<AWElement> aux = TextUtils.String2ElementList(text);
//	            if (pastePos==1) {
//	            	// From first element, don't add previous separator.
//	            	for (int i=0; i<aux.size(); i++) { // Add elements.
//						G.elementList.add(pastePos+i,aux.get(i));
//					}
//	            }
//	            else {
		            G.elementList.add(pastePos,AWElement.createSeparatorElement(pastePos, G.separatorSpace));
		    		for (int i=0; i<aux.size(); i++) { // Add elements.
						G.elementList.add(pastePos+i+1,aux.get(i));
					}
//	            }
	    		for (int count=pastePos;count<G.elementList.size();count++) { // Adjust positions.
	                AWElement tmpE = G.elementList.get(count);
	                tmpE.setPosition(count);
	            }
	    		TextUtils.regenerateTextZone();
	    		G.activeElement.getTextField().requestFocusInWindow();
	        }
	    }
	    catch (Exception exc) { System.out.println(exc); }
    }
    
    
	   
    public static void pictogramNextImage() {
    	// Quite similar to DBManagement.searchOnBD()
    	try {
    		if (!(G.activeComponent instanceof AWTextField)) return;
    		
	    	int numPictogram = G.activeElement.getNumPictogram();
    		String str = G.activeElement.getTextField().getText().toLowerCase(); // Case insensitive.
    		
    		// Now, we also filter puntuaction marks.
    		if ( (str.equals("¡")) || (str.equals("¿")) || (str.equals("!")) || (str.equals("?")) ) {}
    		else {
    			if (str.endsWith("...")) {
    				str = str.substring(0,str.length()-3);
    			}
    			else if ( (str.endsWith(",")) || (str.endsWith(";")) || (str.endsWith(":")) || (str.endsWith(".")) ) {
    				str = str.substring(0,str.length()-1);
    			}
    		}

    		ArrayList<String> paths = new ArrayList<String>();
	        //Statement stat = G.conn.createStatement();   
	        
	        ResultSet rs = DB.getInstance().query("select * from ArawordView where word=\""+str+"\"");
	        while (rs.next()) {
	            String name = rs.getString("name");
	            paths.add(name);
	        }
	        if (G.documentLanguage.equals("Castellano")) {
	        	// If it's a form of some verb, return the verb.
	        	Statement statAux = G.connVerbsDB.createStatement();
	        	ResultSet rsAux = statAux.executeQuery("select * from verbs where form=\""+str+"\"");
		        if (rsAux.next()) {
		        	String verb = rsAux.getString("verb");
		        	rs = DB.getInstance().query("select * from ArawordView where word=\""+verb+"\"");
		        	while (rs.next()) {
		        		String name = rs.getString("name");
		        		paths.add(name);
		        	}
		        }
	        }
	        if (paths.isEmpty()) {
	        	//No images found
	        }
	        else {
	        	G.activeElement.setNumPictogram((numPictogram+1) % paths.size()); // Mod operation, spinning wheel.
    			G.activeElement.regeneratePictogram();
    			G.lastPictogramWordAssociation.put(G.activeElement.getTextField().getText().toLowerCase(),(Integer)G.activeElement.getNumPictogram());
	        }
    	}
        catch (Exception exc) {System.out.println(exc);}
    }
    
//    public static void databaseImportDB(JProgressBar jpb) {
//    	if (JOptionPane.showConfirmDialog(null,TLanguage.getString("DATABASE_MENU_IMPORT_BD_WARNING"),
//                TLanguage.getString("WARNING"),JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
//    		G.pictogramsPath = G.tempPictogramsPath;
//     	   	DBManagement.connectDB();
//	    	DBManagement.importDB(jpb);
//	    	DBManagement.createAraWordView(G.documentLanguage);
//	    	// Text already written gets its pictograms.
//	    	TextUtils.regenerateDocument();
//	    	TSetup.save(); // Save DBPath, Pictograms path, etc...
//	    	JOptionPane.showMessageDialog(null, TLanguage.getString("DATABASE_MENU_IMPORT_BD_SUCCESS"));
//    	}
//    }
    
    public static void pictogramCompoundSplit() {
    	if (!(G.activeComponent instanceof AWTextField)) return;
    	if (!G.activeElement.isCompound()) {
        	TextUtils.updateUndo();
            int tmp = G.maxLengthCompoundWords;
            G.maxLengthCompoundWords = 6; // If it's manual, search for long words.
            if (TextUtils.checkCompoundWord()) TextUtils.regenerateTextZone();
            G.maxLengthCompoundWords = tmp;
        }
    	else {
    		TextUtils.updateUndo();
            TextUtils.splitWord();
    	}
    }
    
    public static void pictogramHideImageActiveElement() {
    	if (!(G.activeComponent instanceof AWTextField)) return;
    	if (G.activeElement.isVisibleImage()) {
        	G.activeElement.setVisibleImage(false);
        	G.activeElement.regeneratePictogram();
        }
    }
    
    public static void pictogramHideImageAllElements() {
    	if (!(G.activeComponent instanceof AWTextField)) return;
    	for (AWElement elem: G.elementList) {
            if (elem.getType()==0) { // Pictogram
            	if (elem.isVisibleImage()) {
            		elem.setVisibleImage(false);
            		elem.regeneratePictogram();
                }
            }
        }
    }
    
    public static void pictogramHideBorderActiveElement() {
    	if (!(G.activeComponent instanceof AWTextField)) return;
    	if (G.activeElement.isVisibleBorder()) {
        	G.activeElement.setVisibleBorder(false);
        	G.activeElement.regeneratePictogram();
        }
    }
    
    public static void pictogramHideBorderAllElements() {
    	if (!(G.activeComponent instanceof AWTextField)) return;
    	for (AWElement elem: G.elementList) {
            if (elem.getType()==0) { // Pictogram
            	if (elem.isVisibleBorder()) {
            		elem.setVisibleBorder(false);
            		elem.regeneratePictogram();
                }
            }
        }
    }
    
    public static void pictogramShowImageActiveElement() {
    	if (!(G.activeComponent instanceof AWTextField)) return;
    	if (!G.activeElement.isVisibleImage()) {
        	G.activeElement.setVisibleImage(true);
        	G.activeElement.regeneratePictogram();
        }
    }
    
    public static void pictogramShowImageAllElements() {
    	if (!(G.activeComponent instanceof AWTextField)) return;
    	for (AWElement elem: G.elementList) {
            if (elem.getType()==0) { // Pictogram
            	if (!elem.isVisibleImage()) {
            		elem.setVisibleImage(true);
            		elem.regeneratePictogram();
                }
            }
        }
    }
    
    public static void pictogramShowBorderActiveElement() {
    	if (!(G.activeComponent instanceof AWTextField)) return;
    	if (!G.activeElement.isVisibleBorder()) {
        	G.activeElement.setVisibleBorder(true);
        	G.activeElement.regeneratePictogram();
        }
    }
    
    public static void pictogramShowBorderAllElements() {
    	if (!(G.activeComponent instanceof AWTextField)) return;
    	for (AWElement elem: G.elementList) {
            if (elem.getType()==0) { // Pictogram
            	if (!elem.isVisibleBorder()) {
            		elem.setVisibleBorder(true);
            		elem.regeneratePictogram();
                }
            }
        }
    }
    
    public static void pictogramToUpperCaseActiveElement() {
    	if (!(G.activeComponent instanceof AWTextField)) return;
        G.activeElement.getTextField().setText(G.activeElement.getTextField().getText().toUpperCase());
        G.activeElement.regeneratePictogram(); // Not really needed, added security.
    }
    
    public static void pictogramToUpperCaseAllElements() {
    	if (!(G.activeComponent instanceof AWTextField)) return;
    	for (AWElement elem: G.elementList) {
            if (elem.getType()==0) { // Pictogram
            	elem.getTextField().setText(elem.getTextField().getText().toUpperCase());
                elem.regeneratePictogram(); // Not really needed, added security.
            }
        }
    }
    
    public static void pictogramToLowerCaseActiveElement() {
    	if (!(G.activeComponent instanceof AWTextField)) return;
        G.activeElement.getTextField().setText(G.activeElement.getTextField().getText().toLowerCase());
        G.activeElement.regeneratePictogram(); // Not really needed, added security.
    }
    
    public static void pictogramToLowerCaseAllElements() {
    	if (!(G.activeComponent instanceof AWTextField)) return;
    	for (AWElement elem: G.elementList) {
            if (elem.getType()==0) { // Pictogram
            	elem.getTextField().setText(elem.getTextField().getText().toLowerCase());
                elem.regeneratePictogram(); // Not really needed, added security.
            }
        }
    }
    
    public static void pictogramChangeName() {
    	if (!(G.activeComponent instanceof AWTextField)) return;
    	if (G.activeElement.isSearchImageOnBD()) {
    		G.activeElement.setSearchImageOnBD(false);
    		G.activeElement.setWordSearchBD(G.activeElement.getTextField().getText());
    	}
    }
    
    public static void pictogramInsertImage() {
    	mainFrame f = new mainFrame(G.applicationLanguage);
        f.setVisible(true);
        f.pack();
    }
    
    public static void VoiceSintesys() {
    	    	
    	araword.utils.TInterpreterGoogleTTS ttsg = new araword.utils.TInterpreterGoogleTTS();
        ttsg.setCurrentVoiceAndText(araword.utils.TextUtils.getText());          
        ttsg.run();
    }
    
    
    
    //ZIP vars and methods
    private final static int ZIP_BUFFER_SIZE = 2048;

	private static String tempDirectoryPath;

	private static File tempDirectory;

	static {
		tempDirectory = new File(TFileHandler.getCurrentDirectory(), "temp");
		tempDirectoryPath = tempDirectory.getAbsolutePath();
		tempDirectory.mkdirs();
	}

	/**
	 * Returns the current temporal directory.
	 * 
	 * @return The current temporal directory
	 */
	public static File getTempDirectory() {
		return tempDirectory;
	}
	
	/**
	 * Returns the current temporal directory path.
	 * 
	 * @return The current temporal directory path
	 */
	public static String getTempDirectoryPath() {
		return tempDirectoryPath;
	}

	/**
	 * Deletes the current temporal directory and all its contents.
	 */
	public static void deleteTempDirectory() {
		TFileHandler.deleteDirectory(tempDirectory);
		//tempDirectory.delete();
	}

	/**
	 * Deletes all the current temporal directory contents.
	 */
	public static void cleanTempDirectory() {
		TFileHandler.deleteDirectory(tempDirectory);
	}
	
	// Zips a directory to a file
		private static void zip(File srcDir, File dstFile) throws IOException {
			System.out.println("operacion de zipear");
			System.out.println("en "+ dstFile.getAbsolutePath());
			//borrar el anterior
			//TFileHandler.remove(dstFile.getAbsolutePath());
			// Create a zip output stream to zip the data to
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(dstFile));
			// Zip all the contents of the directory into the zip output stream
			zip(srcDir, srcDir, zos);
			// Close the zip output stream
			zos.close();
		}
	
	// Zips a directory to a ZipOutputStream
		private static void zip(File srcDir, File originSrcDir,
				ZipOutputStream dstStream) throws IOException {
			
			// Create buffer
			byte[] buffer = new byte[ZIP_BUFFER_SIZE];
			int bytes = 0;
			// Get a listing of the directory content
			String[] dirList = srcDir.list();
			// Zip each file of the directory listing
			for (int i = 0; i < dirList.length; i++) {
				File file = new File(srcDir, dirList[i]);
				// If the file is a directory, call this
				// function again to add its content recursively
				if (file.isDirectory()) {
					zip(file, originSrcDir, dstStream);
					continue;
				}
				// If the file is not a directory
				// Create a FileInputStream on top of the file
				FileInputStream fis = new FileInputStream(file);
				// Create a new zip entry
				ZipEntry entry = new ZipEntry(TFileHandler.removeDirectoryPath(
						originSrcDir.getAbsolutePath(), file.getAbsolutePath()));
				// Place the zip entry in the ZipOutputStream object
				dstStream.putNextEntry(entry);
				// Write the content of the file to the ZipOutputStream
				while ((bytes = fis.read(buffer)) != -1)
					dstStream.write(buffer, 0, bytes);
				// Close the FileInputStream
				fis.close();
			}
		}

		// Unzips a file to a directory
		private static void unzip(File srcFile, File dstDir) throws IOException {
			// Create buffer
			byte[] buffer = new byte[ZIP_BUFFER_SIZE];
			int bytes;
			// Get zip file entries
			ZipFile zipFile = new ZipFile(srcFile);
			Enumeration entries = zipFile.entries();
			// For each entry
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry)entries.nextElement();
				// If the entry is a directory, create it
				if (entry.isDirectory()) {
					File newDirectory = new File(entry.getName());
					if (!newDirectory.exists())
						newDirectory.mkdirs();
					continue;
				}
				// If the entry is a file, get it
				File newFile = new File(dstDir, entry.getName().replace('\\', '/'));
				// Check if its directory exists and create it if necessary
				File newFileDir = newFile.getParentFile();
				if (!newFileDir.exists())
					newFileDir.mkdirs();
				// Create the input and output streams
				InputStream in = zipFile.getInputStream(entry);
				OutputStream out = new BufferedOutputStream(new FileOutputStream(
						newFile));
				// Copy one into the other
				while ((bytes = in.read(buffer)) >= 0)
					out.write(buffer, 0, bytes);
				// Close input and output streams
				in.close();
				out.close();
			}
			// Close the zip file
			zipFile.close();
		}
   
    
}

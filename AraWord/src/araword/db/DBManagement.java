package araword.db;

import araword.G;
import araword.configuration.TLanguage;
import database.DB;
import database.ImageManager;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.channels.FileChannel;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.List;

public class DBManagement {

    public static void connectDB(){
        /*try {
            //Create an object to connect to the data base
            Class.forName("org.sqlite.JDBC");
            String fileName = G.pictogramsPath+File.separator+G.databaseName;
            File dataBase = new File(fileName);
            if (!dataBase.exists()){
                G.conn = DriverManager.getConnection("jdbc:sqlite:"+fileName);
                createTablesDB();
            }
                G.conn = DriverManager.getConnection("jdbc:sqlite:"+fileName);      
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }*/
    }
    
    public static void connectVerbsDB(){
        String verbs_data_base_name;
        String verbs_txt_file_name;
        
        try {
            //Create an object to connect to the data base
            verbs_data_base_name = new String(G.documentLanguage+"_verbs") ;
            
                            //verb database name will be like "Castellano_verbs.db", "Ingles_verbs.db", etc.
            verbs_txt_file_name = new String(G.documentLanguage+"_verbs.txt");
            //the file name will be like "Castellano_verbs.txt", "Ingles_verbs.txt", etc.
            //the file must contain all the verbs conjugated, one verb per line, different forms separated by ",", like
            //to warn,warn,warned,warned,i warn,you warn, etc., etc.

            //In the general case, there will not be a set of conjugated verb forms for all the languages
            //Let's look if such exists, and then identify conjugated verb forms. If not, just the infinitive could be
            //used in order to look for the pictogram.
            //For instance: if "Ingles_verbs.db" exists, when writting "I was" Araword will show the "to be" pictogram. If not
            //the "I" and "was" pictos will be shown
            
            if (verbs_data_base_name.length() > 0) {  //a language with conjugated formal verbs
                
                Class.forName("org.sqlite.JDBC");
                String fileName = "resources"+File.separator+verbs_data_base_name+".db";
                File dataBase = new File(fileName);
                File conjugated_verbs = new File("resources"+File.separator+verbs_txt_file_name);
                
                if (dataBase.exists()) {
                	G.connVerbsDB = DriverManager.getConnection("jdbc:sqlite:"+fileName);
                } else if (conjugated_verbs.exists()) {
                	//the database does not exist, but can be created
                    
                    G.connVerbsDB = DriverManager.getConnection("jdbc:sqlite:"+fileName);
                                        
                    createTablesVerbsDB(verbs_txt_file_name);
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }
    }
    
    /**
     * Close the connection to the database.
     */ 
    public static void closeDB(){           
        try {
            // Closing the connection to the data base
            DB.getInstance().close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }           
    }
    
    public static void createAraWordView (String language) {
        // If language = "(todos // all)", view of all languages.
        try {
            String query = "DROP VIEW IF EXISTS ArawordView";
            DB.getInstance().update(query);
            query = "";
            if (language.equals("(todos // all)"))
                query = "CREATE VIEW IF NOT EXISTS ArawordView AS SELECT M.word word, T.name type, M.name name "+
                "FROM main M, type T"+
                "WHERE M.idT = T.id " +
                "ORDER BY word";
            else
                query = "CREATE VIEW IF NOT EXISTS ArawordView AS SELECT M.word word, T.name type, M.name name "+
                "FROM main M, type T, language L "+
                "WHERE M.idT = T.id AND M.idL = L.id AND L.name =\"" + language + "\" " +
                "ORDER BY word";
            DB.getInstance().update(query);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }           
    }
    
    public static DBResult searchOnDB(String word, int numPictogram) {
        class tempClass { // Useful for searchOnDB
            public String name;
            public String type;
            public String infinitive;
        }
                
        DBResult dbr = new DBResult();
        dbr.setImage(G.notFound);
        dbr.setBorder(G.borders.get("NO_BORDER"));
        dbr.setFileImage("");
        try {
 
            if (word.equals("")) { // Speed up frequent case.
                return dbr; 
            }
            String str = word.toLowerCase(); // Case insensitive.
            // Now, we also filter quotation marks.
            if ( (str.equals("¡")) || (str.equals("¿")) || (str.equals("!")) || (str.equals("?")) ) {}
            else {
                if (str.endsWith("...")) {
                    str = str.substring(0,str.length()-3);
                }
                else if ( (str.endsWith(",")) || (str.endsWith(";")) || (str.endsWith(":")) || (str.endsWith(".")) ) {
                    str = str.substring(0,str.length()-1);
                }
            }

            ArrayList<tempClass> paths = new ArrayList<tempClass>();
            String query = "select * from ArawordView where word='"+str.replace("'", "''")+"'";
            ResultSet rs = DB.getInstance().query(query);
            while (rs.next()) {
                String name = rs.getString("name");
                String type = rs.getString("type");
                String inf=null;
                tempClass t = new tempClass();
                t.name = name;
                t.type = type;
                t.infinitive=inf;
                paths.add(t);
            }
            
            // *********VERBO********

			String verbs_data_base_name = new String(G.documentLanguage
					+ "_verbs");
			File dataBase = new File("resources" + File.separator
					+ verbs_data_base_name + ".db");

			if (dataBase.exists()) {
                //If it's a form of some verb, return the verb.
                Statement statAux = G.connVerbsDB.createStatement();
                statAux.setEscapeProcessing(true);
                ResultSet rsAux = statAux.executeQuery("select * from verbs where form='"+str.replace("'", "''")+"'");
                
                while (rsAux.next()) {
                    String verb = rsAux.getString("verb");
                    System.out.println("VERB="+ verb);
                    rs = DB.getInstance().query("select * from ArawordView where word='"+verb.replace("'", "''")+"'");
                 
                    while (rs.next()) {
                        //System.out.println("uno6");
                        String name = rs.getString("name");
                        System.out.println("name="+ name);
                        String type = rs.getString("type");
                        tempClass t = new tempClass();
                        t.name = name;
                        t.type = type;
                        t.infinitive=verb;
                        paths.add(t);
                        // el problema esta con palabras como casa
                        //es un nombre y un verbo a la vez.
                        // a la lista path se añaden todos
                        //hay que comprobar segun numpicto cual es el que se ha seleccionado 
                        //y comprobar si el seleccionado es verbo.
                    }
                }   
            }
                        

            if (paths.isEmpty()) {
                //No images found
                return dbr;
            }
            else {
                String name = "";
                String type = "";
                String inf = null;
                if (numPictogram<paths.size()) {
                    name = paths.get(numPictogram).name;
                    type = paths.get(numPictogram).type;
                    if (paths.get(numPictogram).infinitive!=null) {
                    	inf=paths.get(numPictogram).infinitive;
                    	System.out.println("***** "+ inf + " *********");
                    }
                }
                else { // First one, for example
                    name = paths.get(0).name;
                    type = paths.get(0).type;
                    if (paths.get(0).infinitive!=null) {
                    	inf=paths.get(0).infinitive;
                    	System.out.println("***** "+ inf + " *********");
                    }
                }

                String fileName = DB.getInstance().getImagesPath() + File.separator + name;
                File file = new File(fileName);
                if (file.exists()) {
                    ImageIcon image2 = new ImageIcon(DB.getInstance().getImagesPath() + File.separator + name);
                    dbr.setImage(new ImageIcon(image2.getImage().getScaledInstance(G.imagesSize,-1,Image.SCALE_SMOOTH)));         
                    
                    dbr.setFileImage(name);
                    dbr.setBorder(G.borders.get(type));
                    //is it a verb?
                    dbr.setInfinitive(inf);                    	
                   
                }
            }
        }
        catch (Exception exc) {System.out.println(exc);}
        return dbr;
    }
    
    
    
   //*********************************************************************
   // search on bbdd for a word and a file name, and 
   // returns it position num on the BBDD
   //*********************************************************************
    public static int searchnumPictoOnDB(String word, String filePictogram) {
       
       
        DBResult dbr = new DBResult();
        int returnNumber=0;
        try {
 
           
            String str = word.toLowerCase(); // Case insensitive.
            // Now, we also filter quotation marks.
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
            String query = "select * from ArawordView where word='"+str.replace("'", "''")+"'";
            ResultSet rs = DB.getInstance().query(query);
            while (rs.next()) {
                String name = rs.getString("name");
                
                paths.add(name);
            }
            
            // *********VERBO********

			String verbs_data_base_name = new String(G.documentLanguage
					+ "_verbs");
			File dataBase = new File("resources" + File.separator
					+ verbs_data_base_name + ".db");

			if (dataBase.exists()) {
                //If it's a form of some verb, return the verb.
				
                Statement statAux = G.connVerbsDB.createStatement();
                
                ResultSet rsAux = statAux.executeQuery("select * from verbs where form='"+str.replace("'", "''")+"'");
                
                while (rsAux.next()) {
                    String verb = rsAux.getString("verb");
                    
                    rs = DB.getInstance().query("select * from ArawordView where word='"+verb.replace("'", "''")+"'");
                 
                    while (rs.next()) {
                       
                        String name = rs.getString("name");
                        
                        paths.add(name);
                        
                    }
                }   
            }
                        

            if (paths.isEmpty()) {
                //No images found
                return 0;
            }
            else {
            	returnNumber=-1;
            	int i=0;
            	do {
            		if (paths.get(i).equals(filePictogram)) returnNumber=i;
            		i++;
            	} while ((returnNumber==-1) && (i<paths.size()));
            	
            	if (returnNumber==-1)
            		return 0;
            	
            } 
            
        }
        catch (Exception exc) {System.out.println(exc);}
        return returnNumber;
    }
    
    
    
    

    public static void createTablesDB() {
        try {                   
            //We create an Statement object that will connect to the data base
            
            //Creating MAIN table
            DB.getInstance().update("DROP TABLE IF EXISTS main;");
            String query = "CREATE TABLE main (word VARCHAR(50), idL INTEGER, idT INTEGER, name VARCHAR(50), nameNN VARCHAR(50))";
            DB.getInstance().update(query);
            
            //Creating LANGUAGE table
            DB.getInstance().update("DROP TABLE IF EXISTS language;");
            query = "CREATE TABLE IF NOT EXISTS language(" +
                    "id INTEGER PRIMARY KEY," +
                    "name VARCHAR(45) NOT NULL)";
            DB.getInstance().update(query);
            
            //Creating TYPE table
            DB.getInstance().update("DROP TABLE IF EXISTS type;");
            query = "CREATE TABLE IF NOT EXISTS type (" +
                    "id INTEGER PRIMARY KEY," +
                    "name VARCHAR(45) NOT NULL)";
            DB.getInstance().update(query);
            
            //Creating index
            DB.getInstance().update("CREATE UNIQUE INDEX main_index ON main (word, idL, idT, name, nameNN)");      
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }
    }
    
    private static void fillTableVerbsDB(final String verbs_txt_file_name) {
        try {
            // Fill with data from verbosConjugados.txt
			new Thread() {
				@Override
				public void run() {
					try {
						G.connVerbsDB.setAutoCommit(false);

						PreparedStatement stmt = G.connVerbsDB
								.prepareStatement("INSERT OR IGNORE INTO "
										+ "verbs" + " (form,verb) VALUES (?,?)");

						int numTransactions = 0;

						FileInputStream fileInputStream = new FileInputStream(
								"resources" + File.separator
										+ verbs_txt_file_name);
						ProgressMonitorInputStream pm = new ProgressMonitorInputStream(
								null,
								TLanguage
										.getString("VERB_DATABASE_CREATION_MESSAGE"),
								fileInputStream);

						// Use free CSV library for easier parsing.
						// CsvReader cvsReader = new CsvReader(new
						// FileInputStream(pm), ',',
						// Charset.forName("UTF-8"));
						BufferedReader br = new BufferedReader(
								new InputStreamReader(pm, "UTF-8"));
						String l;
						while ((l = br.readLine()) != null) {
							// Podemos usar get con el nombre de la cabecera o
							// por
							// posición
							String[] line = l.split(",");

							String verb = line[0];

							// Cambio
							// System.out.println(numTransactions+": "+ verb);

							for (int i = 1; i < line.length; i++) {
								String form = line[i];

								stmt.setString(1, form);
								stmt.setString(2, verb);
								stmt.executeUpdate();
								numTransactions++;
								if (numTransactions > 5000) {
									G.connVerbsDB.commit();
									numTransactions = 0;
								}
							}
						}
						
						G.connVerbsDB.setAutoCommit(true);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					JOptionPane.showMessageDialog(null,
                            TLanguage.getString("VERB_DATABASE_CREATION_MESSAGE_END"),
                            TLanguage.getString("WARNING"),JOptionPane.INFORMATION_MESSAGE);                    
				};
			}.start();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }
    }
    
    public static void createTablesVerbsDB(String verbs_txt_file_name) {
        try {                   
            //We create an Statement object that will connect to the data base
            Statement stmt = G.connVerbsDB.createStatement();
            
            //Creating IMAGE table
            
            stmt.execute("DROP TABLE IF EXISTS " + "verbs" + ";");
            String query = "CREATE TABLE " + "verbs" + " (form VARCHAR(50),verb VARCHAR(50))";
            stmt.execute(query);
            
            //Creating index
            
            stmt.execute("CREATE UNIQUE INDEX verbs_index ON " + "verbs" + " (form, verb)");
            
            fillTableVerbsDB(verbs_txt_file_name);
            
            stmt.close();       
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }
    }
    
    
    
    //**********************************
    
   
    
    
    
    //*********************************
    //management of new awz file format
    
    public static void exportDBAWZ(String output, JProgressBar progressBar,
			String queryLoaded, List<String> customs,
			List<String> customsWords, String docLang) {

		
		try {

			DB conn = DB.getInstance();
			// checkTables();

			HashMap<Integer, String> languageIDs = new HashMap<Integer, String>();
			HashMap<Integer, String> typeIDs = new HashMap<Integer, String>();

			long tiempoInicio = System.currentTimeMillis();

			Element dataBaseXML = new Element("database");
			// Insert languages
			Element languages = new Element("languages");

			Element Elanguage = new Element("language");
			Elanguage.setText(docLang);
			languages.addContent(Elanguage);

			dataBaseXML.addContent(languages);
			ResultSet rs = conn.query("select * from type order by id");
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				typeIDs.put(id, name);
			}
			// Insert images


			// Set the progressbar
			int currentImage = 0;
			progressBar.setMinimum(currentImage);
			progressBar.setMaximum(customs.size());
			

			for (int i = 0; i < customs.size(); i++) {
				String name = customs.get(i);
				String wordBBDD = customsWords.get(i);
				Element image = new Element("image");
				image.setAttribute("id", name);


				ResultSet rs2 = conn
						.query("SELECT id FROM language WHERE name = '"
								+ docLang + "'");

				while (rs2.next()) {

					int idL = rs2.getInt("id");

					Element language = new Element("language");

					language.setAttribute("id", docLang);
					// get picto type
					/*
					 * //System.out.println("select * from main where word = \""
					 * + wordBBDD + "\" and idL = " + idL + " order by idT");
					 */
					ResultSet rs3 = conn
							.query("select * from main where word LIKE \""
									+ wordBBDD.replace("'", "''") + "\" and idL = " + idL
									+ " order by idT");
					int idT = 0;

					if (rs3.next())
						idT = rs3.getInt("idT");
					
					Element wordE = new Element("word");
					wordE.setAttribute("type", typeIDs.get(idT));
					wordE.setText(wordBBDD);
						language.addContent(wordE);
						// Copy image
						String pathSrc = conn.getImagesPath() + File.separator
								+ name;
						String pathDst = output + File.separator + name;
						try {
							// Create channel on the source
							FileChannel srcChannel = new FileInputStream(
									pathSrc).getChannel();
							// Create channel on the destination
							FileChannel dstChannel = new FileOutputStream(
									pathDst).getChannel();
							// Copy file contents from source to destination
							dstChannel.transferFrom(srcChannel, 0,
									srcChannel.size());

							// Close the channels
							srcChannel.close();
							dstChannel.close();
						} catch (IOException exc) {
						// System.out.println(exc.getMessage());
						// System.out.println(exc.toString());
						}

					rs3.close();
					image.addContent(language);
				}
				rs2.close();
				dataBaseXML.addContent(image);
				progressBar.setValue(currentImage++);
			}
			rs.close();
			conn.close();

			// New DB
			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
			FileOutputStream f = new FileOutputStream(output + File.separator
					+ "images.xml");
			out.output(dataBaseXML, f);
			f.flush();
			f.close();

			long totalTiempo = System.currentTimeMillis() - tiempoInicio;
			System.out.println("El tiempo total es :" + totalTiempo / 1000
					+ " segundos");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
    
    public static void exportPictosAWZ(String path, String language,
			List<String> customs, List<String> customsWords, JProgressBar progressBar) {
		
		
		// create folder
		File theDir = new File(path);
		if (!theDir.exists()) {
			System.out.println("creating directory: " + path);
			boolean result = theDir.mkdir();
			if (result) {
				System.out.println("DIR created");
			}
		}

		

		String customSet = "(";
		for (String customName : customs) {
			customSet = customSet + "'" + customName + "' , ";
		}
		customSet = customSet.substring(0, customSet.length() - 2) + ")";
		System.out.println("******** CUSTOM LIST ***********");
		System.out.println(customSet);
		// String query =

		String query = "SELECT COUNT(*) AS row_count, GROUP_CONCAT(main.word) AS terms, main.nameNN AS name FROM main "
				+ "              WHERE main.name IN (SELECT main.name FROM main WHERE name IN "
				+ customSet
				+ " AND idL = (SELECT id FROM language WHERE name LIKE '"
				+ language + "')) GROUP BY main.name";

		
		exportDBAWZ(path, progressBar, query, customs,
				customsWords, language);
	}

	// ****** importDB confirm

	// ***************************************
	// ***************************************
	// **************************************

	// ****** importDB confirm
	public static void importDB2(String input, JProgressBar progressBar,
			boolean doReplace) throws Exception {
		// **********mover a araword

		// Copies the images from the source directory to the directory images
		// and renames them so that there are not characters like ' ' or ''
		String pathSrc = "";
		DB conn = DB.getInstance();
		// checkTables();

		System.out.println("***** Importo nuevos pictos");
		
		

		long tiempoInicio = System.currentTimeMillis();

		String directoryPath = input + File.separator;
		File myDirectory = new File(directoryPath);
		String[] list = myDirectory.list();

		File fileXML = new File(input + File.separator + "images.xml");
		if (fileXML.exists()) {

			SAXBuilder builder = new SAXBuilder(false);
			Document docXML = builder.build(fileXML);
			Element root = docXML.getRootElement();
			List images = root.getChildren("image");
			Iterator j = images.iterator();
			// Insert languages
			List<Element> globalLanguages = root.getChild("languages")
					.getChildren("language");
			Iterator<Element> langsI = globalLanguages.iterator();
			HashMap<String, Integer> languageIDs = new HashMap<String, Integer>();
			HashMap<String, Integer> typeIDs = new HashMap<String, Integer>();
			Element e;
			int i = 0;
			int contTypes = 0;
			int contImages = 0;
			while (langsI.hasNext()) {

				e = langsI.next();
				languageIDs.put(e.getText(), i);

				
				PreparedStatement stmt = conn
						.prepareStatement("INSERT OR IGNORE INTO language (id,name) VALUES (?,?)");
				stmt.setInt(1, i);
				stmt.setString(2, e.getText());
				stmt.executeUpdate();
				stmt.close();
				i++;
			}
			conn.activateTransactions();
			int currentImage = 0;
			
			/*
			progressBar.setMinimum(currentImage);
			progressBar.setMaximum(images.size());
			progressBar.setStringPainted(true);
			progressBar.setValue(currentImage);
			*/
			progressBar.setMaximum(images.size());
			progressBar.setValue(0);
			int pbval=0;
			
			while (j.hasNext()) {
				pbval++;
				progressBar.setValue(pbval);
				
				
				long periodTime = System.currentTimeMillis();

				//progressBar.setValue(currentImage++);
				System.out.println("***TOCO PROGRESSBAR***");
				//progressBar.repaint();
				Element image = (Element) j.next();
				String id = image.getAttributeValue("id");
				List languages = image.getChildren("language");
				Iterator k = languages.iterator();
				pathSrc = directoryPath.concat(id);

				if (!checkImportedIMG(pathSrc, languages)) {
					


					// **************** aqui hay que decidir si se importa o no
					// con una ventana grafica
					boolean acceptImage = araword.gui.GUI.createCustomDialog(pathSrc, languages);
					

					// f.dispose();
					if (acceptImage) {
						// Replace image if needed
						
						if (doReplace
								&& new File(DB.getInstance().getImagesPath()
										+ File.separator + id).exists()) {
							ImageManager.delete(id);
							String query = "DELETE FROM main WHERE nameNN = '"
									+ id + "'";
							conn.update(query);
						}
						
						// Copy image
						String newFileName = ImageManager.add(pathSrc);
						
						id = newFileName.substring(newFileName
								.lastIndexOf(File.separator) + 1);
						while (k.hasNext()) {

							Element languageElement = (Element) k.next();
							String language = languageElement
									.getAttributeValue("id");
							
							List words = languageElement.getChildren("word");
							Iterator l = words.iterator();
							
							while (l.hasNext()) {
								Element wordElement = (Element) l.next();
								// Insert type if needed
								String type = wordElement
										.getAttributeValue("type");
								System.out.println("before if type");
								if (!typeIDs.containsKey(type)) {
									
									typeIDs.put(type, contTypes);
									
									conn = DB.getInstance();
									PreparedStatement stmt = conn
											.prepareStatement("INSERT OR IGNORE INTO type (id,name) VALUES (?,?)");
									
									stmt.setInt(1, contTypes);
									
									stmt.setString(2, type);
									
									stmt.executeUpdate();
									
									stmt.close();

									contTypes++;
								}
								// Insert, at last, image!!
								
								PreparedStatement stmt = conn
										.prepareStatement("INSERT OR IGNORE INTO main (word, idL, idT, name, nameNN) VALUES (?,?,?,?,?)");
								stmt.setString(1, wordElement.getText()
										.toLowerCase());
								stmt.setInt(2, languageIDs.get(language));
								stmt.setInt(3, typeIDs.get(type));
								stmt.setString(4, id);
								stmt.setString(5, id);
								stmt.executeUpdate();
								stmt.close();
								
							}
							// Avoid heap problems
							if (contImages > 5000
									&& ((System.currentTimeMillis() - periodTime) / contImages) > 100) {
								periodTime = System.currentTimeMillis();
								conn.executeQueries();
								contImages = 0;
							} else {
								contImages++;
							}
						}
					}
				} else {
				}
			}
			try {
				conn = DB.getInstance();
				
				conn.executeQueries();
				
				conn.close();
				
			} catch (Exception ee) {
				System.out.println(ee.getMessage());
				System.out.println(ee.toString());
			}
			long totalTiempo = System.currentTimeMillis() - tiempoInicio;
			System.out.println("El tiempo total es :" + totalTiempo
					+ " milisegundos");
		}
	}

	// ***************************************
	// ***************************************
	// **************************************
	private static boolean checkImportedIMG(String imageName, List l) {
		// return true if the picto ready to be imported is already on the bbdd
		// get candidates
		

		boolean result = true; // true is the img has been already imported for
								// all languages

		// recover words

		Iterator k = l.iterator();
		while (k.hasNext()) {
			Element languageElement = (Element) k.next();
			String language = languageElement.getAttributeValue("id");

			List words = languageElement.getChildren("word");
			Iterator w = words.iterator();
			while (w.hasNext()) {
				Element wordElement = (Element) w.next();
				String word = wordElement.getText();
				
					// CHECK image candidates for this word
					String query = "SELECT GROUP_CONCAT(main.word) AS terms, main.nameNN AS name FROM main WHERE main.name IN (SELECT main.name FROM main WHERE word LIKE '"
							+ word
							+ "' AND idL = (SELECT id FROM language WHERE name LIKE '"
							+ language + "')) GROUP BY main.name";
					try {
						DB conn = DB.getInstance();
						ResultSet rs = conn.query(query);
						boolean matched = false;
						while ((rs.next()) && (!matched)) {

							// int id = rs.getInt("id");
							String name = rs.getString("name");
							
							matched = compareIMG(imageName, DB.getInstance()
									.getImagesPath() + File.separator + name);
							// if matched==true es que son la misma imagen y no
							// hay
							// que importar
							
						}
						conn.close();
						if (!matched)
							result = false;
					} catch (Exception e) {
						System.out.println(e.getMessage());
						System.out.println(e.toString());
					}

				

			}

		}

		return result;
	}

	private final static int BUFFSIZE = 1024;
	private static byte buff1[] = new byte[BUFFSIZE];
	private static byte buff2[] = new byte[BUFFSIZE];

	private static boolean compareIMG(String newIMG, String BBDDIMG) {
		
		// compare binary files

		File f1;
		File f2;

		FileInputStream is1 = null;
		FileInputStream is2 = null;

		try {
			f1 = new File(newIMG);
			f2 = new File(BBDDIMG);
			// compara longitudes de ficheros si no coinciden ya puedes return
			// false
			
			if (f1.length() != f2.length())
				return false;

			is1 = new FileInputStream(f1);
			is2 = new FileInputStream(f2);

			int read1 = -1;
			int read2 = -1;

			do {
				int offset1 = 0;
				while (offset1 < BUFFSIZE
						&& (read1 = is1
								.read(buff1, offset1, BUFFSIZE - offset1)) >= 0) {
					offset1 += read1;
				}

				int offset2 = 0;
				while (offset2 < BUFFSIZE
						&& (read2 = is2
								.read(buff2, offset2, BUFFSIZE - offset2)) >= 0) {
					offset2 += read2;
				}
				if (offset1 != offset2)
					return false;
				if (offset1 != BUFFSIZE) {
					Arrays.fill(buff1, offset1, BUFFSIZE, (byte) 0);
					Arrays.fill(buff2, offset2, BUFFSIZE, (byte) 0);
				}
				if (!Arrays.equals(buff1, buff2))
					return false;
			} while (read1 >= 0 && read2 >= 0);
			if (read1 < 0 && read2 < 0)
				return true; // both at EOF
			return false;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			System.out.println(e.toString());
		} finally {
			try {
				if (is1 != null)
					is1.close();
				if (is2 != null)
					is2.close();
			} catch (Exception ei2) {
			}
		}
		return false;
	}

	
    
    
}

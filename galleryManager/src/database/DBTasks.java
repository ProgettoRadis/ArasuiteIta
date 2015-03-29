package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class DBTasks {

    private static void createTablesDB() {
        try {
            DB conn = DB.getInstance();
            // Creating MAIN table
            conn.update("DROP TABLE IF EXISTS main;");
            String query = "CREATE TABLE main (word VARCHAR(50), idL INTEGER, idT INTEGER, name VARCHAR(50), nameNN VARCHAR(50))";
            conn.update(query);

            // Creating LANGUAGE table
            conn.update("DROP TABLE IF EXISTS language;");
            query = "CREATE TABLE IF NOT EXISTS language(" + "id INTEGER PRIMARY KEY," + "name VARCHAR(45) NOT NULL)";
            conn.update(query);

            // Creating TYPE table
            conn.update("DROP TABLE IF EXISTS type;");
            query = "CREATE TABLE IF NOT EXISTS type (" + "id INTEGER PRIMARY KEY," + "name VARCHAR(45) NOT NULL)";
            conn.update(query);

            // Creating index
            conn.update("CREATE UNIQUE INDEX main_index ON main (word, idL, idT, name, nameNN)");

            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }
    }

    public static void addImageDB(String pictogramToAddPath, String language, String type, String word) {
        try {
            DB conn = DB.getInstance();
            int idL = 0, idT = 0;
            // checkTables();

            ResultSet rs = conn.query("select id from language where name=\"" + language + "\"");
            while (rs.next()) {
                idL = rs.getInt("id");
            }
            rs.close();
            rs = conn.query("select id from type where name=\"" + type + "\"");
            while (rs.next()) {
                idT = rs.getInt("id");
            }
            rs.close();

            String id = pictogramToAddPath.substring(pictogramToAddPath.lastIndexOf(File.separator) + 1);
            String idOrig = id;

            // Insert, at last, image!!
            PreparedStatement stmt = conn.prepareStatement("INSERT OR IGNORE INTO main (word, idL, idT, name, nameNN) VALUES (?,?,?,?,?)");
            stmt.setString(1, word.toLowerCase());
            stmt.setInt(2, idL);
            stmt.setInt(3, idT);
            stmt.setString(4, id);
            stmt.setString(5, idOrig);
            stmt.executeUpdate();
            stmt.close();

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void importDB(String input, JProgressBar progressBar,
            boolean doReplace) throws Exception {
        // Copies the images from the source directory to the directory images
        // and renames them so that there are not characters like ' ' or ''
        DB conn = DB.getInstance();
        // checkTables();

        long tiempoInicio = System.currentTimeMillis();

        String directoryPath = input + File.separator;
        File myDirectory = new File(directoryPath);
        String[] list = myDirectory.list();

        File fileXML = new File(input + File.separator + "images.xml");
        if (!fileXML.exists()) {
            JOptionPane.showMessageDialog(null,
                    "No se encuentra el fichero XML", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
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
            progressBar.setMinimum(currentImage);
            progressBar.setMaximum(images.size());
            progressBar.setStringPainted(true);
            progressBar.setValue(currentImage);
            while (j.hasNext()) {
                long periodTime = System.currentTimeMillis();

                progressBar.setValue(currentImage++);
                progressBar.repaint();
                Element image = (Element) j.next();
                String id = image.getAttributeValue("id");
                List languages = image.getChildren("language");
                Iterator k = languages.iterator();
                if (exists(list, id)) {

                    String pathSrc = directoryPath.concat(id);

                    // Replace image if needed
                    if (doReplace
                            && new File(DB.getInstance().getImagesPath()
                                    + File.separator + id).exists()) {
                        ImageManager.delete(id);
                        String query = "DELETE FROM main WHERE nameNN = '" + id
                                + "'";
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
                            String type = wordElement.getAttributeValue("type");
                            if (!typeIDs.containsKey(type)) {
                                typeIDs.put(type, contTypes);
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
            conn.executeQueries();
            conn.close();
            long totalTiempo = System.currentTimeMillis() - tiempoInicio;
            System.out.println("El tiempo total es :" + totalTiempo
                    + " milisegundos");
        }
    }

    public static void exportDB(String output, JProgressBar progressBar) {
        DBTasks.exportDB(output, progressBar, "SELECT DISTINCT name FROM main ORDER BY name");
    }

    public static void exportDB(String output, JProgressBar progressBar, String queryLoaded) {
        try {

            DB conn = DB.getInstance();
            // checkTables();

            HashMap<Integer, String> languageIDs = new HashMap<Integer, String>();
            HashMap<Integer, String> typeIDs = new HashMap<Integer, String>();

            long tiempoInicio = System.currentTimeMillis();

            XMLOutputFactory xof = XMLOutputFactory.newInstance();
            FileOutputStream fileOutputStream = new FileOutputStream(output + File.separator
                    + "images.xml");
            XMLStreamWriter writer = xof.createXMLStreamWriter(fileOutputStream, "UTF-8");

            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("database");

            // Insert languages
            writer.writeStartElement("languages");
            ResultSet rs = conn.query("select * from language order by id");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                languageIDs.put(id, name);
                writer.writeStartElement("language");
                writer.writeCharacters(name);
                writer.writeEndElement();
            }
            writer.writeEndElement();
            rs = conn.query("select * from type order by id");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                typeIDs.put(id, name);
            }
            rs = conn.query(queryLoaded);
            
            // Set the progressbar
            ResultSet rsTotalRows = conn.query("select COUNT(*) AS totalRows FROM ("
                    + queryLoaded + ")");
            int totalRows = rsTotalRows.getInt("totalRows");
            progressBar.setMinimum(0);
            progressBar.setMaximum(totalRows);
            progressBar.setStringPainted(true);
            int currentImage = 0;
            PreparedStatement st = conn
                    .prepareStatement("SELECT GROUP_CONCAT(word, ';') AS words, GROUP_CONCAT(idT, ';') AS types, idL FROM main WHERE name=? GROUP BY idL");
            while (rs.next()) {
                String imageName = rs.getString("name");

                writer.writeStartElement("image");
                writer.writeAttribute("id", imageName);
                writer.writeAttribute("name", imageName);

                st.setString(1, imageName);
                ResultSet imageRs = st.executeQuery();
                while (imageRs.next()) {
                    int langId = imageRs.getInt("idL");
                    writer.writeStartElement("language");
                    writer.writeAttribute("id", languageIDs.get(langId));
                    
                    String[] words = imageRs.getString("words").split(";");
                    String[] types = imageRs.getString("types").split(";");
                    for (int i = 0; i < words.length; i++) {
                        writer.writeStartElement("word");
                        writer.writeAttribute("type", typeIDs.get(Integer.parseInt(types[i])));
                        writer.writeCharacters(words[i]);
                        writer.writeEndElement();
                    }
                    writer.writeEndElement();
                }
                imageRs.close();
                writer.writeEndElement();
                writer.flush();

                // Copy image
                String pathSrc = conn.getImagesPath() + File.separator + imageName;
                String pathDst = output + File.separator + imageName;
                try {
                    // Create channel on the source
                    FileChannel srcChannel = new FileInputStream(pathSrc).getChannel();
                    // Create channel on the destination
                    FileChannel dstChannel = new FileOutputStream(pathDst).getChannel();
                    // Copy file contents from source to destination
                    dstChannel.transferFrom(srcChannel, 0, srcChannel.size());

                    // Close the channels
                    srcChannel.close();
                    dstChannel.close();
                } catch (IOException exc) {
                    System.out.println(exc.getMessage());
                    System.out.println(exc.toString());
                }

                progressBar.setValue(currentImage++);
            }
            rs.close();
            conn.close();

            writer.writeEndDocument();

            // New DB
            writer.flush();
            writer.close();
            fileOutputStream.close();

            long totalTiempo = System.currentTimeMillis() - tiempoInicio;
            System.out.println("El tiempo total es :" + totalTiempo / 1000 + " segundos");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static boolean exists(String list[], String name) {
        boolean exists = false;
        int i = 0;
        while ((i < list.length) && !exists) {
            exists = name.equals(list[i]);
            i++;
        }
        return exists;
    }

}

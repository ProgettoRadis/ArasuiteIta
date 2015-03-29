/*
 * File: TIGDataBase.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Patricia M. Jaray
 * 
 * Date: Jan 18, 2008
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

package tico.imageGallery.dataBase;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import database.DB;

// TODO: Auto-generated Javadoc
/**
 * Database for the images in the gallery.
 * 
 * @author Patricia M. Jaray
 * @version 1.0 Jan 18, 2008
 */

public class TIGDataBase {

    /**
     * The Constant SEARCH_OPTIONS_AND.
     */
    private final static int SEARCH_OPTIONS_AND = 1;

    /**
     * The Constant SEARCH_OPTIONS_OR.
     */
    private final static int SEARCH_OPTIONS_OR = 2;

    // Data base name
    /**
     * The file name.
     */
    private static String fileName = "images" + File.separator + "images.db";
    // Data base connection
    /**
     * The conn.
     */
    private static Connection conn;

    /**
     * Connect to database.
     */
    public static void conectDB() {
        try {
            // Create an object to connect to the data base
            Class.forName("org.sqlite.JDBC");
            File dataBase = new File(fileName);
            if (!dataBase.exists()) {
                File directory = new File("images");
                directory.mkdir();
                conn = DriverManager.getConnection("jdbc:sqlite:" + fileName);
                createTablesDB();
            }
            conn = DriverManager.getConnection("jdbc:sqlite:" + fileName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }
    }

    /**
     * Close the connection to the database.
     */
    public static void closeDB() {
        try {
            // Closing the connection to the data base
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }
    }

    /**
     * Create the tables Image, Concept and Asociated in the database.
     */
    public static void createTablesDB() {
        try {
            // We create an Statement object that will connect to the data base
            Statement stmt = conn.createStatement();

            String query = "CREATE TABLE Concept (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "word char(30) UNIQUE NOT NULL," + "noaccents char(30) NOT NULL)";
            stmt.addBatch(query);

            query = "CREATE TABLE Image (id INTEGER PRIMARY KEY AUTOINCREMENT," + "name char(50) NOT NULL,"
                    + "path char(80) NOT NULL," + "noaccents char(50) NOT NULL)";
            stmt.addBatch(query);

            query = "CREATE TABLE Asociated (concept INTEGER NOT NULL "
                    + "CONSTRAINT fk_concept REFERENCES Concept(id)," + "image INTEGER NOT NULL "
                    + "CONSTRAINT fk_image REFERENCES Image(id)," + "PRIMARY KEY (concept,image))";
            stmt.addBatch(query);
            stmt.executeBatch();

            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }
    }

    /**
     * Insert <code>concept</code> in the table Concept of database.
     * 
     * @param concept
     *            the concept
     * 
     * @return the int
     */
    public static int insertConceptDB(String concept) {
        String conceptQuery;
        String insertConceptQuery;
        int conceptKey = -1;
        try {
            Statement stmt = conn.createStatement();
            conceptQuery = "SELECT * FROM Concept WHERE word = \"" + concept + "\"";
            insertConceptQuery = "INSERT INTO Concept (word,noaccents) VALUES (\"" + concept + "\", \""
                    + order(concept) + "\")";
            try {
                stmt.executeUpdate(insertConceptQuery);
                ResultSet rsIm = stmt.executeQuery("SELECT last_insert_rowid()");
                conceptKey = rsIm.getInt(1);
            } catch (SQLException e) {
                ResultSet rsIm = stmt.executeQuery(conceptQuery);
                conceptKey = rsIm.getInt("id");
            }
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }
        return conceptKey;
    }

    /**
     * Update concept database.
     * 
     * @param oldConcept
     *            the old concept
     * @param newConcept
     *            the new concept
     * 
     * @return the int
     */
    public static int updateConceptDB(String oldConcept, String newConcept) {
        String oldConceptQuery;
        String newConceptQuery;
        String insertConceptQuery;
        String updateConceptQuery;
        int conceptKey = -1;

        try {
            Statement stmt = conn.createStatement();
            oldConceptQuery = "SELECT * FROM Concept WHERE word = \"" + oldConcept + "\"";
            newConceptQuery = "SELECT * FROM Concept WHERE word = \"" + newConcept + "\"";
            ResultSet rsIm = stmt.executeQuery(oldConceptQuery);
            if (!rsIm.next()) {
                rsIm = stmt.executeQuery(newConceptQuery);
                if (!rsIm.next()) {
                    insertConceptQuery = "INSERT INTO Concept (word,noaccents) VALUES (\"" + newConcept + "\", \""
                            + order(newConcept) + "\")";
                    stmt.executeUpdate(insertConceptQuery);
                }
            } else {
                rsIm = stmt.executeQuery(newConceptQuery);
                if (!rsIm.next()) {
                    updateConceptQuery = "UPDATE Concept SET word = \"" + newConcept + "\", noaccents = \""
                            + order(newConcept) + "\" WHERE word = \"" + oldConcept + "\"";
                    stmt.executeUpdate(updateConceptQuery);
                    rsIm = stmt.executeQuery(newConceptQuery);
                }
            }
            conceptKey = rsIm.getInt("id");
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }
        return conceptKey;
    }

    /**
     * Delete all the associations of the image with the id indicate by
     * <code>image</code>.
     * 
     * @param image
     *            the image
     * 
     * @return the int
     */
    public static int deleteAsociatedOfImage(int image) {

        String deleteQuery;
        int result = -1;

        try {
            Statement stmt = conn.createStatement();
            // Hacemos lo mismo con el concepto
            deleteQuery = "DELETE FROM Asociated WHERE image = \"" + image + "\"";
            result = stmt.executeUpdate(deleteQuery);
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }
        return result;
    }

    /**
     * Activate transactions.
     */
    public static void activateTransactions() {
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Execute queries.
     */
    public static void executeQueries() {
        try {
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete image database.
     * 
     * @param key
     *            the key
     * 
     * @return the int
     */
    public static int deleteImageDB(int key) {

        String deleteImageQuery, deleteAsociationQuery;
        int result = -1;

        try {
            Statement stmt = conn.createStatement();

            deleteAsociationQuery = "DELETE FROM Asociated WHERE image = \"" + key + "\"";
            deleteImageQuery = "DELETE FROM Image WHERE id = \"" + key + "\"";

            stmt.executeUpdate(deleteAsociationQuery);
            result = stmt.executeUpdate(deleteImageQuery);
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }
        return result;
    }

    /**
     * Delete concept database.
     * 
     * @param concept
     *            the concept
     * 
     * @return the int
     */
    public static int deleteConceptDB(String concept) {

        String conceptQuery;
        String deleteConceptQuery;
        String deleteAsociationQuery;
        int conceptKey = -1;

        try {

            Statement stmt = conn.createStatement();

            // Hacemos lo mismo con el concepto
            conceptQuery = "SELECT * FROM Concept WHERE word = \"" + concept + "\"";
            ResultSet rsIm = stmt.executeQuery(conceptQuery);
            if (rsIm.next()) {
                conceptKey = rsIm.getInt("id");
                deleteAsociationQuery = "DELETE FROM Asociated WHERE concept = \"" + conceptKey + "\"";
                deleteConceptQuery = "DELETE FROM Concept WHERE word = \"" + concept + "\"";
                stmt.executeUpdate(deleteAsociationQuery);
                stmt.executeUpdate(deleteConceptQuery);
                rsIm = stmt.executeQuery(conceptQuery);
                if (!rsIm.next())
                    conceptKey = 0;
            }
            stmt.close();
            // deleteAsociatedConcept(conceptKey);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }
        return conceptKey;
    }

    /**
     * Insert image database.
     * 
     * @param name
     *            the name
     * @param path
     *            the path
     */
    public static void insertImageDB(String name, String path) {

        String imageQuery;
        String insertImageQuery = "";

        try {

            Statement stmt = conn.createStatement();

            // Hacemos lo mismo con el concepto
            imageQuery = "SELECT id FROM Image WHERE path = \"" + path + "\"";
            ResultSet rsIm = stmt.executeQuery(imageQuery);
            if (!rsIm.next()) {

                insertImageQuery = "INSERT INTO Image (name,path,noaccents) VALUES (\"" + name + "\",\"" + path
                        + "\",\"" + order(name) + "\")";
                stmt.executeUpdate(insertImageQuery);
                rsIm = stmt.executeQuery(imageQuery);
            }
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }
    }

    /**
     * Insert asociated database.
     * 
     * @param concept
     *            the concept
     * @param image
     *            the image
     */
    public static void insertAsociatedDB(int concept, int image) {

        String asociatedQuery;
        String insertAsociatedQuery;

        try {

            Statement stmt = conn.createStatement();

            // Hacemos lo mismo con el concepto
            asociatedQuery = "SELECT * FROM Asociated WHERE concept = \"" + concept + "\" " + "AND image = \"" + image
                    + "\"";
            ResultSet rsIm = stmt.executeQuery(asociatedQuery);
            if (!rsIm.next()) {
                insertAsociatedQuery = "INSERT INTO Asociated (concept,image) VALUES (\"" + concept + "\", \"" + image
                        + "\")";
                stmt.executeUpdate(insertAsociatedQuery);
                rsIm = stmt.executeQuery(asociatedQuery);
            }
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }
    }

    /**
     * Insert database.
     * 
     * @param concepts
     *            the concepts
     * @param imagePath
     *            the image path
     * @param imageName
     *            the image name
     */
    public static void insertDB(Vector concepts, String imagePath, String imageName) {

        String concept;
        int imageKey;
        int conceptKey;

        insertImageDB(imageName, imagePath);
        imageKey = imageKeySearch(imagePath);

        for (int i = 0; i < concepts.size(); i++) {
            concept = (String) concepts.elementAt(i);
            conceptKey = insertConceptDB(concept);
            insertAsociatedDB(conceptKey, imageKey);
        }
    }

    /**
     * Gets the key words.
     * 
     * @return the key words
     */
    public static Vector<String> getKeyWords() {
        Vector<String> data = new Vector<String>();
        try {
            // Creamos un objeto Statement que se conectara a la BD
            Statement stmt = conn.createStatement();
            String query = "SELECT word, noaccents FROM Concept ORDER BY noaccents";
            ResultSet res = stmt.executeQuery(query);
            while (res.next())
                data.add(res.getString("word"));
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }
        return data;
    }

    /**
     * Image name search.
     * 
     * @param path
     *            the path
     * 
     * @return the string
     */

    // Obtiene el nombre de una imagen a partir del path
    public static String imageNameSearch(String path) {
        String result = "";

        try {
            // Creamos un objeto Statement que se conectara a la BD
            Statement stmt = conn.createStatement();
            String query = "SELECT name FROM Image WHERE path == \"" + path + "\"";
            ResultSet res = stmt.executeQuery(query);
            if (res.next()) {
                result = res.getString("name");
            }
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }

        return result;
    }

    /**
     * Number of images.
     * 
     * @return the int
     */
    /*
     * public static int numberOfImages(){
     * 
     * int images = -1;
     * 
     * try{ //Creamos un objeto Statement que se conectara a la BD Statement
     * stmt = conn.createStatement(); String query =
     * "SELECT COUNT(*) FROM Image"; ResultSet res = stmt.executeQuery(query);
     * stmt.close(); images = res.getInt(1); } catch (Exception e){
     * System.out.println(e.getMessage()); System.out.println(e.toString()); }
     * return images; }
     */

    /**
     * Image search.
     * 
     * @param imagen
     *            the imagen
     * 
     * @return the vector
     */
    public static Vector<Vector<String>> imageSearchByName(String imagen) {

        Vector<Vector<String>> data = new Vector<Vector<String>>();

        try {
            // Creates the statement to connect to database
            /*
             * Statement stmt = conn.createStatement(); String query =
             * "SELECT * FROM Image WHERE name LIKE \"" + imagen.replace('*',
             * '%') + "\" " + "OR noaccents LIKE \"" + imagen.replace('*', '%')
             * + "\" " + "ORDER BY noaccents"; ResultSet res =
             * stmt.executeQuery(query); while (res.next()) { Vector<String>
             * result = new Vector<String>(2);
             * result.add(res.getString("path"));
             * result.add(res.getString("name")); data.add(result); }
             * stmt.close();
             */
            String query = "SELECT * FROM main WHERE name LIKE '" + imagen.replace('*', '%') + "' OR nameNN LIKE '"
                    + imagen.replace('*', '%') + "' GROUP BY nameNN ORDER BY nameNN";
            ResultSet rs = DB.getInstance().query(query);
            while(rs.next()){
                Vector<String> result = new Vector<String>(2);
                result.add(rs.getString("nameNN"));
                result.add(rs.getString("name"));
                data.add(result);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }
        return data;
    }

    /**
     * Image key search.
     * 
     * @param imagen
     *            the imagen
     * 
     * @return the int
     */
    public static int imageKeySearch(String imagen) {

        int key = -1;

        try {
            // Creamos un objeto Statement que se conectara a la BD
            Statement stmt = conn.createStatement();
            String query = "SELECT id FROM Image WHERE path = \"" + imagen + "\"";
            ResultSet res = stmt.executeQuery(query);
            if (res.next()) {
                key = res.getInt("id");
            }
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }

        return key;
    }

    // Devuelve id de imagen por nombre
    /**
     * Image key search name.
     * 
     * @param imagen
     *            the imagen
     * 
     * @return the int
     */
    public static int imageKeySearchName(String imagen) {

        int key = -1;

        try {
            // Creamos un objeto Statement que se conectara a la BD
            Statement stmt = conn.createStatement();
            String query = "SELECT id FROM Image WHERE name = \"" + imagen + "\"";
            ResultSet res = stmt.executeQuery(query);
            if (res.next()) {
                key = res.getInt("id");
            }
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }

        return key;
    }

    // Devuelve id de una categoría por nombre
    /**
     * Concept key search.
     * 
     * @param concept
     *            the concept
     * 
     * @return the int
     */
    public static int conceptKeySearch(String concept) {

        int key = -1;

        try {
            // Creamos un objeto Statement que se conectara a la BD
            Statement stmt = conn.createStatement();
            String query = "SELECT id FROM Concept WHERE word = \"" + concept + "\"";
            ResultSet res = stmt.executeQuery(query);
            if (res.next()) {
                key = res.getInt("id");
            }
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }

        return key;
    }

    // Obtiene las palabras clave de una imagen

    private static PreparedStatement imageConcepts;

    /**
     * Asociated concept search.
     * 
     * @param path
     *            the path
     * 
     * @return the vector< string>
     */

    public static Vector<String> asociatedConceptSearch(String path) {

        Vector<String> data = new Vector<String>();
        ResultSet res;
        String query;

        try {
            // Creamos un objeto Statement que se conectara a la BD
            Statement stmt = conn.createStatement();
            query = "SELECT id FROM Image WHERE path=\"" + path + "\"";
            res = stmt.executeQuery(query);
            int id = res.getInt(1);
            if (imageConcepts == null) {
                stmt.addBatch("create index if not exists kk on Asociated (image)");
                stmt.executeBatch();
                imageConcepts = conn.prepareStatement("SELECT word FROM Concept C left join Asociated A "
                        + "on C.id = A.concept WHERE A.image = ?");
            }
            imageConcepts.setInt(1, id);
            res = imageConcepts.executeQuery();
            while (res.next()) {
                data.add(res.getString("word"));
            }
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }

        return data;
    }

    /**
     * Delete the association between the concept with the id indicate by
     * <code>concept</code> and the image with the id indicate by
     * <code>image</code>.
     * 
     * @param concept
     *            the concept
     * @param image
     *            the image
     * 
     * @return the int
     */
    public static int deleteAsociatedDB(int concept, int image) {

        String deleteQuery;
        int result = -1;

        try {
            Statement stmt = conn.createStatement();

            // Hacemos lo mismo con el concepto
            deleteQuery = "DELETE FROM Asociated WHERE image = \"" + image + "\" AND concept =  \"" + concept + "\"";
            result = stmt.executeUpdate(deleteQuery);
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }
        return result;
    }

    /**
     * Modify image.
     * 
     * @param path
     *            the path
     * @param concepts
     *            the concepts
     * 
     * @return the int
     */
    public static int modifyImage(String path, Vector concepts) {
        int i = 0;

        int image = imageKeySearch(path);
        if (image == -1)
            System.out.println("Error con la imagen");
        Vector oldConcepts = asociatedConceptSearch(path);
        for (int j = 0; j < oldConcepts.size(); j++) {
            if (!concepts.contains(oldConcepts.elementAt(j))) {
                int conceptKey = conceptKeySearch((String) oldConcepts.elementAt(j));
                deleteAsociatedDB(conceptKey, image);
            }
        }
        for (int j = 0; j < concepts.size(); j++) {
            int conceptKey = conceptKeySearch((String) concepts.elementAt(j));
            if (conceptKey == -1) {
                insertConceptDB((String) concepts.elementAt(j));
                conceptKey = conceptKeySearch((String) concepts.elementAt(j));
            }
            if (!areAsociated(conceptKey, image))
                insertAsociatedDB(conceptKey, image);
        }

        return i;
    }

    /**
     * Are asociated.
     * 
     * @param concept
     *            the concept
     * @param image
     *            the image
     * 
     * @return true, if successful
     */
    public static boolean areAsociated(int concept, int image) {

        ResultSet res;
        String query;
        boolean success = false;

        try {
            // Creamos un objeto Statement que se conectara a la BD
            Statement stmt = conn.createStatement();
            query = "SELECT * FROM Asociated A " + "WHERE A.concept = \"" + concept + "\" AND A.image = \"" + image
                    + "\"";
            res = stmt.executeQuery(query);

            if (res.next()) {
                success = true;
            }
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }
        return success;
    }

    /**
     * Search.
     * 
     * @param keyWord1
     *            the key word1
     * @param searchOptions1
     *            the search options1
     * @param keyWord2
     *            the key word2
     * @param searchOptions2
     *            the search options2
     * @param keyWord3
     *            the key word3
     * 
     * @return the vector
     */
    public static Vector<Vector<String>> imageSearchByKeyWords(String keyWord1, int searchOptions1, String keyWord2,
            int searchOptions2, String keyWord3) {

        Vector<Vector<String>> data = new Vector<Vector<String>>();
        ResultSet res;
        String query;

        String firstKeyWord = order(keyWord1.trim()).replace('*', '%');
        String secondKeyWord = order(keyWord2.trim()).replace('*', '%');
        String thirdKeyWord = order(keyWord3.trim()).replace('*', '%');

        try {
            // Creamos un objeto Statement que se conectara a la BD
            Statement stmt = conn.createStatement();
            query = "SELECT DISTINCT I.path path, I.name name, I.noaccents noaccents FROM Concept C, Asociated A, Image I ";

            if (searchOptions1 == 0 && searchOptions2 == 0) { // Search by
                                                              // keyWord1

                query = query + "WHERE C.noaccents LIKE \"" + firstKeyWord
                        + "\" AND I.id = A.image AND A.concept = C.id";

            } else if (searchOptions2 == 0) { // Search by keyWord1, keyWord2

                switch (searchOptions1) {

                case SEARCH_OPTIONS_AND:

                    query = query + "WHERE C.noaccents LIKE \"" + firstKeyWord
                            + "\" AND I.id = A.image AND A.concept = C.id "
                            + "AND I.name IN (SELECT DISTINCT I.name name FROM Concept C, Asociated A, Image I "
                            + "WHERE C.noaccents LIKE \"" + secondKeyWord
                            + "\" AND I.id = A.image AND A.concept = C.id)";
                    break;

                case SEARCH_OPTIONS_OR:

                    query = query + "WHERE (C.noaccents LIKE \"" + firstKeyWord + "\" OR C.noaccents LIKE \""
                            + secondKeyWord + "\") AND I.id = A.image AND A.concept = C.id";

                }
            } else {// Search by keyWord1, keyWord2, keyWord3

                switch (searchOptions1) {

                case SEARCH_OPTIONS_AND:

                    switch (searchOptions2) {

                    case SEARCH_OPTIONS_AND: // keyWord1 AND keyWord2 AND
                                             // keyWord3

                        query = query + "WHERE C.noaccents LIKE \"" + firstKeyWord
                                + "\" AND I.id = A.image AND A.concept = C.id "
                                + "AND I.name IN (SELECT DISTINCT I.name name FROM Concept C, Asociated A, Image I "
                                + "WHERE C.noaccents LIKE \"" + secondKeyWord
                                + "\" AND I.id = A.image AND A.concept = C.id) "
                                + "AND I.name IN (SELECT DISTINCT I.name name FROM Concept C, Asociated A, Image I "
                                + "WHERE C.noaccents LIKE \"" + thirdKeyWord
                                + "\" AND I.id = A.image AND A.concept = C.id)";
                        break;

                    case SEARCH_OPTIONS_OR: // (keyWord1 AND keyWord2) OR
                                            // keyWord3

                        query = query
                                + "WHERE C.noaccents LIKE \""
                                + firstKeyWord
                                + "\" AND I.id = A.image AND A.concept = C.id "
                                + "AND I.name IN (SELECT DISTINCT I.name name FROM Concept C, Asociated A, Image I "
                                + "WHERE C.noaccents LIKE \""
                                + secondKeyWord
                                + "\" AND I.id = A.image AND A.concept = C.id) "
                                + "UNION SELECT DISTINCT I.path path, I.name name, I.noaccents noaccents FROM Concept C, Asociated A, Image I "
                                + "WHERE C.noaccents LIKE \"" + thirdKeyWord
                                + "\" AND I.id = A.image AND A.concept = C.id";
                        break;
                    }
                    break;

                case SEARCH_OPTIONS_OR:

                    switch (searchOptions2) {

                    case SEARCH_OPTIONS_AND: // (keyWord1 OR keyWord2) AND
                                             // keyWord3

                        query = query + "WHERE (C.noaccents LIKE \"" + firstKeyWord + "\" OR C.noaccents LIKE \""
                                + secondKeyWord + "\") AND I.id = A.image AND A.concept = C.id "
                                + "AND I.name IN (SELECT DISTINCT I.name name FROM Concept C, Asociated A, Image I "
                                + "WHERE C.word LIKE \"" + thirdKeyWord + "\" AND I.id = A.image AND A.concept = C.id)";
                        break;

                    case SEARCH_OPTIONS_OR: // keyWord1 OR keyWord2 OR keyWord3

                        query = query + "WHERE (C.noaccents LIKE \"" + firstKeyWord + "\" OR C.noaccents LIKE \""
                                + secondKeyWord + "\" OR C.noaccents LIKE \"" + thirdKeyWord
                                + "\") AND I.id = A.image AND A.concept = C.id";
                        break;
                    }
                    break;
                }
            }

            query = query + " ORDER BY I.noaccents";

            res = stmt.executeQuery(query);

            while (res.next()) {
                Vector result = new Vector(2);
                result.add(res.getString("path"));
                result.add(res.getString("name"));
                data.add(result);
            }
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }

        return data;
    }

    /**
     * Order.
     * 
     * @param word
     *            the word
     * 
     * @return the string
     */
    private static String order(String word) {
        CharSequence seq_nz = "nz";
        CharSequence seq_ñ = "ñ";
        CharSequence seq_NZ = "NZ";
        CharSequence seq_Ñ = "Ñ";
        String result = word.replace(' ', '_').replace(',', '-').replace('á', 'a').replace('é', 'e').replace('í', 'i')
                .replace('ó', 'o').replace('ú', 'u').replace('Á', 'A').replace('É', 'E').replace('Í', 'I')
                .replace('Ó', 'O').replace('Ú', 'U').replace(seq_ñ, seq_nz).replace(seq_Ñ, seq_NZ).toLowerCase();
        return result;
    }
}
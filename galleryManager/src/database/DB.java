package database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import configuration.TConfiguration;

public class DB {

    protected Connection con;
    private static DB instance;
    private static Statement s;
    private static PreparedStatement ps;

    private DB() {
        // Creating connection
        try {
            // ConfigManager cm = ConfigManager.getInstance();
            // String dbName = cm.getProperty(ConfigManager.KEY_DB_FILENAME);
            // String dbPathName = cm.getProperty(ConfigManager.KEY_ROOT_PATH);

            Class.forName("org.sqlite.JDBC");

            con = DriverManager.getConnection("jdbc:sqlite:" + TConfiguration.getDatabasePath());

            createTablesDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DB getInstance() throws SQLException {

        if (instance == null) {
            try {
                instance = new DB();
            } catch (Exception e) {
                e.printStackTrace();
                throw new SQLException();
            }
        }

        return instance;
    }

    public ResultSet query(String sql) throws SQLException {
        s = con.createStatement();
        ResultSet rs = null;
		String finalSQL = DB.escapeQuery(sql);
		rs = s.executeQuery(finalSQL);
        return rs;
    }

	public static String escapeQuery(String sql) {
		String upperCaseSQL = sql.toUpperCase();

		String selectPart = "";
		String fromPart = "";
		if (upperCaseSQL.indexOf(" WHERE ") > 0) {
			selectPart = sql.substring(0,
					upperCaseSQL.indexOf(" WHERE "));
			fromPart = sql.substring(upperCaseSQL.indexOf(" WHERE "));
		} else {
			selectPart = sql;
		}

		return selectPart + fromPart.replace('*', '%');
	}

    public int update(String sql) throws SQLException {
        s = con.createStatement();
        int rows = 0;
        rows = s.executeUpdate(sql);
        return rows;
    }

    public void close() throws SQLException {
        con.close();
        con = null;
        instance = null;
    }

    public void activateTransactions() throws SQLException {
        con.setAutoCommit(false);
    }

    public void executeQueries() throws SQLException {
        con.setAutoCommit(true);
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        ps = con.prepareStatement(sql);
        return ps;
    }

    public void executeBatch(String sql) throws SQLException {
        s = con.createStatement();
        s.addBatch(sql);
        s.executeBatch();
    }

    public String getImagesPath() throws FileNotFoundException, IOException {
        return TConfiguration.getImagesPath();
    }

    private void createTablesDB() {

        try {
            // Creating MAIN table
            String query = "CREATE TABLE IF NOT EXISTS main (word VARCHAR(50), idL INTEGER, idT INTEGER, name VARCHAR(50), nameNN VARCHAR(50))";
            update(query);

            // Creating LANGUAGE table
            query = "CREATE TABLE IF NOT EXISTS language(" + "id INTEGER PRIMARY KEY," + "name VARCHAR(45) NOT NULL)";
            update(query);

            // Creating TYPE table
            query = "CREATE TABLE IF NOT EXISTS type (" + "id INTEGER PRIMARY KEY," + "name VARCHAR(45) NOT NULL)";
            update(query);

            // Creating index
            update("CREATE UNIQUE INDEX IF NOT EXISTS main_index ON main (word, idL, idT, name, nameNN)");

			// Inserting default data
			String languages[] = { "Castellano", "Ingles", "Frances",
					"Catalan", "Italiano", "Aleman", "Portugues",
					"Portugues Brasil", "Gallego" };
			query = "INSERT OR IGNORE INTO language(id,name) VALUES (?,?)";
			PreparedStatement stmt = prepareStatement(query);
			for (int i = 0; i < languages.length; i++) {
				stmt.setInt(1, i);
				stmt.setString(2, languages[i]);
				stmt.executeUpdate();
			}
			stmt.close();

			String types[] = { "nombreComun", "descriptivo", "verbo",
					"miscelanea", "nombrePropio", "contenidoSocial" };
			query = "INSERT OR IGNORE INTO type(id,name) VALUES (?,?)";
			stmt = prepareStatement(query);
			for (int i = 0; i < types.length; i++) {
				stmt.setInt(1, i);
				stmt.setString(2, types[i]);
				stmt.executeUpdate();
			}
			stmt.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }

    }
}

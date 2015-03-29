package configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class TConfiguration {

    private static final String configPath = File.separator + "conf" + File.separator + "conf.properties";
    private static Properties p;
	private static String absolutePath;

    /*
     * KEY - VALUES
     */
    private static final String KEY_DATABASE = "database.name";
    private static final String VALUE_DATABASE = File.separator + "AraSuite.db";

    private static final String KEY_IMAGEPATH = "image.path";
    private static final String VALUE_IMAGEPATH = File.separator + "images";

    private static final String KEY_LANGUAGE = "language";
	private static final String VALUE_LANGUAGE = "Castellano";

	public static final String KEY_DATABASE_UPDATE_URL = "updateUrl";
	private static final String VALUE_DATABASE_UPDATE_URL = "http://arasaac.org/zona_descargas/arasuite/pictos_data";

	public static final String KEY_DB_RELEASE_DATE = "dbReleaseDate";

    /*
     * PUBLIC FUNCTIONS
     */
    
    public static String getDatabasePath() {
        return getProperty(KEY_DATABASE);
    }

    public static String getImagesPath() {
        return getProperty(KEY_IMAGEPATH);
    }
    
    public static String getLanguage() {
        return getProperty(KEY_LANGUAGE);
    }

	public static String getAbsoultePath() {
		return absolutePath;
	}

    /*
     * PRIVATE FUNCTIONS
     */
    
	public static void setProperty(String key, String value) {
		try {
			checkConfigurationFile();
			p.setProperty(key, value);
			File f = new File(absolutePath + configPath);
			p.store(new FileOutputStream(f), "Default properties created.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public static String getProperty(String key) {
        checkConfigurationFile();
        return p.getProperty(key);
    }

    private static void checkConfigurationFile() {
        if(p==null){
            try {
                // Obtaining absolute path
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
					absolutePath = System.getProperty("user.home")
							+ File.separator + "arasuite";
                } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
					absolutePath = System.getProperty("user.home")
							+ File.separator + ".arasuite";
                } else {
					absolutePath = System.getProperty("user.home")
                            + File.separator + ".arasuite";
                }

                System.out.println(absolutePath);
				System.out.println(absolutePath + configPath);

				File f = new File(absolutePath + configPath);
				if (!f.exists()) {
                    // The configuration file doesn't exist

                    // Creating conf directory
                    File w = new File(absolutePath + File.separator + "conf");
                    w.mkdirs();
                    System.out.println(w.getAbsolutePath());

                    // Creating conf file
                    System.out.println(f.getAbsolutePath());
                    f.createNewFile();

                    // Loading properties
                    p = new Properties();
                    p.load(new FileInputStream(f));

                    p.setProperty(KEY_DATABASE, absolutePath + VALUE_DATABASE);
                    p.setProperty(KEY_IMAGEPATH, absolutePath + VALUE_IMAGEPATH);
                    p.setProperty(KEY_LANGUAGE, VALUE_LANGUAGE);
					p.setProperty(KEY_DATABASE_UPDATE_URL,
							VALUE_DATABASE_UPDATE_URL);

                    p.store(new FileOutputStream(f), "Default properties created.");

                } else {
                    p = new Properties();
                    try {
						p.load(new FileInputStream(new File(absolutePath
								+ configPath)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

	public static void setLanguage(String language) {
		setProperty(KEY_LANGUAGE, language);
	}
}

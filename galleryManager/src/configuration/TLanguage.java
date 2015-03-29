package configuration;

import java.io.File;
import java.util.ResourceBundle;

public class TLanguage {

	private static ResourceBundle captions;

    /**
     * It inits the language file and returns true if the file is loaded
     * correctly.
     * 
     * @param lang
     * @return
     */
	public static boolean initLanguage(String lang) {
		ResourceBundle langs = ResourceBundle.getBundle("lang" + File.separator
				+ "languages");
		try {
			String locale = langs.getString(lang.replaceAll(" ", "_")
					.toUpperCase());
			captions = ResourceBundle.getBundle("lang" + File.separator
					+ locale);
		} catch (Exception e) {
			captions = ResourceBundle.getBundle("lang" + File.separator + "en");
		}

		return true;
    }

    public static String getString(String key) {
		return captions.getString(key);
    }
}

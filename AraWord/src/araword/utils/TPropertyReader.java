package araword.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class TPropertyReader {

	public  String getPropertyValue(String propDir,String propFileName,String propertyName) throws IOException
	{
		// the bin/ folder is returned when getting the class loader
		ClassLoader classLoader = TPropertyReader.class.getClassLoader();
		File classpathRoot = new File(classLoader.getResource("").getPath());
		File t1 = classpathRoot.getParentFile();
		File t = new File (t1.getAbsolutePath()+File.separator+propDir+File.separator+propFileName);

		Properties prop = new Properties();
 
		InputStream inputStream = new FileInputStream(t); //TPropertyReader.class.getClassLoader().getResourceAsStream(propFileName);
 
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
		return prop.getProperty(propertyName);		
	}
	
}

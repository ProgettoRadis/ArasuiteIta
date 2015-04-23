package araword.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class TPropertyReader {

	public  String getPropertyValue(String propFileName,String propertyName) throws IOException
	{
		System.out.println(new File(".").getPath());
		Properties prop = new Properties();
 
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
		return prop.getProperty(propertyName);
		
	}
	
}

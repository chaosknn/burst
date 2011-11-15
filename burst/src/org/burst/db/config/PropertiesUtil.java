package org.burst.db.config;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesUtil {
	
	public static final String PROPERTIES_DIR_NAME = "properties";
	public static String PROPERTIES_PATH = "";
	
	public static Properties loadProp(String filename) throws Exception{
		Properties prop = new Properties();
		String propFile = getPropertiesPath() + filename;
		prop.load(new FileInputStream(propFile));
		
		return prop;
	}
	
	public static String getProperty(Properties prop, String key) throws Exception{
		return prop.getProperty(key);
	}
	
	// Notice  E:/workspace/F10/bin/properties/
	public static String getPropertiesPath(){
		if(PROPERTIES_PATH.length() > 0){
			return PROPERTIES_PATH;
		}
		
		PROPERTIES_PATH = AppPathUtil.getAppPath() + AppPathUtil.getFileSeparator() + 
			PROPERTIES_DIR_NAME + AppPathUtil.getFileSeparator();
		
		return PROPERTIES_PATH;
	}
}

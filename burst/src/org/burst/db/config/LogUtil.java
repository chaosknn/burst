package org.burst.db.config;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

public class LogUtil {
	
	private static boolean inited = false;
	
	public static void init(boolean bForce) throws Exception{
		if(inited && !bForce){
			return;
		}
		
		try{
			Properties props = new Properties();
			String log4jProperties = "log4j.properties";
			String log4jPath = PropertiesUtil.getPropertiesPath() + log4jProperties;
			FileInputStream istream = new FileInputStream(log4jPath);
            props.load(istream);
            istream.close();
            PropertyConfigurator.configure(props);
			
            inited = true;
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}

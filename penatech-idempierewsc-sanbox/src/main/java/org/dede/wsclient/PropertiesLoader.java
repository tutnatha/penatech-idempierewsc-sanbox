package org.dede.wsclient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

	public PropertiesLoader() {
		
	}
	
	public String getPropValue(String param) {
		
		Properties prop = new Properties();
		String val = null;
		try (InputStream input = new FileInputStream("penatech.properties")) {
		
			
			if (input == null) {
                System.out.println("Sorry, unable to find " + "penatech.properties");
                return val;
            }
			
			// load a properties file
            prop.load(input);
            val = prop.getProperty(param);
            
            System.out.println(prop.getProperty("db.url"));
            System.out.println(prop.getProperty("db.user"));
            System.out.println(prop.getProperty("db.password"));
		}catch (IOException ex) {
            ex.printStackTrace();
        }
		
		return val;
		
	}
}

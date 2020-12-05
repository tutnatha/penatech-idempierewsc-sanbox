package org.dede.wsclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.idempiere.webservice.client.base.LoginRequest;
import org.idempiere.webservice.client.exceptions.XMLWriteException;
import org.idempiere.webservice.client.net.WebServiceConnection;


public abstract class AbstractTestWS2 {
	private LoginRequest login;
	private WebServiceConnection client;

	public AbstractTestWS2() {
		login = new LoginRequest();
//		login.setUser("SuperUser");
		login.setUser(getPropValue("idempiere.user"));
//		login.setPass("System");
		login.setPass(getPropValue("idempiere.pass"));
		login.setClientID(Integer.valueOf(getPropValue("idempiere.clientid")));		//11
		login.setRoleID(Integer.valueOf(getPropValue("idempiere.roleid")));			//102
		login.setOrgID(Integer.valueOf(getPropValue("idempiere.orgid")));				//0
		login.setStage(Integer.valueOf(getPropValue("idempiere.stage")));				//2

		client = new WebServiceConnection();
		client.setAttempts(3);
		client.setTimeout(5000);
		client.setAttemptsTimeout(5000);
		client.setUrl(getUrlBase());
		
		client.setAppName("Java Test WS Client");
		runTest();
	}

	public LoginRequest getLogin() {
		return login;
	}

	public String getUrlBase() {
//		return "http://localhost:8031";
//            return "https://test-postgresql.idempiere.org/";
//            return "http://192.168.43.57:8080/ADInterface/services/ModelADService";
//		return "http://192.168.43.251:8080/ADInterface/services/ModelADService";
//		return "http://192.168.43.251:8080/ADInterface/services/ModelADService";
//		return "http://169.254.161.184:8080/ADInterface/services/ModelADService";
		
		String url = getPropValue("webservice.url");		//cukup ini
//		url = url+"/ADInterface/services/ModelADService";
		return url;
	}

//addby ajus 30 june 2020
	public String getUrlBase(String svc) {
//		String url="http://192.168.43.251:8080/ADInterface/services/";
		
		String url = getPropValue("webservice.url");
		url = url+"/ADInterface/services/ModelADService";
		
		if (svc.equals("compositeInterface")) {
			url = url + "compositeInterface";
		}else if (svc.equals("ModelADService")) {
			url = url + "ModelADService";
		}
		return url;
	}
	
	public WebServiceConnection getClient() {
		return client;
	}

	public void printTotal() {
		System.out.println("--------------------------");
		System.out.println("Web Service: " + getWebServiceType());
		System.out.println("Attempts: " + client.getAttemptsRequest());
		System.out.println("Time: " + client.getTimeRequest());
		System.out.println("--------------------------");
	}

	public void saveRequestResponse() {
		try {
//			getClient().writeRequest("../documents/" + getWebServiceType() + "_request.xml");
//			getClient().writeResponse("../documents/" + getWebServiceType() + "_response.xml");
//                        getClient().writeRequest("/home/tutnatha/NetBeansProjects/idempierewsc-sanbox/idempierewsc-sanbox/src/main/documents/" + getWebServiceType() + "_request.xml");
//			getClient().writeResponse("/home/tutnatha/NetBeansProjects/idempierewsc-sanbox/idempierewsc-sanbox/src/main/documents/" + getWebServiceType() + "_response.xml");

//			getClient().writeRequest("/Users/user/eclipse-workspace/Unicenta4.3.2workspace/penatech-idempierewsc-sanbox/src/main/documents/" + getWebServiceType() + "_request.xml");
//			getClient().writeResponse("/Users/user/eclipse-workspace/Unicenta4.3.2workspace/penatech-idempierewsc-sanbox/src/main/documents/" + getWebServiceType() + "_response.xml");
			
			getClient().writeRequest(getPropValue("path_request.xml")+ getWebServiceType() + "_request.xml");
			getClient().writeResponse(getPropValue("path_response.xml")+ getWebServiceType() + "_response.xml");
			
		} catch (XMLWriteException e) {
			e.printStackTrace();
		}
	}

	public void printRequestResponse() {
		try {
			getClient().writeRequest(System.out);
			System.out.println();
			System.out.println();
			getClient().writeResponse(System.out);
		} catch (XMLWriteException e) {
			e.printStackTrace();
		}
	}

	public void runTest() {
		testPerformed();
		saveRequestResponse();
		printTotal();
		System.out.println();
	}

	public abstract String getWebServiceType();

	public abstract void testPerformed();
	

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


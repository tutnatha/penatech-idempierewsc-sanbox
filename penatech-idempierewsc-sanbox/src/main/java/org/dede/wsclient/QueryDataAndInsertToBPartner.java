package org.dede.wsclient;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.idempiere.webservice.client.base.DataRow;
import org.idempiere.webservice.client.base.Enums.WebServiceResponseStatus;
import org.idempiere.webservice.client.base.Field;
import org.idempiere.webservice.client.net.WebServiceConnection;
import org.idempiere.webservice.client.request.QueryDataRequest;
import org.idempiere.webservice.client.response.WindowTabDataResponse;
//import org.idempiere.wsclienttest.AbstractTestWS;

public class QueryDataAndInsertToBPartner extends AbstractTestWS2 {

	@Override
	public String getWebServiceType() {
		return "QueryBPartnerTest-KU";
//                return "QueryBPartner";
	}

	@Override
	public void testPerformed() {
		// TODO Auto-generated method stub
		QueryDataRequest ws = new QueryDataRequest();
		ws.setWebServiceType(getWebServiceType());
		ws.setLogin(getLogin());
		ws.setLimit(3);
		ws.setOffset(3);
		
		DataRow data = new DataRow();
//		data.addField("Name", "%Store%");	//Ganti filter = Customer
		data.addField("Name", "%");	//Ganti filter = Customer
		data.addField("IsCustomer", "Y");
		ws.setDataRow(data);

		WebServiceConnection client = getClient();
		
		//Load properties
		PropertiesLoader pl = new PropertiesLoader();	
		
		//connect to local database
//		String jdbcUrl = "jdbc:postgresql://localhost:5432/unicenta434";
//	    String username = "postgres";
//	    String password = "postgres";
		
		String jdbcUrl = pl.getPropValue("db.url");
		String username = pl.getPropValue("db.user");
		String password = pl.getPropValue("db.password");
	    
	    Connection conn = null;
	    PreparedStatement psInsert = null;
	    PreparedStatement psInsertCust = null;
	    
		try {
			//kapling database
			//Open connection
	        conn = DriverManager.getConnection(jdbcUrl, username, password);
	        
	        psInsert = conn.prepareStatement("INSERT INTO temp_bpartner (c_bpartner_id, bpartner_name, tax_id, value, logo_id) " 
	        + "VALUES (?,?,?,?,?)");         
			
	        //psInsert untuk Customer pilih dulu yg not null
	        psInsertCust = conn.prepareStatement("INSERT INTO CUSTOMERS (id, searchkey, name, maxdebt, visible, isvip, discount) VALUES(?,?,?,?,?,?,?)");
			
			//kapling webservice
			WindowTabDataResponse response = client.sendRequest(ws);

			if (response.getStatus() == WebServiceResponseStatus.Error) {
				System.out.println(response.getErrorMessage());
			} else {
				System.out.println("Respons Data: " + response.toString());
				System.out.println("Total rows: " + response.getTotalRows());
				System.out.println("Num rows: " + response.getNumRows());
				System.out.println("Start row: " + response.getStartRow());
				System.out.println();
				
				//Variable businer partner Class 
				Integer CBParterID = 0;
				String Name  = "";
				String Value  = "";
				String TaxID  = "";
				Integer LogoID  = 0;
				
				//variable Customer
				Float maxdebt = (float) 1000000;
				Boolean visible = true;
				Boolean isvip = false;
				Float discount = (float) 0;
				
				for (int i = 0; i < response.getDataSet().getRowsCount(); i++) {
					
					System.out.println("Row: " + (i + 1));
					
					for (int j = 0; j < response.getDataSet().getRow(i).getFieldsCount(); j++) {
						Field field = response.getDataSet().getRow(i).getFields().get(j);
						System.out.println("Column: " + field.getColumn() + " = " + field.getStringValue());
						//Array List Penampung BPartner Class
						//Atau pakai kondisi per Kolom
						if (field.getColumn().equals("C_BPartner_ID")) {
							CBParterID = field.getIntValue();
							
						}else if(field.getColumn().equals("Value")) {
							Value = field.getStringValue();
							
						}else if(field.getColumn().equals("Name")) {
							Name = field.getStringValue();
							
						}else if(field.getColumn().equals("TaxID")) {
							TaxID = field.getStringValue();
							
						}else if(field.getColumn().equals("Logo_ID")){
//							if(field.getIntValue().equals(null)){setInt(LogoID,0);}
//							else {
//							LogoID = field.getIntValue();
						}
					}
					System.out.println();
					
					//Find atau Look at database first:
					//pake metode SQL Query:
					//Jika ada Lakukan Statement Update:
					//Jika tidak ada lakukan Insert:
					
					//Insert Row Data ke Table BParner
					//insertIntoBpartner				
					psInsert.setLong(1, CBParterID);
					psInsert.setString(2, Name);
					psInsert.setString(3, TaxID);
					psInsert.setString(4, Value);
					psInsert.setInt(5, LogoID);
					psInsert.executeUpdate(); //remark dulu
					
					
					//Insert ke Table Customer nya Unicenta
					psInsertCust.setString(1,CBParterID.toString());
					//2 searchkey
					psInsertCust.setString(2,CBParterID.toString());
					//3 Name
					psInsertCust.setString(3, Name);
					//4 maxdebt
					psInsertCust.setFloat(4, maxdebt);
					//5 visible
					psInsertCust.setBoolean(5,visible );
					//6 isvip
					psInsertCust.setBoolean(6, isvip);
					//7 discount
					psInsertCust.setFloat(7, discount);
					
					psInsertCust.executeUpdate();
					
				}					

			}
				
//			}
			
		} catch (Exception e) {
		e.printStackTrace();
		}finally {
	          try {
	              // Close connection
	              if (psInsert != null) {
	              	psInsert.close();
	              }
	              if (psInsertCust != null) {
		              	psInsert.close();
		          }
	              if (conn != null) {
	                conn.close();
	              }
	            } catch (Exception e) {
	              e.printStackTrace();
	            }
	          }

	}

	public void insertIntoBPartner() {
//belum dipakai
	}
	
	public static void main(String[] args) {
		new QueryDataAndInsertToBPartner();
	}
	
}

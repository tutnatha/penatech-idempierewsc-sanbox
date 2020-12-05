package org.dede.wsclient;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import org.idempiere.webservice.client.base.DataRow;
import org.idempiere.webservice.client.base.Enums.WebServiceResponseStatus;
import org.idempiere.webservice.client.base.Field;
import org.idempiere.webservice.client.base.LoginRequest;
import org.idempiere.webservice.client.net.WebServiceConnection;
import org.idempiere.webservice.client.request.QueryDataRequest;
import org.idempiere.webservice.client.response.WindowTabDataResponse;
import org.idempiere.wsclienttest.AbstractTestWS;


/* Author: Ajus
 * descriptiio : ancur code but good :D
 * 			: first code */

import java.time.OffsetTime;

import java.time.OffsetDateTime;

public class QueryDataAndInsertToMStrorageOnHand extends AbstractTestWS {
	
	
//	private static final java.time.LocalDate LocalDate = null;
//	private static final LocalTime LocalTime = null;
	
	private Timestamp date;

	@Override
	public String getWebServiceType() {
		// TODO Auto-generated method stub
		return "QueryStorageOnHand";
	}
	
	@Override
	public void testPerformed() {
		// TODO Auto-generated method stub
		QueryDataRequest ws = new QueryDataRequest();
		ws.setWebServiceType(getWebServiceType());
		ws.setLogin(getLogin());
		
//		ws.setLimit(3);
		ws.setLimit(0);
		
//		ws.setOffset(3);
		ws.setOffset(0);
		
		DataRow data = new DataRow();
//		data.addField("Name", "%Store%");
//		data.addField("Name", "%");
		ws.setDataRow(data);

		WebServiceConnection client = getClient();
		
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
	    PreparedStatement psInsertStockCurent = null;
	    
	    //Author : "THE GREAT COPAS MAN" AJUS
	    // copas di https://stackoverflow.com/questions/16520767/issue-inserting-timestamp-in-postgres-table
	    Date date= new Date ();
	    Timestamp timestamp = new Timestamp(date.getTime());
	    this.date = timestamp;
	    
		try {
			conn = DriverManager.getConnection(jdbcUrl, username, password);
	        
			psInsert = conn.prepareStatement ("INSERT INTO temp_m_storageonhand (ad_client_id ," +
			        "ad_org_id ," +
			        "created ," +
			        "createdby ," +
			        "isactive ," +
			        "m_attributesetinstance_id ," +
			        "m_locator_id ," +
			        "m_product_id ," +
			        "qtyonhand ," +
			        "updated ," +
			        "updatedby," +
			        "datematerialpolicy)" + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");

	        		   //psInsertCust = conn.prepareStatement("select * from where");
					
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
						
						//variable products
						Number 	  ad_client_id  = 0;
						Number 	  ad_org_id  	= 0;
						Timestamp created = this.date;
						Number 	  createdby 	= 0;
						String    isactive 		= "Y";
						Number    m_attributesetinstance_id = 0;
						Number    m_locator_id = 0;
						Number    m_product_id = 0;
						Number    qtyonhand = 0;
						Timestamp updated = this.date;
						Number    updatedby = 0;
						Timestamp datematerialpolicy = this.date;
						
						for (int i = 0; i < response.getDataSet().getRowsCount(); i++) {
							
							System.out.println("Row: " + (i + 1));
							
							for (int j = 0; j < response.getDataSet().getRow(i).getFieldsCount(); j++) {
								Field field = response.getDataSet().getRow(i).getFields().get(j);
								System.out.println("Column: " + field.getColumn() + " = " + field.getStringValue());
								//Array List Penampung BPartner Class
								//Atau pakai kondisi per Kolom
								
								if (field.getColumn().equals("AD_Client_ID")) {
//									Number Ad_Client_id = Long.valueOf( (String) field.getValue());
									Integer Ad_Client_id = field.getIntValue();
									ad_client_id = Ad_Client_id;
									
									
								}else if(field.getColumn().equals("Created")){
//									LocalDate Created = (LocalDate) field.getValue();
									created = created;
//									IsPurchased = sIsPurchased.charAt(0);
									
									
								}else if(field.getColumn().equals("Createdby")){
									Integer Createdby = field.getIntValue();
//									IsSold = sIsSold.charAt(0);
									createdby = Createdby;
								
								}else if(field.getColumn().equals("IsActive")){
									String sIsactive = field.getStringValue();
//									IsStocked = sIsStocked.charAt(0);
									isactive = sIsactive;	
									
								}else if(field.getColumn().equals("Updated")){
									updated = updated;
//									resultdate2 = Updated;
									
								}else if(field.getColumn().equals("UpdatedBy")){
									Integer Updatedby = field.getIntValue();
//									M_Product_Category_ID = (float) sM_Product_Category_ID;
//									double varDouble = Double.parseDouble(sM_Product_Category_ID);
									updatedby = Updatedby;
								
								}else if(field.getColumn().equals("M_AttributeSetInstance_ID")){
//									C_TaxCategory_ID = field.getFloatValue();
//									Integer Pricelist = field.getIntValue();
									Integer M_Attributsetinstance_ID = field.getIntValue();
									m_attributesetinstance_id = M_Attributsetinstance_ID ;
									
								}else if(field.getColumn().equals("M_Locator_ID")){
//									C_TaxCategory_ID = field.getFloatValue();
//									Integer Pricelist = field.getIntValue();
									Integer M_Locator_ID = field.getIntValue();
									m_locator_id = M_Locator_ID ;	
	    						
								}else if(field.getColumn().equals("M_Product_ID")){
//									C_TaxCategory_ID = field.getFloatValue();
//									Integer Pricelist = field.getIntValue();
									Integer M_Product_ID = field.getIntValue();
									m_product_id = M_Product_ID ;
									
								}else if(field.getColumn().equals("QtyOnHand")){
//									C_TaxCategory_ID = field.getFloatValue();
//									Integer Pricelist = field.getIntValue();
									qtyonhand = field.getIntValue();
									
								}else if(field.getColumn().equals("DateMaterialPolicy")){
//									C_TaxCategory_ID = field.getFloatValue();
//									Integer Pricelist = field.getIntValue();
									datematerialpolicy = datematerialpolicy;

									
								}else if(field.getColumn().equals("AD_Org_ID")){
//									C_TaxCategory_ID = field.getFloatValue();
//									Integer Pricelist = field.getIntValue();
									ad_org_id = field.getIntValue();
								}
							}
							System.out.println();
							
							//Find atau Look at database first:
							//pake metode SQL Query:
							//Jika ada Lakukan Statement Update:
							//Jika tidak ada lakukan Insert:
							
							//Insert Row Data ke Table Temp_Products
							//insertIntoBpartner				
							psInsert.setInt		(1,  (int) ad_client_id);
							psInsert.setInt 	(2,  (int) ad_org_id);
							psInsert.setObject 	(3,   created);
							psInsert.setInt 	(4,  (int) createdby);
							psInsert.setString 	(5,  isactive);
//							PreparedStatement st = conn.prepareStatement("INSERT INTO mytable (columnfoo) VALUES (?)");
							psInsert.setObject  (6,  m_attributesetinstance_id);
//							psInsert.setInt 	(7,  (int) createdby);			
							psInsert.setInt   	(7,  (int) m_locator_id);
							
							psInsert.setInt 	(8,  (int) m_product_id);
							psInsert.setInt 	(9, (int) qtyonhand);
							psInsert.setObject 	(10,  updated);
							psInsert.setInt 	(11, (int) updatedby);
							psInsert.setObject 	(12,  datematerialpolicy);
							
							psInsert.executeUpdate();
							
							//insert stockcurrent
//							psInsertStockCurret.
						}
			}					
				
			} catch (Exception e) {
			e.printStackTrace();
			}finally {
				try {
			           // Close connection
			           if (psInsert != null) {
			        	   psInsert.close();
			
			           }
		          	} catch (Exception e) {
		              e.printStackTrace();
		          	}
			}
		}
	
	public static void main(String[] args) {
		new QueryDataAndInsertToMStrorageOnHand();
	}
}

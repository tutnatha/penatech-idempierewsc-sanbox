package org.dede.wsclient;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import org.idempiere.webservice.client.base.DataRow;
import org.idempiere.webservice.client.base.Enums.WebServiceResponseStatus;
import org.idempiere.webservice.client.base.Field;
import org.idempiere.webservice.client.net.WebServiceConnection;
import org.idempiere.webservice.client.request.QueryDataRequest;
import org.idempiere.webservice.client.response.WindowTabDataResponse;
import org.idempiere.wsclienttest.AbstractTestWS;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;


public class QueryAndDataInsertToProductPrice extends AbstractTestWS {
	
	@Override
	public String getWebServiceType() {
		// TODO Auto-generated method stub
		return "QueryProductPrice";
//      return "QueryBPartner";
		
	}
		@Override
		public void testPerformed() {
			// TODO Auto-generated method stub
			QueryDataRequest ws = new QueryDataRequest();
			ws.setWebServiceType(getWebServiceType());
			ws.setLogin(getLogin());
			ws.setLimit(0);
			ws.setOffset(0);
			DataRow data = new DataRow();
//			data.addField("Name", "%Store%");
//			data.addField("M_ProductPrice_UU", "%");
			ws.setDataRow(data);
			
			WebServiceConnection client = getClient();
			
			//Load properties
			PropertiesLoader pl = new PropertiesLoader();			
			
			//connect to local database
//			String jdbcUrl = "jdbc:postgresql://localhost:5432/unicenta434";
//		    String username = "postgres";
//		    String password = "postgres";
			
			//Ganti cpirit...
			String jdbcUrl = pl.getPropValue("db.url");
			String username = pl.getPropValue("db.user");
			String password = pl.getPropValue("db.password");
			
		    Connection conn = null;
		    PreparedStatement psInsert = null;
		    PreparedStatement psInsertProducts = null;
		    
		    try {
				//kapling database
				//Open connection
		        conn = DriverManager.getConnection(jdbcUrl, username, password);
		        
		        psInsert = conn.prepareStatement ("INSERT INTO temp_m_productprice (m_pricelist_version_id ," +
		        "m_product_id ," +
		        "ad_client_id ," +
		        "ad_org_id ," +
		        "isactive ," +
		        "created ," +
		        "createdby ," +
		        "updated ," +
		        "updatedby ," +
		        "pricelist ," +
		        "pricestd ," +
		        "pricelimit ," +
		        "m_product_price_id)"  + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
		    	        
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
					Number 	m_pricelist_version_id = 0;
					Number 	m_product_id   = 0;
					Number 	ad_client_id  = 0;
					Number 	ad_org_id = 0;
					String  isactive = "";
//					Date    created = System.currentTimeMillis();
//					long    created = System.currentTimeMillis();
					LocalDate created = LocalDate.now();
					Number 	createdby = 0;
					long    updated = System.currentTimeMillis();
					Number  updatedby = 0;
					
//					Number  pricelist = 0;
//					Number  pricestd = 0;
//					Number  pricelimit = 0;
					
					Float  pricelist = (float)0;
					Float  pricestd = (float)0;
					Float  pricelimit = (float)0;
					
					Number  m_product_price_id = 0;
					
					for (int i = 0; i < response.getDataSet().getRowsCount(); i++) {
						
						System.out.println("Row: " + (i + 1));
						
						for (int j = 0; j < response.getDataSet().getRow(i).getFieldsCount(); j++) {
							Field field = response.getDataSet().getRow(i).getFields().get(j);
							System.out.println("Column: " + field.getColumn() + " = " + field.getStringValue());
							//Array List Penampung BPartner Class
							//Atau pakai kondisi per Kolom
							if (field.getColumn().equals("M_PriceList_Version_ID")) {
								Integer M_Pricelist_id = field.getIntValue();
								m_pricelist_version_id = M_Pricelist_id;
								
							}else if(field.getColumn().equals("M_Product_ID")) {
								Integer M_product_id = field.getIntValue();
								m_product_id = M_product_id;
								
							}else if(field.getColumn().equals("AD_Client_ID")) {
								Integer Ad_client_id = field.getIntValue();
//								IsSummary = sIsSummary.charAt(0);
								ad_client_id = Ad_client_id;
								
							}else if(field.getColumn().equals("AD_Org_ID")) {
								Integer Ad_ord_id = field.getIntValue();
								ad_org_id = Ad_ord_id;
								
							}else if(field.getColumn().equals("IsActive")){
								String sIsactive = field.getStringValue();
//								IsStocked = sIsStocked.charAt(0);
								isactive = sIsactive;
								
							}else if(field.getColumn().equals("Created")){
								Date Created = field.getDateValue();
//								IsPurchased = sIsPurchased.charAt(0);
								
								
							}else if(field.getColumn().equals("Createdby")){
								Integer Createdby = field.getIntValue();
//								IsSold = sIsSold.charAt(0);
								createdby = Createdby;
								
							}else if(field.getColumn().equals("Updated")){
								Date Updated = field.getDateValue();
//								resultdate2 = Updated;
								
							}else if(field.getColumn().equals("Updatedby")){
								Integer Updatedby = field.getIntValue();
//								M_Product_Category_ID = (float) sM_Product_Category_ID;
//								double varDouble = Double.parseDouble(sM_Product_Category_ID);
								updatedby = Updatedby;
							
							}else if(field.getColumn().equals("PriceList")){
//								C_TaxCategory_ID = field.getFloatValue();
//								Integer Pricelist = field.getIntValue();
								float Pricelist = field.getFloatValue();
								pricelist = Pricelist;
								
								
							}else if(field.getColumn().equals("PriceStd")){
//								Integer Pricestd = field.getIntValue();
								float Pricestd = field.getFloatValue();
//								IsBOM = sIsBOM.charAt(0);
								pricestd = Pricestd;
								 
								
							}else if(field.getColumn().equals("PriceLimit")){
//								Integer Pricelimit = field.getIntValue();
								float Pricelimit = field.getFloatValue();
//								IsInvoicePrintDetails = sIsInvoicePrintDetails.charAt(0);
								pricelimit = Pricelimit;
								
								
							}else if(field.getColumn().equals("M_ProductPrice_ID")){
								Integer M_Product_Price_Id = field.getIntValue();
//								IsPickListPrintDetails = sIsPickListPrintDetails.charAt(0); 
								m_product_price_id = M_Product_Price_Id;
								
							}else if (field.getColumn().equals("M_ProductPrice_UU")){
							
							}
							
						}
						
						System.out.println();
						
						//Find atau Look at database first:
						//pake metode SQL Query:
						//Jika ada Lakukan Statement Update:
						//Jika tidak ada lakukan Insert:
						
						//Insert Row Data ke Table Temp_Products
						//insertIntoBpartner				
						psInsert.setInt		(1,  (int) m_pricelist_version_id);
						psInsert.setInt 	(2,  (int) m_product_id);
						psInsert.setInt 	(3,  (int) ad_client_id);
						psInsert.setInt 	(4,  (int) ad_org_id);
						psInsert.setString 	(5,  isactive);
						
						//created
//						long yourmilliseconds = System.currentTimeMillis();
//						SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");    
//						Time resultdate = new Time(created);
//						System.out.println(sdf.format(resultdate));
						
						LocalDate localDate = LocalDate.now();
//						PreparedStatement st = conn.prepareStatement("INSERT INTO mytable (columnfoo) VALUES (?)");
						psInsert.setObject(6, localDate);
						
						psInsert.setInt 	(7,  (int) createdby);			
						
						//updated
						LocalDate resultdate2 = LocalDate.now();
//						System.out.println(sdf.format(resultdate2));
						psInsert.setObject   	(8,  resultdate2);
						
						psInsert.setInt 	(9,  (int) updatedby);
						psInsert.setFloat 	(10, (float) pricelist);
						psInsert.setFloat 	(11, (float) pricestd);
						psInsert.setFloat 	(12, (float) pricelimit);
						psInsert.setInt 	(13, (int) m_product_price_id);
						
						psInsert.executeUpdate();
								
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
		new QueryAndDataInsertToProductPrice();
	}
	
}
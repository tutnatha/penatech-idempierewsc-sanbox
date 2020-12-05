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
import org.idempiere.wsclienttest.AbstractTestWS;

//public class QueryDataAndInsertToProducts extends AbstractTestWS {
public class QueryDataAndInsertToProducts extends AbstractTestWS2 {

	@Override
	public String getWebServiceType() {
		// TODO Auto-generated method stub
		return "QueryProducts";
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
//		data.addField("Name", "%Store%");
		data.addField("Name", "%");
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
	    PreparedStatement psInsertProducts = null;
	    
		try {
			//kapling database
			//Open connection
	        conn = DriverManager.getConnection(jdbcUrl, username, password);
//	        psInsert = conn.prepareStatement("INSERT INTO temp_prod_category (m_product_category_id, value, name, m_product_category_parent_id) " 
//	    	        + "VALUES (?,?,?,?)");         
	    			
//	        psInsert = conn.prepareStatement
//	        		("INSERT INTO temp_products (prod_id, prod_reference, prod_code, prod_name, prod_pricebuy, prod_pricesell, prod_category, prod_taxcat, prod_stockcost, "
//	        				+ "prod_stockvolume, prod_iscom, prod_isscale, prod_isconstant, prod_printkb, prod_sendstatus,"
//	        				+ "prod_isservice, prod_isprice, prod_isverpatrib, prod_warranty, prod_stockunits) " + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	        
	        psInsert = conn.prepareStatement ("INSERT INTO temp_products2 (m_product_id ," +
	        "value ," +
	        "name ," +
	        "c_uom_id ," +
	        "issummary ," +
	        "isstocked ," +
	        "ispurchased ," +
	        "issold ," +
	        "isbom ," +
	        "isinvoiceprintdetails ," +
	        "ispicklistprintdetails ," +
	        "isverified ," +
	        "m_product_category_id ," +
	        "c_taxcategory_id ," +
	        "producttype ," +
	        "iswebstorefeatured ," +
	        "isselfservice ," +
	        "isdropship ," +
	        "isexcludeautodelivery ," +
	        "lowlevel ," +
	        "unitsperpack ," +
	        "iskanban ," +
	        "ismanufactured, " +
	        "isownbox )" +
	    	" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	        
	        //psInsert untuk Customer pilih dulu yg not null
	        psInsertProducts = conn.prepareStatement
	        		("INSERT INTO Products (id, reference, code, name, pricebuy, pricesell, category, taxcat, stockcost, stockvolume, iscom, isscale, isconstant, printkb, sendstatus,"
	        		+ "isservice, isvprice, isverpatrib, warranty, stockunits) " + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	    	        
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
				
				//Variable m_product Class 
				Number M_Product_ID = (double)0;
				String Name = "";
				String IsSummary = "N";
				Number C_UOM_ID = (double)0;
				String IsStocked = "Y";
				String IsPurchased = "Y";
				String IsSold = "Y";
				String Value = "";
				
//				Number M_Product_Category_ID = (int)0;
				String M_Product_Category_ID = "";
				
//				Float C_TaxCategory_ID = (float)0;
				String C_TaxCategory_ID = "";
				
//				char IsBOM = 'N';
				String IsBOM = "N";
				
//				char IsInvoicePrintDetails = 'N';
				String IsInvoicePrintDetails = "N";
				
//				char IsPickListPrintDetails = 'N';
				String IsPickListPrintDetails = "N";
				
//				char IsVerified = 'N';
				String IsVerified = "N";
				
//				char ProductType = 'I';
				String ProductType = "I";
				
//				char IsWebStoreFeatured = 'N';
				String IsWebStoreFeatured = "N";
				
//				char IsSelfService = 'Y';
				String IsSelfService = "Y";
				
//				char IsDropShip = 'N';
				String IsDropShip = "N";
				
//				char IsExcludeAutoDelivery = 'N' ;
				String IsExcludeAutoDelivery = "N";
				
				Float UnitsPerPack = (float) 0;
				
				Float LowLevel = (float) 0;
				
//				char IsKanban = 'N';
				String IsKanban = "N";
				
//				char IsManufactured = 'N' ;
				String IsManufactured = "N";
				
//				char IsOwnBox = 'N';
				String IsOwnBox = "N";
				
				
				
				
				//sprtiinya belum kepakai
//				String 	prod_id = "";
//				String 	prod_reference = "";
//				String 	prod_code = "";
//				String 	prod_name = "";
//				Float  	prod_pricebuy = (float) 0;
//				Float  	prod_pricesell = (float) 0;
//				String 	prod_category = "";
//				String 	prod_taxcat = "";
//				Float 	prod_stockcost = (float) 0;
//				Float 	prod_stockvolume = (float) 0;
//				Boolean prod_iscom = (false);
//				Boolean prod_isscale = (false);
//				Boolean prod_isconstant = (false);
//				Boolean prod_printkb = (false);
//				Boolean prod_sendstatus = (false);
//				Boolean prod_isservice = (false);
//				Boolean prod_isprice = (false);
//				Boolean prod_isverpatrib = (false);
//				Boolean prod_warranty =(false);
//				Float 	prod_stockunits = (float) 0;
				
				
				//variable products
				String 	id = "";
				String 	reference   = "";
				String 	code  = "";
				String 	name = "";
				Float  	pricebuy = (float) 0;
				Float  	pricesell = (float) 0;
				String 	category = "";
				String 	taxcat = "";
				Float 	stockcost = (float) 0;
				Float 	stockvolume = (float) 0;
				Boolean iscom = (false);
				Boolean isscale = (false);
				Boolean isconstant = (false);
				Boolean printkb = (false);
				Boolean sendstatus = (false);
				Boolean isservice = (false);
				Boolean isvprice = (false);
				Boolean isverpatrib = (false);
				Boolean warranty =(false);
				Float 	stockunits = (float) 0;
	    				
				for (int i = 0; i < response.getDataSet().getRowsCount(); i++) {
					
					System.out.println("Row: " + (i + 1));
					
					for (int j = 0; j < response.getDataSet().getRow(i).getFieldsCount(); j++) {
						Field field = response.getDataSet().getRow(i).getFields().get(j);
						System.out.println("Column: " + field.getColumn() + " = " + field.getStringValue());
						//Array List Penampung BPartner Class
						//Atau pakai kondisi per Kolom
						if (field.getColumn().equals("M_Product_ID")) {
							M_Product_ID = field.getDoubleValue();
							id = field.getStringValue();
							
						}else if(field.getColumn().equals("Name")) {
							Name = field.getStringValue();
							
						}else if(field.getColumn().equals("IsSummary")) {
							String sIsSummary = field.getStringValue();
//							IsSummary = sIsSummary.charAt(0);
							IsSummary = sIsSummary;
							
						}else if(field.getColumn().equals("C_UOM_ID")) {
//							C_UOM_ID = field.getFloatValue();
							C_UOM_ID = field.getDoubleValue();
							
						}else if(field.getColumn().equals("IsStocked")){
							String sIsStocked = field.getStringValue();
//							IsStocked = sIsStocked.charAt(0);
							IsStocked = sIsStocked;
							
						}else if(field.getColumn().equals("IsPurchased")){
							String sIsPurchased = field.getStringValue();
//							IsPurchased = sIsPurchased.charAt(0);
							IsPurchased = sIsPurchased;		
							
						}else if(field.getColumn().equals("IsSold")){
							String sIsSold = field.getStringValue();
//							IsSold = sIsSold.charAt(0);
							IsSold = sIsSold;
							
						}else if(field.getColumn().equals("Value")){
							Value = field.getStringValue();
							
						}else if(field.getColumn().equals("M_Product_Category_ID")){
							String sM_Product_Category_ID = field.getStringValue();
//							M_Product_Category_ID = (float) sM_Product_Category_ID;
//							M_Product_Category_ID = field.getFloatValue();
//							double varDouble = Double.parseDouble(sM_Product_Category_ID);
							M_Product_Category_ID = sM_Product_Category_ID; 
						}else if(field.getColumn().equals("C_TaxCategory_ID")){
//							C_TaxCategory_ID = field.getFloatValue();
							C_TaxCategory_ID = field.getStringValue();
							
						}else if(field.getColumn().equals("IsBOM")){
							 String sIsBOM = field.getStringValue();
//							 IsBOM = sIsBOM.charAt(0);
							 IsBOM = sIsBOM;
							
						}else if(field.getColumn().equals("IsInvoicePrintDetails")){
							String sIsInvoicePrintDetails = field.getStringValue();
//							IsInvoicePrintDetails = sIsInvoicePrintDetails.charAt(0);
							IsInvoicePrintDetails = sIsInvoicePrintDetails;
							
						}else if(field.getColumn().equals("IsPickListPrintDetails")){
							String sIsPickListPrintDetails = field.getStringValue();
//							IsPickListPrintDetails = sIsPickListPrintDetails.charAt(0); 
							IsPickListPrintDetails = sIsPickListPrintDetails;
							
						}else if(field.getColumn().equals("IsVerified")){
							String sIsVerified = field.getStringValue();
//							IsVerified = sIsVerified.charAt(0);
							IsVerified = sIsVerified;
							
						}else if(field.getColumn().equals("ProductType")){
							String sProductType = field.getStringValue();
//							ProductType = sProductType.charAt(0);
							ProductType = sProductType;
							
						}else if(field.getColumn().equals("IsWebStoreFeatured")){
							 String sIsWebStoreFeatured = field.getStringValue();
//							 IsWebStoreFeatured = sIsWebStoreFeatured.charAt(0); 
							 IsWebStoreFeatured = sIsWebStoreFeatured;
							 
						}else if(field.getColumn().equals("IsSelfService")){
							 String sIsSelfService = field.getStringValue();
//							 IsSelfService = sIsSelfService.charAt(0);
							 IsSelfService = sIsSelfService;
							 
						}else if(field.getColumn().equals("IsDropShip")){
							 String sIsDropShip = field.getStringValue();
//							 IsDropShip = sIsDropShip.charAt(0);
							 IsDropShip = sIsDropShip;
							 
						}else if(field.getColumn().equals("IsExcludeAutoDelivery")){
							 String sIsExcludeAutoDelivery = field.getStringValue();
//							 IsExcludeAutoDelivery = sIsExcludeAutoDelivery.charAt(0);
							 IsExcludeAutoDelivery = sIsExcludeAutoDelivery;
							 
						}else if(field.getColumn().equals("UnitsPerPack")){
							 UnitsPerPack = field.getFloatValue();
							
						}else if(field.getColumn().equals("LowLevel")){
							 LowLevel = field.getFloatValue();
							
						}else if(field.getColumn().equals("IsKanban")){
							 String sIsKanban = field.getStringValue();
//							 IsKanban = sIsKanban.charAt(0);
							 IsKanban = sIsKanban;
							 
						}else if(field.getColumn().equals("IsManufactured")){
							String sIsManufactured = field.getStringValue();
//							IsManufactured = sIsManufactured.charAt(0);
							IsManufactured = sIsManufactured;
							
						}else if(field.getColumn().equals("IsOwnBox")){
							 String sIsOwnBox = field.getStringValue();
//							 IsOwnBox = sIsOwnBox.charAt(0) ;
							 IsOwnBox = sIsOwnBox; 
							
						}						
						
					}
					
					System.out.println();
					
					//Find atau Look at database first:
					//pake metode SQL Query:
					//Jika ada Lakukan Statement Update:
					//Jika tidak ada lakukan Insert:
					
					//Insert Row Data ke Table Temp_Products
					//insertIntoBpartner				
//					
//					psInsert.setFloat  (1, (float) M_Product_ID);		//ini Harus Double atau bilangan bulat bukan pecahan
					psInsert.setDouble  (1, (double) M_Product_ID);		//belum di coba
					
					psInsert.setString (2,  Value);
//					psInsert.setObject (3,  IsSummary);
					psInsert.setString (3, Name);
//					
//					psInsert.setFloat  (4,  (float)C_UOM_ID);		//Ganti ke bilangan bulat
					psInsert.setDouble  (4,  (double)C_UOM_ID);
					
					psInsert.setString (5,  IsSummary);
					psInsert.setString (6,  IsStocked);
					psInsert.setString (7,  IsPurchased);
					psInsert.setString (8,  IsSold);

//					psInsert.setDouble (9,   (double) M_Product_Category_ID);
					psInsert.setString (9, IsBOM);
					
//					psInsert.setFloat  (10, (float)C_TaxCategory_ID);
					psInsert.setString (10, IsInvoicePrintDetails);
					
					psInsert.setString (11, IsBOM);
					psInsert.setString (12, IsVerified);
					psInsert.setString (13, M_Product_Category_ID);
					psInsert.setString (14, C_TaxCategory_ID);
					psInsert.setString (15, ProductType);
					psInsert.setString (16, IsWebStoreFeatured);
					psInsert.setString (17, IsSelfService);
					psInsert.setString (18, IsDropShip);
					psInsert.setString (19, IsExcludeAutoDelivery);
					psInsert.setFloat  (20, LowLevel);
					psInsert.setFloat  (21, UnitsPerPack);
					psInsert.setString (22, IsKanban );
					psInsert.setString (23, IsManufactured);
					psInsert.setString (24, IsOwnBox);

					psInsert.executeUpdate(); //remark dulu
					
					
					//Insert ke Table Customer nya Unicenta
//					id = String.valueOf(M_Product_ID);
//					id = M_Product_ID.toString();					
					psInsertProducts.setString (1, id.toString());
//					
					reference = id;
					psInsertProducts.setString (2,reference.toString());
//					
					code = id;
					psInsertProducts.setString (3, code.toString());
//					
					name = Name;
					psInsertProducts.setString (4, name.toString());
//					
					pricebuy = (float) 0;
					psInsertProducts.setFloat  (5, pricebuy.floatValue());
//					
					psInsertProducts.setFloat  (6, pricesell.floatValue());
//					
					category = M_Product_Category_ID; 
					psInsertProducts.setString (7, category.toString());
//					
					taxcat = C_TaxCategory_ID;
					psInsertProducts.setString(8, taxcat.toString());
//					
					psInsertProducts.setFloat(9, stockcost.floatValue());
//					
					psInsertProducts.setFloat(10, stockvolume.floatValue());
//					
					psInsertProducts.setBoolean(11, iscom.booleanValue());
//					
					psInsertProducts.setBoolean(12, isscale.booleanValue());
//					
					psInsertProducts.setBoolean(13, isconstant.booleanValue());
//					
					psInsertProducts.setBoolean(14, printkb.booleanValue());
//					
					psInsertProducts.setBoolean(15, sendstatus.booleanValue());
//					
					psInsertProducts.setBoolean(16, isservice.booleanValue());
//					
					psInsertProducts.setBoolean(17, isvprice.booleanValue());
//					
					psInsertProducts.setBoolean(18, isverpatrib.booleanValue());
//					
					psInsertProducts.setBoolean(19, warranty.booleanValue());
//					
					psInsertProducts.setFloat(20, stockunits.floatValue());
//					
//					
					psInsertProducts.executeUpdate();
					
				}					

			}
	    				
//	    }
			
		} catch (Exception e) {
		e.printStackTrace();
		}finally {
	          try {
	              // Close connection
	              if (psInsert != null) {
	              	psInsert.close();
	              }
	              if (psInsertProducts != null) {
		              	psInsertProducts.close();
		          }
	              if (conn != null) {
	                conn.close();
	              }
	            } catch (Exception e) {
	              e.printStackTrace();
	            }
	          }

	}
	
	public static void main(String[] args) {
		new QueryDataAndInsertToProducts();
	}
}

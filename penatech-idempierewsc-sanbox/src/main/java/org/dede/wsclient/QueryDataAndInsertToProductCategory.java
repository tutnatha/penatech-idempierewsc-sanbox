package org.dede.wsclient;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.idempiere.webservice.client.base.DataRow;
import org.idempiere.webservice.client.base.Enums.WebServiceResponseStatus;
import org.idempiere.webservice.client.base.Field;
import org.idempiere.webservice.client.net.WebServiceConnection;
import org.idempiere.webservice.client.request.QueryDataRequest;
import org.idempiere.webservice.client.response.WindowTabDataResponse;
import org.idempiere.wsclienttest.AbstractTestWS;

/* Author : Tut Natha 
 * Team	: Ajus
 *      : Dede */

//public class QueryDataAndInsertToProductCategory extends AbstractTestWS {
public class QueryDataAndInsertToProductCategory extends AbstractTestWS2 {
	@Override
	public String getWebServiceType() {
		// TODO Auto-generated method stub
		return "QueryProductCategory";
//		QueryProductCategory
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
//		data.addField("Name", "%");	//di Tab field Input
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
	    PreparedStatement psInsertCategories = null;
	    
		try {
			//kapling database
			//Open connection
	        conn = DriverManager.getConnection(jdbcUrl, username, password);
//	        psInsert = conn.prepareStatement("INSERT INTO temp_prod_category (m_product_category_id, value, name, m_product_category_parent_id) " 
//	    	        + "VALUES (?,?,?,?)");
	    	
	        String SQLGetID = "SELECT id FROM categories where id = ?";
	        PreparedStatement pstmtGetID = conn.prepareStatement(SQLGetID);
	        
	        String SQLUpdate = "UPDATE categories SET name = ?, catshowname = ? WHERE id = ?";
	        PreparedStatement pstmtUpdate = conn.prepareStatement(SQLUpdate);
	        
	        psInsert = conn.prepareStatement("INSERT INTO temp_prod_category (m_product_category_id, value, name) " + "VALUES (?,?,?)");
	        
	    	//psInsert untuk Customer pilih dulu yg not null
	    	psInsertCategories = conn.prepareStatement("INSERT INTO categories (id, name, catshowname ) VALUES(?,?,?)");
	    	        
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
	    				
	    		//Variable product category Class 
	    		Integer MProductCategoryId = 0;
	    		String Name  = "";
	    		String Value  = "";
	    		int MProductCategoryParentId = 0;
	    				
	    		//variable categories
	    		String CategoriesId = "";
	    		String CategoriesName = "";
	    		Boolean catshowname = true;
	    				
	    				
	    		for (int i = 0; i < response.getDataSet().getRowsCount(); i++) {
	    					
	    			System.out.println("Row: " + (i + 1));
	    					
					for (int j = 0; j < response.getDataSet().getRow(i).getFieldsCount(); j++) {
						Field field = response.getDataSet().getRow(i).getFields().get(j);
						System.out.println("Column: " + field.getColumn() + " = " + field.getStringValue());
						//Array List Penampung BPartner Class
						//Atau pakai kondisi per Kolom
						if (field.getColumn().equals("M_Product_Category_ID")) {
							MProductCategoryId = field.getIntValue();
							
						}else if(field.getColumn().equals("Value")) {
							Value = field.getStringValue();
							
						}else if(field.getColumn().equals("Name")) {
							Name = field.getStringValue();
							
						}else if(field.getColumn().equals("MProductCategoryParentId")) {
							MProductCategoryParentId = field.getIntValue();
							
//								}else if(field.getColumn().equals("Logo_ID")){
//	    							if(field.getIntValue().equals(null)){setInt(LogoID,0);}
//	    							else {
//	    							LogoID = field.getIntValue();
						}
					}
					
					System.out.println();
	    					
	    			//Find atau Look at database first:
	    			//pake metode SQL Query:
					pstmtGetID.setString(1, MProductCategoryId.toString());
					ResultSet rsGetID = pstmtGetID.executeQuery();
			    	
			    	if(rsGetID.next()) {
			    		String categoryID = rsGetID.getString(1);
			    		//Jika ada Lakukan Statement Update:
			    		pstmtUpdate.setString(1, Name);
			    		pstmtUpdate.setString(2, "Y");
			    		pstmtUpdate.setString(3, categoryID);
			    		
			    		int ret = pstmtUpdate.executeUpdate();
			    		
			    		if (ret == 0){System.out.println("Update Product Category... Error :D");}
			    		
			    	}else {
			    	//Jika tidak ada lakukan Insert:
	    					
	    			//Insert Row Data ke Table BParner
	    			//insertIntoBpartner				
	    			psInsert.setLong(1, MProductCategoryId);
	    			psInsert.setString(2, Value);
	    			psInsert.setString(3, Name);	//coba cek lagi kolom ini!!!
//	    			psInsert.setLong(4, MProductCategoryParentId);

	    			psInsert.executeUpdate(); //remark dulu
	    					
	    					
	    			//Insert ke Table Customer nya Unicenta
	    			psInsertCategories.setString(1, MProductCategoryId.toString());
	    			//2 searchkey
	    			psInsertCategories.setString(2,Name.toString());
	    			//3 Name
	    			psInsertCategories.setBoolean(3, catshowname);
	    			//4 maxdebt
//	    			psInsertCust.setFloat(4, maxdebt);
	    			//5 visible
//	    			psInsertCust.setBoolean(5,visible );
	    			//6 isvip
//	    			psInsertCust.setBoolean(6, isvip);
	    			//7 discount
//	    			psInsertCust.setFloat(7, discount);
	    					
	    			psInsertCategories.executeUpdate();
	    					
    			}					

    		}
	    				
	    } //else
	    			
	    } catch (Exception e) {
	    	e.printStackTrace();
		}finally {
	        try {
	              // Close connection
	              if (psInsert != null) {
	                	psInsert.close();
	              }
	              if (psInsertCategories != null) {
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
  	
  	public static void main(String args[]) {
  		new QueryDataAndInsertToProductCategory();
  	}
  	
}

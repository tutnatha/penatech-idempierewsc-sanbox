package org.dede.wsclient;

import org.idempiere.webservice.client.base.DataRow;
import org.idempiere.webservice.client.base.Enums;
import org.idempiere.webservice.client.base.Field;
import org.idempiere.webservice.client.net.WebServiceConnection;
import org.idempiere.webservice.client.request.QueryDataRequest;
import org.idempiere.webservice.client.response.WindowTabDataResponse;

import java.sql.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

//Author : Ajus on 26 June 2020
public class UpdateToPOSStockCurrent extends AbstractTestWS2{

		//connect to local database
//		String jdbcUrl = "jdbc:postgresql://localhost:5432/unicenta434";
//	    String username = "postgres";
//	    String password = "postgres";

        @Override
        public void testPerformed() {
            QueryDataRequest ws = new QueryDataRequest();
            ws.setWebServiceType(getWebServiceType());
            ws.setLogin(getLogin());
            ws.setLimit(0);
            ws.setOffset(0);
            DataRow data = new DataRow();
//		data.addField("Name", "%Store%");
//          data.addField("M_PriceList_Version_ID", "103"); //ganti ke Price Version ID
            data.addField("M_Locator_ID", "101");
            ws.setDataRow(data);

            WebServiceConnection client = getClient();

            PropertiesLoader pl = new PropertiesLoader();

            //db connection
            String jdbcUrl = pl.getPropValue("db.url");
            String username = pl.getPropValue("db.user");
            String password = pl.getPropValue("db.password");

            Connection conn = null;
            PreparedStatement psInsert = null;

            String SQL_INSERT = "INSERT INTO temp_m_storageonhand (ad_client_id, ad_org_id, created,"+
                    " createdby, isactive, m_attributesetinstance_id, m_locator_id, m_product_id, qtyonhand,"+
                    " updated, updatedby, datematerialpolicy, datelastinventory) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

            //Row Variable
            Number ad_client_id = null;
            Number ad_org_id = null;
            Timestamp created = null;  //datetime
//            Number createdby = null;
            Integer createdby = null;
            Timestamp datelastinventory = null;
            String isactive = null;
            Number m_attributesetinstance_id = null;
            Number m_locator_id = null;
            Number m_product_id = null;
            Number qtyonhand = null;
            Timestamp updated = null;
            Number updatedby = null;
            Timestamp datematerialpolicy = null;

            try {
                //kapling webservice
                WindowTabDataResponse response = client.sendRequest(ws);

                conn = DriverManager.getConnection(jdbcUrl, username, password);
                psInsert = conn.prepareStatement (SQL_INSERT);

                if (response.getStatus() == Enums.WebServiceResponseStatus.Error) {
                    System.out.println(response.getErrorMessage());
                } else {
                    System.out.println("Respons Data: " + response.toString());
                    System.out.println("Total rows: " + response.getTotalRows());
                    System.out.println("Num rows: " + response.getNumRows());
                    System.out.println("Start row: " + response.getStartRow());
                    System.out.println();

                    //Loop
                    for (int i = 0; i < response.getDataSet().getRowsCount(); i++) {
                        System.out.println("Row: " + (i + 1));

                        for (int j = 0; j < response.getDataSet().getRow(i).getFieldsCount(); j++) {
                            Field field = response.getDataSet().getRow(i).getFields().get(j);
                            System.out.println("Column: " + field.getColumn() + " = " + field.getStringValue());

                            String s = null;
                            s = field.getStringValue();
                            if (field.getColumn().equals("AD_Client_ID")) {
                                ad_client_id = NumberFormat.getInstance().parse(s);
                            }else if (field.getColumn().equals("AD_Org_ID")){
                                ad_org_id = NumberFormat.getInstance().parse(s);
                            }else if (field.getColumn().equals("Created")){

                                //Cara 1
//                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
//                                Date parsedDate;
//                                parsedDate = (Date) dateFormat.parse(s);
//                                Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
//                                created = timestamp;

                                //Cara 2
//                                Timestamp t = new Timestamp(DateUtil.provideDateFormat().parse("2019-01-14T12:00:00.000Z").getTime());
//                                created = t;

                                //Cara 3
                                java.sql.Timestamp ts = java.sql.Timestamp.valueOf( s ) ;
                                created = ts;
                            }else if (field.getColumn().equals("CreatedBy")){
//                                createdby = NumberFormat.getInstance().parse(s);
                                createdby = field.getIntValue();
                            }else if (field.getColumn().equals("DateLastInventory")){
                                //Cara 3
//                                java.sql.Timestamp ts = java.sql.Timestamp.valueOf( s ) ;
//                                datelastinventory = ts;
                            }else if (field.getColumn().equals("IsActive")){
                                isactive = s;
                            }else if (field.getColumn().equals("M_AttributeSetInstance_ID")){
                                m_attributesetinstance_id = NumberFormat.getInstance().parse(s);
                            }else if (field.getColumn().equals("M_Locator_ID")){
                                m_locator_id = NumberFormat.getInstance().parse(s);
                            }else if (field.getColumn().equals("M_Product_ID")){
                                m_product_id = NumberFormat.getInstance().parse(s);
                            }else if (field.getColumn().equals("QtyOnHand")){
                                qtyonhand = NumberFormat.getInstance().parse(s);
                            }else if (field.getColumn().equals("Updated")){
                                //Cara 3
                                java.sql.Timestamp ts = java.sql.Timestamp.valueOf( s ) ;
                                updated = ts;
                            }else if (field.getColumn().equals("UpdatedBy")){
                                updatedby = NumberFormat.getInstance().parse(s);
                            }else if (field.getColumn().equals("M_StorageOnHand_UU")){

                            }else if (field.getColumn().equals("DateMaterialPolicy")){
                                //Cara 3
                                java.sql.Timestamp ts = java.sql.Timestamp.valueOf( s ) ;
                                datematerialpolicy = ts;
                            }
                        }

                        //Insert temp table disini...
                        psInsert.setLong(1, (Long) ad_client_id);
                        psInsert.setLong(2, (Long) ad_org_id);
                        psInsert.setTimestamp(3, created);
                        psInsert.setInt(4, (Integer) createdby);  //Number createdby;
                        psInsert.setString( 5, isactive);
                        //                        psInsert.setString(5, String.valueOf(createdby));  //Number createdby;
                        psInsert.setLong(6, (Long) m_attributesetinstance_id);  //Number m_attributesetinstance_id;
                        psInsert.setLong(7, (Long) m_locator_id);               //Number m_locator_id;
                        psInsert.setLong(8, (Long) m_product_id);               //Number m_product_id;
                        psInsert.setLong(9, (Long) qtyonhand);                 //Number qtyonhand;
                        psInsert.setTimestamp(10, updated);                     //Timestamp updated;
                        psInsert.setLong(11 , (Long) updatedby);                //Number updatedby;
                        psInsert.setTimestamp(12, datematerialpolicy);               //Timestamp datematerialpolicy;
                        psInsert.setTimestamp(13, datelastinventory);            //Timestamp datelastinventory;
                        psInsert.executeUpdate();

                        //Insert Stock Current

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

//            String jdbcUrl = pl.getPropValue("db.url");
//            String username = pl.getPropValue("db.user");
//            String password = pl.getPropValue("db.password");

            //Create cursor on POS Product
            try (Connection con = DriverManager.getConnection(jdbcUrl, username, password);
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * from STOCKCURRENT")) {

	    	//Loop Cursor
            while (rs.next()) {
                System.out.println("STOCKCURRENT:" + rs.getString(1)+ " " + rs.getString(2)+ 
                		" " + rs.getString(3)+ " " + rs.getString(4));
                		
                //select Price from temp_product_price 
                String SQL = "SELECT m_locator_id, m_product_id, m_attributesetinstance_id, qtyonhand "+
                		"FROM temp_m_storageonhand "+
                		"WHERE m_product_id = ?";
                
                try (PreparedStatement pstmt = con.prepareStatement(SQL)) {
                	//i dont know it's need or not
                
                	String sM_Product_ID = rs.getString(2);
//                	pstmt.setInt(1, Integer.valueOf(sM_Product_ID));
                	pstmt.setFloat(1, Float.valueOf(sM_Product_ID));
                	ResultSet rsQ = pstmt.executeQuery();
                	
                	while (rsQ.next()) {
                		System.out.println(" temp_m_storageonhand :" + rsQ.getString(1)+ " " + rsQ.getFloat(2)+ 
                        		" " + rsQ.getString(3) + " " + rsQ.getString(4));
                		System.out.println("	Update disini:");
                		
                		String SQLUpdate = "UPDATE STOCKCURRENT SET LOCATION = ?, PRODUCT = ?, ATTRIBUTESETINSTANCE_ID = ?, UNITS = ?"
                                + " WHERE PRODUCT = ? ";
                		
//                		con.prepareStatement(SQLUpdate);
                		
                		String Location, Product, AttributeSetInstance_ID;
                		Float Units;
                		Location = String.valueOf(rsQ.getInt(1));
                		
                		//Harusnya tidak pakai decimal point
                		Product = String.valueOf(rsQ.getFloat(2));	//coba di dcek lagi
                		
                		AttributeSetInstance_ID = String.valueOf(rsQ.getInt(3));
                		Units = Float.valueOf(rsQ.getInt(4));
                		
                        try (PreparedStatement pstmtUpdate = con.prepareStatement(SQLUpdate)) {
                          con.setAutoCommit(false);
                          pstmtUpdate.setString(1, Location);
//                          pstmtUpdate.setString(2, Product);
                          pstmtUpdate.setFloat(2, Float.valueOf(Product));
                          pstmtUpdate.setString(3, AttributeSetInstance_ID);
                          pstmtUpdate.setFloat(4, Units);
                          pstmtUpdate.setString(5, sM_Product_ID);
                          
                          int ret = pstmtUpdate.executeUpdate();
                          
                          if (ret == 0){
                        	  String SQLInsert = "INSERT INTO STOCKCURRENT (LOCATION, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS) VALUES (?,?,?,?)";

	                          	try (PreparedStatement pstmtInsert = con.prepareStatement(SQLInsert)) {
	                                  con.setAutoCommit(false);
	                                  pstmtInsert.setString(1, Location);
	                                  pstmtInsert.setString(2, Product);
	                                  pstmtInsert.setString(3, AttributeSetInstance_ID);
	                                  pstmtInsert.setFloat(4, Units);
	                                  
	                                  pstmtInsert.executeUpdate();
	                                  
	                                  con.commit();
	                                  System.out.println("	Insert disini:");
                                }
                          }
                          
                          con.commit();
                        }catch (SQLException ex) {

                        	String SQLInsert = "INSERT INTO STOCKCURRENT (LOCATION, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS) VALUES (?,?,?,?)";

                        	try (PreparedStatement pstmtInsert = con.prepareStatement(SQLInsert)) {
                                con.setAutoCommit(false);
                                pstmtInsert.setString(1, Location);					//String varchar
//                                pstmtInsert.setString(2, Product);				//String varchar
                                pstmtInsert.setFloat(2, Float.valueOf(Product));	
                                pstmtInsert.setString(3, AttributeSetInstance_ID);	//String varchar
                                pstmtInsert.setFloat(4, Units);						//Float
                                
                                pstmtInsert.executeUpdate();
                                
                                con.commit();
                                System.out.println("	Insert disini:");
                              }
                        	
                        	
                            if (con != null) {
                                try {
//                                    con.rollback();
                                	con.commit();
                                } catch (SQLException ex1) {
                                    Logger lgr = Logger.getLogger(UpdateToPOSProductPrice.class.getName());
                                    lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                                }
                            }

                            Logger lgr = Logger.getLogger(UpdateToPOSProductPrice.class.getName());
                            lgr.log(Level.SEVERE, ex.getMessage(), ex);                		
                        }
                        
                	}

                }
                

            }

        } catch (SQLException ex) {
        
            Logger lgr = Logger.getLogger(UpdateToPOSProductPrice.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
	    
	  }

    @Override
    public String getWebServiceType() {
        return "QueryStockOnHand";
    }

    public static void main(String[] args) {
		// TODO Auto-generated method stub
  		new UpdateToPOSStockCurrent();
	}

}

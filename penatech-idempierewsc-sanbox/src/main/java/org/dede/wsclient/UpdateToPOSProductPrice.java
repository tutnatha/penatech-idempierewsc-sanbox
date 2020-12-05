package org.dede.wsclient;

//import java.beans.Statement;
import org.idempiere.webservice.client.base.DataRow;
import org.idempiere.webservice.client.base.Enums;
import org.idempiere.webservice.client.base.Field;
import org.idempiere.webservice.client.net.WebServiceConnection;
import org.idempiere.webservice.client.request.QueryDataRequest;
import org.idempiere.webservice.client.response.WindowTabDataResponse;

import java.math.BigInteger;
import java.sql.*;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateToPOSProductPrice extends AbstractTestWS2 {
    @Override
    public void testPerformed() {
        QueryDataRequest ws = new QueryDataRequest();
        ws.setWebServiceType(getWebServiceType());
        ws.setLogin(getLogin());
        ws.setLimit(0);
        ws.setOffset(0);
        DataRow data = new DataRow();
//		data.addField("Name", "%Store%");
        data.addField("M_PriceList_Version_ID", "103"); //ganti ke Price Version ID
        ws.setDataRow(data);

        WebServiceConnection client = getClient();

		//connect to local database
		//ingat buatkan properties file
//		String jdbcUrl = "jdbc:postgresql://localhost:5432/unicenta434";
//	    String username = "postgres";
//	    String password = "postgres";

        PropertiesLoader pl = new PropertiesLoader();

        String jdbcUrl = pl.getPropValue("db.url");
        String username = pl.getPropValue("db.user");
        String password = pl.getPropValue("db.password");

        //Insert into temp_m_productprice
        String SQL_INSERT = "INSERT INTO temp_m_productprice (m_pricelist_version_id, m_product_id, ad_client_id,"+
                " ad_org_id, isactive, created, createdby, updated, updatedby, pricelist, pricestd, pricelimit, m_product_price_id)" +
                " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Connection conn = null;
        PreparedStatement psInsert = null;

        //Row Variable
        Number m_pricelist_version_id = null;
        Number m_product_id = null;
        Number ad_client_id = null;
        Number ad_org_id = null;
        String isactive = null;
        Timestamp created = null;
        Number createdby = null;
        Timestamp updated = null;
        Number updatedby = null;
        Number pricelist = null;
        Number pricestd = null;
        Number pricelimit = null;
        Number m_product_price_id = null;

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

                    //Row Variable

                    for (int j = 0; j < response.getDataSet().getRow(i).getFieldsCount(); j++) {
                        Field field = response.getDataSet().getRow(i).getFields().get(j);
                        System.out.println("Column: " + field.getColumn() + " = " + field.getStringValue());

                        String s = null;
                        s = field.getStringValue();
                        if (field.getColumn().equals("AD_Client_ID")) {
                            ad_client_id = NumberFormat.getInstance().parse(s);
                        }else if (field.getColumn().equals("AD_Org_ID")) {
                            ad_org_id = NumberFormat.getInstance().parse(s);
                        }else if (field.getColumn().equals("IsActive")) {
                            isactive = s;
                        }else if (field.getColumn().equals("Created")) {
                            java.sql.Timestamp ts = java.sql.Timestamp.valueOf( s ) ;
                            created = ts;
                        }else if (field.getColumn().equals("CreatedBy")) {
                            createdby = NumberFormat.getInstance().parse(s);
                        }else if (field.getColumn().equals("Updated")) {
                            java.sql.Timestamp ts = java.sql.Timestamp.valueOf( s ) ;
                            updated = ts;
                        }else if (field.getColumn().equals("UpdatedBy")) {
                            updatedby = NumberFormat.getInstance().parse(s);
                        }else if (field.getColumn().equals("M_Product_ID")) {
                            m_product_id = NumberFormat.getInstance().parse(s);
                        }else if (field.getColumn().equals("M_PriceList_Version_ID")) {
                            m_pricelist_version_id = NumberFormat.getInstance().parse(s);
                        }else if (field.getColumn().equals("PriceList")) {
//                            pricelist = NumberFormat.getInstance().parse(s);
                            pricelist = Double.parseDouble(s);
                        }else if (field.getColumn().equals("PriceStd")) {
//                            pricestd = NumberFormat.getInstance().parse(s);
                            pricestd = Double.parseDouble(s);
                        }else if (field.getColumn().equals("PriceLimit")) {
//                            pricelimit = NumberFormat.getInstance().parse(s);
                            pricelimit = Double.parseDouble(s);
                        }else if (field.getColumn().equals("M_ProductPrice_UU")) {

                        }else if (field.getColumn().equals("M_ProductPrice_ID")) {
                            m_product_price_id = NumberFormat.getInstance().parse(s);
                        }

                    }

                    //Insert temp table disini...
                    psInsert.setLong(1, (Long) m_pricelist_version_id);
                    psInsert.setLong(2, (Long) m_product_id);
                    psInsert.setLong(3, (Long) ad_client_id);
                    psInsert.setLong(4, (Long) ad_org_id);
                    psInsert.setString(5, isactive);
                    psInsert.setTimestamp(6, created);
                    psInsert.setLong(7, (Long) createdby);
                    psInsert.setTimestamp(8, updated);
                    psInsert.setLong(9, (Long) updatedby);

//                    psInsert.setLong(10, (Long)  pricelist);
                    psInsert.setDouble(10, (Double)  pricelist);
                    //nilainya bisa bulat atau pecahan
                    String s = pricestd.toString();

//                    if(isInt(s)){
//                        psInsert.setInt(11, (Integer)  pricestd);
//                    }else
//                    if(isDouble(s)){
                        psInsert.setDouble(11, (Double)  pricestd);
//                    }else if(isFloat(s)){
//                        psInsert.setFloat(11, (Float)  pricestd);
//                    }else if(isLong(s)){
//                       psInsert.setLong(11, (Long)  pricestd);
//                    }

//                    Float.parseFloat(s);
//                    Double.parseDouble(s);
//                    Long.parseLong(s);
                    //new BigInteger(s);

//                    psInsert.setFloat(12, (Float)  pricelimit);
                    psInsert.setDouble(12, (Double)  pricelimit);

                    psInsert.setLong(13, (Long)   m_product_price_id);
                    psInsert.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Connection con = DriverManager.getConnection(jdbcUrl, username, password);
             //Create cursor on POS Product
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * from PRODUCTS")) {
	    	//Loop Cursor
            while (rs.next()) {
                System.out.println("PRODUCTS:" + rs.getString(1)+ " " + rs.getString(2)+ 
                		" " + rs.getString(3)+ " " + rs.getString(4)+ " " + 
                		rs.getString(5) + " " + rs.getString(6));
                
                //select Price from temp_product_price 
                String SQL = "SELECT pricelist, pricestd, pricelimit "+
                		"FROM temp_m_productprice "+
                		"WHERE m_product_id = ?";
                
                try (PreparedStatement pstmt = con.prepareStatement(SQL)) {
                	//i dont know it's need or not
                
                	String sMProductID = rs.getString(1);
                	pstmt.setDouble(1, Double.valueOf(sMProductID));
                	ResultSet rsQ = pstmt.executeQuery();
                	
                	while (rsQ.next()) {
                		System.out.println(" temp_m_productprice :" + rsQ.getString(1)+ " " + rsQ.getString(2)+ 
                        		" " + rsQ.getString(3));
                		System.out.println("	Update disini:");
                		
                		String SQLUpdate = "UPDATE PRODUCTS SET PRICEBUY = ?, PRICESELL = ? "
                                + " WHERE ID=? ";
                		
//                		con.prepareStatement(SQLUpdate);
                		
                		Float BuyPrice, SellPrice;
                		BuyPrice = Float.valueOf(rsQ.getString(1));
                		SellPrice = Float.valueOf(rsQ.getString(1));
                		
                        try (PreparedStatement pstmtUpdate = con.prepareStatement(SQLUpdate)) {
                          con.setAutoCommit(false);
                          pstmtUpdate.setFloat(1, BuyPrice);
                          pstmtUpdate.setFloat(2, SellPrice);
                          pstmtUpdate.setString(3, sMProductID);                          
                          
                          pstmtUpdate.executeUpdate();
                          
                          con.commit();
                        }catch (SQLException ex) {

                            if (con != null) {
                                try {
                                    con.rollback();
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
        return "QueryProductsPrice";
    }



    public static void main(String args[]) {
		UpdateToPOSProductPrice run = new UpdateToPOSProductPrice();
	}

    public static boolean isDouble(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isFloat(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Float.parseFloat(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isInt(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isLong(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Long.parseLong(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

//    public static boolean isBigInteger(String strNum) {
//        if (strNum == null) {
//            return false;
//        }
//        try {
//            double d = BigInteger.parseBigInteger(strNum);
//        } catch (NumberFormatException nfe) {
//            return false;
//        }
//        return true;
//    }
}

package org.dede.wsclient;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

import org.idempiere.webservice.client.base.DataRow;
import org.idempiere.webservice.client.base.Enums.WebServiceResponseStatus;
import org.idempiere.webservice.client.net.WebServiceConnection;
import org.idempiere.webservice.client.request.CreateDataRequest;
import org.idempiere.webservice.client.response.StandardResponse;
import org.idempiere.wsclienttest.AbstractTestWS;

public class PushDataSalesOrder extends AbstractTestWS{

	public PushDataSalesOrder() {
		
	}

	@Override
	public String getWebServiceType() {

		return "CreatePOSOrder";
	}

	@Override
	public void testPerformed() {
		CreateDataRequest createData = new CreateDataRequest();
		createData.setLogin(getLogin());
		createData.setWebServiceType(getWebServiceType());

		//Star
		//Connect to Database
//		String jdbcUrl = "jdbc:postgresql://localhost:5432/unicenta434";
//	    String username = "postgres";
//	    String password = "postgres";

		//Load properties
		PropertiesLoader pl = new PropertiesLoader();

		//Ganti cpirit...
		String jdbcUrl = pl.getPropValue("db.url");
		String username = pl.getPropValue("db.user");
		String password = pl.getPropValue("db.password");

	    Connection conn = null;
	    PreparedStatement psQuery = null;
	    
	    String SQL = "SELECT id, "+
	    		"tickettype, "+
	    		"ticketid, "+
	    		"person, "+
	    		"status FROM tickets ";
	    
	    //Create Cursor
	    //Select * frpm Ticket
		//on special date range
	    try {
	    	Connection con = DriverManager.getConnection(jdbcUrl, username, password);
	    	PreparedStatement pstmt = con.prepareStatement(SQL);
	    	
	    	ResultSet rs = pstmt.executeQuery();

	    	//Loop
	    	while(rs.next()) {
	    		System.out.println("Row : "+ rs.getString(1) + " " + 
	    				rs.getString(2) + " " +
	    				rs.getString(3) + " " +
	    				rs.getString(4));
	    		
	    		//Ubah ke nama kolom
	    		DataRow data = new DataRow();
	    		data.addField("M_PriceList_ID", 101);
	    		data.addField("M_Warehouse_ID", 103);
	    		data.addField("PriorityRule", '5');
	    		data.addField("DeliveryRule", 'A');
	    		data.addField("C_PaymentTerm_ID", 105);
	    		data.addField("C_Currency_ID", 100);
	    		data.addField("C_BPartner_Location_ID", 108);
	    		data.addField("C_BPartner_ID", 112);
	    		
//	    		LocalDate created = LocalDate.now();
	    		Date date = new Date(0);
	    		Timestamp timestamp = new Timestamp(date.getDate());
	    		data.addField("DateAcct", timestamp);
	    		
	    		data.addField("DateOrdered", timestamp);
	    		data.addField("C_DocType_ID", 135);
	    		data.addField("C_DocTypeTarget_ID", 135);
	    		data.addField("UpdatedBy", 100);
	    		data.addField("CreatedBy", 100);
	    		data.addField("AD_Client_ID", 11);
	    		data.addField("AD_Org_ID", 11);
	    		
	    		
	    		createData.setDataRow(data);

	    		//insert detail 
//	    		new PushDataSalesOrderLine(aCOrderParent);		//spt ini logicnya
	    		
	    		WebServiceConnection client = getClient();
	    		
	    		try {
	    			StandardResponse response = client.sendRequest(createData);
	    			
	    			if (response.getStatus() == WebServiceResponseStatus.Error) {
	    				System.out.println(response.getErrorMessage());
	    			} else {

	    				System.out.println("RecordID: " + response.getRecordID());
	    				System.out.println();

	    				for (int i = 0; i < response.getOutputFields().getFieldsCount(); i++) {
	    					System.out.println("Column" + (i + 1) + ": " + response.getOutputFields().getField(i).getColumn() + " = " + response.getOutputFields().getField(i).getValue());
	    				}
	    				System.out.println();
	    			}
	    			
	    		}catch (Exception e) {
	    			e.printStackTrace();
	    		}

	    	}
	    	
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
		           // Close connection
		           if (psQuery != null) {
		        	   psQuery.close();
		
		           }
	          	} catch (Exception e) {
	              e.printStackTrace();
	          	}
		}
	    
		//Loop Result Set
		//Then Push Row to Server
		
		
		//The End
		
	}
	
	public static void main(String[] args) {		
		new PushDataSalesOrder();
	}

	public void insertRecordID(ArrayList<Integer> aRecordID,
							   Connection conn){

		String sqlInsert = "insert into temp_recordid (recordid, isprocessed) values (?,?);";
		PreparedStatement psInsert = null;
		try {
			psInsert = conn.prepareStatement(sqlInsert);

			//Start Loop
			int n = aRecordID.size();
//			for (int i = 1; i <= n; i++) {
			for (int i = 0; i < n; i++) {
				psInsert.setInt(1, aRecordID.get(i));    //aRecordID
				psInsert.setString(2, "N");            //Processed
				psInsert.executeUpdate();
			}
			//end loop
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}	
}

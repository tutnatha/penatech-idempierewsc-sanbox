package org.dede.wsclient;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.idempiere.webservice.client.base.DataRow;
import org.idempiere.webservice.client.base.Enums.WebServiceResponseStatus;
import org.idempiere.webservice.client.net.WebServiceConnection;
import org.idempiere.webservice.client.request.CreateDataRequest;
import org.idempiere.webservice.client.response.StandardResponse;
import org.idempiere.wsclienttest.AbstractTestWS;

public class PushDataSalesOrderLine extends AbstractTestWS{
	
	Number COrderParent; 
	
	public Number getCOrderParent() {
		return COrderParent;
	}

	public void setCOrderParent(Number cOrderParent) {
		COrderParent = cOrderParent;
	}

	public PushDataSalesOrderLine(Number aCOrderParent) {
//		super();
		this.COrderParent = aCOrderParent;
		
	}

	public PushDataSalesOrderLine() {
		
	}
	
	@Override
	public String getWebServiceType() {
		
		return "CreatePOSOrderLine";
	}

	@Override
	public void testPerformed() {
		CreateDataRequest createData = new CreateDataRequest();
		createData.setLogin(getLogin());
		createData.setWebServiceType(getWebServiceType());
		
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

	    String SQL = "SELECT ticket, "+
	    		"line, "+
	    		"product, "+
	    		"units, "+
	    		"price, "+
	    		"taxid FROM ticketlines ";
	    
	    //Create Cursor
	    //Select * frpm Ticket
		//on special date range
	    try {
	    	Connection con = DriverManager.getConnection(jdbcUrl, username, password);
	    	PreparedStatement pstmt = con.prepareStatement(SQL);
	    	
	    	ResultSet rs = pstmt.executeQuery();
	    	
	    	while(rs.next()) {
	    		System.out.println("Row : "+ rs.getString(1) + " " + 
	    				rs.getInt(2) + " " +
	    				rs.getString(3) + " " +
	    				rs.getFloat(4) + " " +
	    				rs.getFloat(5) + " " +
	    				rs.getString(6));
	    		
	    		//Ubah ke nama kolom
	    		DataRow data = new DataRow();
//	    		data.addField("C_OrderLine_ID", 124);		//otomatis
	    		data.addField("M_Warehouse_ID", 103);
	    		data.addField("IsActive", 'Y');
	    		data.addField("PriceActual", 21.59);
	    		data.addField("C_Currency_ID", 102);
	    		data.addField("C_Order_ID", 1000000);		//ambil dari c_order parent
	    													//COrderParent
	    		data.addField("C_UOM_ID", 100);
	    		data.addField("QtyOrdered", 10);
	    		
//	    		LocalDate created = LocalDate.now();
	    		Date date = new Date(0);
	    		Timestamp timestamp = new Timestamp(date.getDate());
	    		data.addField("Created", timestamp);
	    		
	    		data.addField("DateOrdered", timestamp);
	    		data.addField("QtyReserved", 0);
	    		data.addField("QtyDelivered", 10);
	    		data.addField("UpdatedBy", 100);
	    		data.addField("CreatedBy", 100);
	    		data.addField("AD_Client_ID", 11);
	    		data.addField("AD_Org_ID", 11);
	    		data.addField("Updated", timestamp);		//hasil database beda
	    		data.addField("Line", 10);
	    		data.addField("PriceList", 22.73);
	    		data.addField("QtyInvoiced", 10);			//input column QtyInvoiced does not exist
	    		data.addField("PriceLimit", 20.46);
	    		data.addField("LineNetAmt", 215.9);
	    		data.addField("FreightAmt", 0);
	    		data.addField("C_Tax_ID", 105);
	    		data.addField("IsDescription", 'N');
//Remark Natha:ß
	    		//	    		data.addField("Processed", 'Y');        //input column Processed not allowed
	    		data.addField("QtyEntered", 10);
	    		data.addField("PriceEntered", 21.59);
	    		data.addField("QtyLostSales", 0);
	    		data.addField("M_Product_ID", rs.getString(3));
	    		
	    		createData.setDataRow(data);

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
		new PushDataSalesOrderLine();

	}

}

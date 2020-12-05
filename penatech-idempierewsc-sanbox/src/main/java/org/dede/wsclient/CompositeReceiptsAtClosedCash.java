package org.dede.wsclient;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.idempiere.webservice.client.base.DataRow;
import org.idempiere.webservice.client.base.Enums.DocAction;
import org.idempiere.webservice.client.base.Enums.WebServiceResponseStatus;
import org.idempiere.webservice.client.net.WebServiceConnection;
import org.idempiere.webservice.client.request.CompositeOperationRequest;
import org.idempiere.webservice.client.request.CreateDataRequest;
import org.idempiere.webservice.client.request.SetDocActionRequest;
import org.idempiere.webservice.client.response.CompositeResponse;
import org.idempiere.wsclienttest.AbstractTestWS;

public class CompositeReceiptsAtClosedCash extends AbstractCompositeWS {
	
	private Timestamp date;
	private String ReceiptsID;
	
	public CompositeReceiptsAtClosedCash() {
//		super();
//		ReceiptsID = receiptsID;
	}

	
	@Override
	public String getWebServiceType() {
		return "CompositeSalesOrderAndLine";
	}

	@Override
	public void testPerformed() {
		
	}	
	
	public void execFunction(String receiptsID) {	
		
		//Load properties
		PropertiesLoader pl = new PropertiesLoader();
		
		//Connect to Database
//			String jdbcUrl = "jdbc:postgresql://localhost:5432/unicenta434";
//		    String username = "postgres";
//		    String password = "postgres";
		
		String jdbcUrl = pl.getPropValue("db.url");
		String username = pl.getPropValue("db.user");
		String password = pl.getPropValue("db.password");
		    
		    Connection conn = null;
		    PreparedStatement psQuery = null;
		    
		    //SQL GET ticket.id
		    //yg belum terkirimke Idempiere
		    String SQLGetID = "select id \n" + 
		    		"from tickets t \n" + 
		    		"where ticketid = (select max(ticketid )from tickets t2 )";
		    
		    try {
		    	Connection con = DriverManager.getConnection(jdbcUrl, username, password);
		    	PreparedStatement pstmtGetID = con.prepareStatement(SQLGetID);
		    	ResultSet rsGetID = pstmtGetID.executeQuery();
		    	
		    	if(rsGetID.next()) {
		    		receiptsID = rsGetID.getString(1);
		    	}
		    	
		    }catch (Exception e) {
				e.printStackTrace();
			}
		    
		    //Query per range tanggal    
		    String SQL = "SELECT t.id, "+
		    		"t.tickettype, "+
		    		"t.ticketid, "+
		    		"t.person, "+
		    		"t.customer, "+
		    		"t.status, "+
		    		"t.hostsync, "+
		    		"t.fiscalnumber, "+
		    		"t.cuponno, "+
		    		"t.fiscalserial, "+
		    		"t.notes, "+
		    		"t.ticketrefundnumber, "+	    		
		    		"r.datenew FROM tickets t " +
		    		"inner join receipts r on r.id = t.id  "+
		    		"where t.id = ?";
		    
		  //Create Cursor
		    //Select * frpm Ticket
			//on special date range
		    
		    CompositeOperationRequest compositeOperation = new CompositeOperationRequest();
			compositeOperation.setLogin(getLogin());
			compositeOperation.setWebServiceType(getWebServiceType());

			try {
		    	Connection con = DriverManager.getConnection(jdbcUrl, username, password);
		    	PreparedStatement pstmt = con.prepareStatement(SQL);
		    	pstmt.setString(1, receiptsID);				//this.ReceiptsID belum dicoba seperti ini
		    	ResultSet rs = pstmt.executeQuery();
		    	
		    	while(rs.next()) {
		    		System.out.println("Row : "+ rs.getString(1) + "-"  + 
		    				rs.getString(2) + "-" +
		    				rs.getString(3) + "-" +
		    				rs.getString(4) + "-" +
		    				rs.getString(5) + "-" + 
		    				rs.getString(6) + "-" +
		    				rs.getString(7) + "-" +
		    				rs.getString(8) + "-" +
		    				rs.getString(9) + "-" +
		    				rs.getString(10) + "-" +
		    				rs.getString(11) + "-" +
		    				rs.getString(12) + "-" +
		    				rs.getString(13));
		    		
		    		//Set Sales atau Ticket header 
		    		CreateDataRequest createSalesOrder = new CreateDataRequest();
		    		createSalesOrder.setWebServiceType("CreatePOSOrder");
		    		DataRow data = new DataRow();
		    		data.addField("M_PriceList_ID", "101");
		    		data.addField("M_Warehouse_ID", "103");
		    		data.addField("PriorityRule", '5');
		    		data.addField("DeliveryRule", 'A');
		    		data.addField("C_PaymentTerm_ID", "105");
		    		data.addField("C_Currency_ID", "100");
		    		data.addField("C_BPartner_Location_ID", "108");
		    		data.addField("C_BPartner_ID", rs.getString(5));	//112
		    		Date date = new Date(0);
		    		Timestamp timestamp = new Timestamp(date.getTime());
		    	    this.date = timestamp;
		    	    
		    	    String sDate = rs.getString(13);
		    		data.addField("DateAcct", sDate);		//timestamp //"2020-07-01 00:00:00"
		    		
		    		data.addField("DateOrdered", sDate);	//timestamp
		    		data.addField("C_DocType_ID", "135");
		    		data.addField("C_DocTypeTarget_ID", "135");
		    		data.addField("UpdatedBy", "100");
		    		data.addField("CreatedBy", "100");
		    		data.addField("AD_Client_ID", "11");
		    		data.addField("AD_Org_ID", "11");
		    		
		    		createSalesOrder.setDataRow(data);

		    		System.out.println("	Header RecordID : " + createSalesOrder.getRecordIDVariable());
		    		
		    		compositeOperation.addOperation(createSalesOrder);
		    		
		    		System.out.println("	OperationsCount : " + compositeOperation.getOperationsCount());
		    		//Loop TicketLine
		    		//Query TicketLine per key ID
		    	    String SQLTicketID = "SELECT ticket t, "+		//1
		    	    		"line l, "+								//2
		    	    		"product p, "+							//3
		    	    		"units u, "+							//4
		    	    		"price pr, "+							//5
		    	    		"taxid tx FROM ticketlines tl " +		//6
		    	    		"where ticket = ?";
		    	    
		    	    PreparedStatement pstmtTicketLine = con.prepareStatement(SQLTicketID);
		    	    String ticket = rs.getString(1);
		    	    pstmtTicketLine.setString(1, ticket);
			    	ResultSet rsTicketLine = pstmtTicketLine.executeQuery();

			    	int Line = 0;

			    	while(rsTicketLine.next()) {
			    		System.out.println("	Row Line: "+ rsTicketLine.getString(1) + "-"  + 
			    				rsTicketLine.getString(2) + "-" +
			    				rsTicketLine.getString(3) + "-" +
			    				rsTicketLine.getString(4) + "-" +
			    				rsTicketLine.getString(5) + "-" +
			    				rsTicketLine.getString(6));
			    		
			    		//Select Price List from Product master
			    		String SQLPrice = "SELECT pricebuy, pricesell " +
			    				"FROM PRODUCTS " +
			    				"WHERE id = ?";
			    		
			    		PreparedStatement pstmtSQLPrice = con.prepareStatement(SQLPrice);
			    		String sID = rsTicketLine.getString(1);
			    	    pstmtSQLPrice.setString(1, sID);
				    	ResultSet rsSQLPrice = pstmtSQLPrice.executeQuery();
				    	if(rsSQLPrice.next()) {
				    		Float fPriceBuy = rsSQLPrice.getFloat(1);
				    		Float fPriceSell = rsSQLPrice.getFloat(2);
				    	}
			    		
			    		//Set data Line
			    		CreateDataRequest createSalesOrderLine = new CreateDataRequest();
			    		
			    		createSalesOrderLine.setWebServiceType("CreatePOSOrderLine");
			    		
			    		DataRow dataLine = new DataRow();
			    		dataLine.addField("M_Warehouse_ID", "103");
			    		dataLine.addField("IsActive", 'Y');
			    		
			    		String sPrice = rsTicketLine.getString(5);
			    		dataLine.addField("PriceActual", sPrice);
			    		
			    		dataLine.addField("C_Currency_ID", "102");
			    		dataLine.addField("C_Order_ID", "@C_Order.C_Order_ID");
			    		dataLine.addField("C_UOM_ID", "100");
			    		
			    		String units = rsTicketLine.getString(4); 
			    		dataLine.addField("QtyOrdered", units);		//"10"
			    		
			    		dataLine.addField("Created", sDate);		//timestamp		//"2020-07-01 00:00:00"
			    		dataLine.addField("DateOrdered", sDate);	//timestamp		//"2020-07-01 00:00:00"
			    		
			    		
			    		dataLine.addField("QtyReserved", "0");
			    		
			    		dataLine.addField("QtyDelivered", units);
			    		
			    		dataLine.addField("UpdatedBy", "100");
			    		dataLine.addField("CreatedBy", "100");
			    		dataLine.addField("AD_Client_ID", "11");
			    		dataLine.addField("AD_Org_ID", "11");
			    		dataLine.addField("Updated", sDate);		//timestamp		//"2020-07-01 00:00:00"
			    		
			    		Line = Line+10;
			    		dataLine.addField("Line", Line);			//"30"	//10
			    		
			    		dataLine.addField("PriceList", sPrice);		//"10.00"
			    		
			    		dataLine.addField("QtyInvoiced", units);	//"10"
			    		
			    		dataLine.addField("PriceLimit", sPrice);			//"20.46"
			    		
			    		//unit * price
			    		Float LineNetAmt = (float)0;
			    		Float fPrice = Float.valueOf(sPrice);
			    		Float fQty = Float.valueOf(units);
			    		LineNetAmt = fPrice * fQty;
			    		String sLineNetAmt = LineNetAmt.toString();
			    		dataLine.addField("LineNetAmt", "215.9");
			    		
			    		dataLine.addField("FreightAmt", "0");
			    		
			    		dataLine.addField("C_Tax_ID", "105");
			    		
			    		dataLine.addField("IsDescription", 'N');
			    		
			    		units = rsTicketLine.getString(4); 
			    		dataLine.addField("QtyEntered", units);
			    		
			    		dataLine.addField("PriceEntered", sPrice);	//"21.59"
			    		dataLine.addField("QtyLostSales", "0");		    		
			    		
			    		String product = rsTicketLine.getString(3); 
			    		dataLine.addField("M_Product_ID", product);	//128
			    		
			    		createSalesOrderLine.setDataRow(dataLine);

			    		compositeOperation.addOperation(createSalesOrderLine);
			    		
			    		System.out.println("		OperationsCount : " + compositeOperation.getOperationsCount());
			    	}
			    			    	
		    	}
		    	//DOc Action
		    	SetDocActionRequest docAction = new SetDocActionRequest();
				docAction.setDocAction(DocAction.Complete);
				docAction.setWebServiceType("DocActionSalesOrder01");		

				docAction.setRecordIDVariable("@C_Order.C_Order_ID");
//Coa Hardcode
//				docAction.setRecordID(1000003);	//tidak ngefek

				//Tamahkan lagi addOperation nya
				compositeOperation.addOperation(docAction);

		    	WebServiceConnection client = getClient();
		    	
		    	//Send data to Idempiere Server
		    	try {
					CompositeResponse response = client.sendRequest(compositeOperation);

					if (response.getStatus() == WebServiceResponseStatus.Error) {
						System.out.println(response.getErrorMessage());
					} else {
						for (int i = 0; i < response.getResponsesCount(); i++) {
							if (response.getResponse(i).getStatus() == WebServiceResponseStatus.Error) {
								System.out.println(response.getResponse(i).getErrorMessage());
							} else {
								System.out.println("Response: " + response.getResponse(i).getWebServiceResponseModel());
								System.out.println("Request: " + response.getResponse(i).getWebServiceType());
							}
							System.out.println();
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
		    	
		    }catch (Exception e) {
				e.printStackTrace();
			}
				    
		}

	public static void main(String[] args) {
//		String ReceiptsID = "f7ea19d4-e82d-4814-a3e6-80b4f9f9e78c";      //input dengan ticketID transaksi
		String ReceiptsID = "729b4f58-8c27-43fb-a305-d002ee03509f";
		CompositeReceiptsAtClosedCash c = new CompositeReceiptsAtClosedCash();
		c.execFunction(ReceiptsID);
	}
		

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}


	public String getReceiptsID() {
		return ReceiptsID;
	}


	public void setTicketID(String receiptsID) {
		ReceiptsID = receiptsID;
	}
	
	//Getter and Setter
	
	
	}

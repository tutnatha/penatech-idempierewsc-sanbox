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

public class CompositeSalesOrderAndOrderLine extends AbstractCompositeWS {

	Number COrderParent;
	private Timestamp date;
	
	public Number getCOrderParent() {
		return COrderParent;
	}
	
	public void setCOrderParent(Number cOrderParent) {
		COrderParent = cOrderParent;
	}
	
//	public CompositeSalesOrderAndOrderLine(Number aCOrderParent) {
	public CompositeSalesOrderAndOrderLine() {	
//	super();
	
//		this.COrderParent = aCOrderParent;
		
	}
	
	public static void main(String[] args) {
		new CompositeSalesOrderAndOrderLine();	 
	}

	@Override
	public String getWebServiceType() {
			return "CompositeSalesOrderAndLine";
	}

	@Override
	public void testPerformed() {

		CompositeOperationRequest compositeOperation = new CompositeOperationRequest();
		compositeOperation.setLogin(getLogin());
		compositeOperation.setWebServiceType(getWebServiceType());

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
		data.addField("C_BPartner_ID", 112);
		
		Date date = new Date(0);
		Timestamp timestamp = new Timestamp(date.getTime());
	    this.date = timestamp;
		data.addField("DateAcct", "2020-06-25 00:00:00");		//timestamp
		
		data.addField("DateOrdered", "2020-06-25 00:00:00");	//timestamp
		data.addField("C_DocType_ID", "135");
		data.addField("C_DocTypeTarget_ID", "135");
		data.addField("UpdatedBy", "100");
		data.addField("CreatedBy", "100");
		data.addField("AD_Client_ID", "11");
		data.addField("AD_Org_ID", "11");
		
		createSalesOrder.setDataRow(data);

		CreateDataRequest createSalesOrderLine = new CreateDataRequest();
		
		createSalesOrderLine.setWebServiceType("CreatePOSOrderLine");
		
		DataRow dataLine = new DataRow();
		dataLine.addField("M_Warehouse_ID", "103");
		dataLine.addField("IsActive", 'Y');
		dataLine.addField("PriceActual", "21.59");
		dataLine.addField("C_Currency_ID", "102");
		dataLine.addField("C_Order_ID", "@C_Order.C_Order_ID");
		dataLine.addField("C_UOM_ID", "100");
		dataLine.addField("QtyOrdered", "10");
		dataLine.addField("Created", "2020-06-25 00:00:00");		//timestamp
		dataLine.addField("DateOrdered", "2020-06-25 00:00:00");	//timestamp
		dataLine.addField("QtyReserved", "0");
		dataLine.addField("QtyDelivered", "10");
		dataLine.addField("UpdatedBy", "100");
		dataLine.addField("CreatedBy", "100");
		dataLine.addField("AD_Client_ID", "11");
		dataLine.addField("AD_Org_ID", "11");
		dataLine.addField("Updated", "2020-06-25 00:00:00");		//timestamp
		dataLine.addField("Line", "30");				//10
		dataLine.addField("PriceList", "10.00");
		dataLine.addField("QtyInvoiced", "10");
		dataLine.addField("PriceLimit", "20.46");
		dataLine.addField("LineNetAmt", "215.9");
		dataLine.addField("FreightAmt", "0");
		dataLine.addField("C_Tax_ID", "105");
		dataLine.addField("IsDescription", 'N');
		dataLine.addField("QtyEntered", "10");
		dataLine.addField("PriceEntered", "21.59");
		dataLine.addField("QtyLostSales", "0");
		dataLine.addField("M_Product_ID", "146");	//128
		
		createSalesOrderLine.setDataRow(dataLine);

		SetDocActionRequest docAction = new SetDocActionRequest();
		docAction.setDocAction(DocAction.Complete);
		docAction.setWebServiceType("DocActionSalesOrder01");
		docAction.setRecordIDVariable("@C_Order.C_Order_ID");

		compositeOperation.addOperation(createSalesOrder);
		compositeOperation.addOperation(createSalesOrderLine);
//Remark Natha:
				compositeOperation.addOperation(docAction);     //seharusnya pake ini but it's magig

		WebServiceConnection client = getClient();

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

	
		
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	
}

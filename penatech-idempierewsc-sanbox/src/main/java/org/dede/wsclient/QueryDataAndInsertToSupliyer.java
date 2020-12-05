package org.dede.wsclient;

import org.idempiere.webservice.client.base.DataRow;
import org.idempiere.webservice.client.base.Enums;
import org.idempiere.webservice.client.base.Field;
import org.idempiere.webservice.client.exceptions.WebServiceException;
import org.idempiere.webservice.client.net.WebServiceConnection;
import org.idempiere.webservice.client.request.QueryDataRequest;
import org.idempiere.webservice.client.response.WindowTabDataResponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QueryDataAndInsertToSupliyer extends AbstractTestWS2 {
    @Override
    public String getWebServiceType() {
        return "QueryVendor";
    }

    @Override
    public void testPerformed() {
        QueryDataRequest ws = new QueryDataRequest();
        ws.setWebServiceType(getWebServiceType());
        ws.setLogin(getLogin());
        ws.setLimit(3);
        ws.setOffset(3);

        DataRow data = new DataRow();
        data.addField("Name", "%");	//Ganti filter = Vendor
        data.addField("IsVendor", "Y");
        ws.setDataRow(data);

        WebServiceConnection client = getClient();

        //Load properties
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
        PreparedStatement psInsertCust = null;
        PreparedStatement psInsertSuppliersKomplit = null;
        PreparedStatement psInsertSuppliersNotNullField = null;

        try {
            //kapling database
            //Open connection
            conn = DriverManager.getConnection(jdbcUrl, username, password);

            psInsert = conn.prepareStatement("INSERT INTO temp_vendor (c_bpartner_id, bpartner_name, tax_id, value, logo_id) "
                    + "VALUES (?,?,?,?,?)");

            //psInsert untuk Customer pilih dulu yg not null
            psInsertCust = conn.prepareStatement("INSERT INTO CUSTOMERS (id, searchkey, name, maxdebt, visible, isvip, discount) VALUES(?,?,?,?,?,?,?)");

            //kolom lengkap
            psInsertSuppliersKomplit = conn.prepareStatement("INSERT INTO SUPPLIERS (id, searchkey, taxid, name, "+
                    "maxdebt, address, address2, postal, city, region, country, firstname, lastname, email, " +
                    "phone, phone2, fax, notes, visible, curdate, curdebt, vatid) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            //kolom not null only
            psInsertSuppliersNotNullField = conn.prepareStatement("INSERT INTO SUPPLIERS (id, searchkey, name, maxdebt, visible) "+
                    "VALUES (?,?,?,?,?)");

            //kapling webservice
            WindowTabDataResponse response = client.sendRequest(ws);

            if (response.getStatus() == Enums.WebServiceResponseStatus.Error) {
                System.out.println(response.getErrorMessage());
            } else {
                System.out.println("Respons Data: " + response.toString());
                System.out.println("Total rows: " + response.getTotalRows());
                System.out.println("Num rows: " + response.getNumRows());
                System.out.println("Start row: " + response.getStartRow());
                System.out.println();

                //Variable businer partner Class
                Number CBParterID = 0;
                String Name  = "";
                String Value  = "";
                String TaxID  = "";
                Integer LogoID  = 0;

                //variable Customer
                Float maxdebt = (float) 1000000;
                Boolean visible = true;
                Boolean isvip = false;
                Float discount = (float) 0;

                for (int i = 0; i < response.getDataSet().getRowsCount(); i++) {

                    System.out.println("Row: " + (i + 1));

                    for (int j = 0; j < response.getDataSet().getRow(i).getFieldsCount(); j++) {
                        Field field = response.getDataSet().getRow(i).getFields().get(j);
                        System.out.println("Column: " + field.getColumn() + " = " + field.getStringValue());

                        if (field.getColumn().equals("C_BPartner_ID")) {
                            CBParterID = field.getIntValue();
                        }else if(field.getColumn().equals("Name")){
                            Name = field.getStringValue();
                        }
                    }

                    psInsertSuppliersNotNullField.setString(1, String.valueOf(CBParterID));
                    psInsertSuppliersNotNullField.setString(2, String.valueOf(CBParterID));
                    psInsertSuppliersNotNullField.setString(3, Name);  //String
                    psInsertSuppliersNotNullField.setFloat(4, maxdebt);   //Float
                    psInsertSuppliersNotNullField.setBoolean(5,visible);   //Boolean
                    psInsertSuppliersNotNullField.executeUpdate();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (WebServiceException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new QueryDataAndInsertToSupliyer();
    }
}
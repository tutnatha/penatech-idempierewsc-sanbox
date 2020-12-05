package org.dede.wsclient;

import java.sql.*;

public class InsertStockCurrent extends AbstractTestWS2 {
    @Override
    public String getWebServiceType() {
        return null;
    }

    @Override
    public void testPerformed() {

        PropertiesLoader pl = new PropertiesLoader();
        String jdbcUrl = pl.getPropValue("db.url");
        String username = pl.getPropValue("db.user");
        String password = pl.getPropValue("db.password");

        String SQL_SELECT = "SELECT m_attributesetinstance_id, m_locator_id, m_product_id, qtyonhand FROM temp_m_storageonhand";
        String SQL_INSERT = "INSERT INTO stockcurrent (location, product, attributesetinstance_id, units)"+
                "VALUES (?,?,?,?)";

        //Select From Table POS Product
        try (Connection con = DriverManager.getConnection(jdbcUrl, username, password);
             //Create cursor on POS Product
             Statement st = con.createStatement();
//             ResultSet rs = st.executeQuery("SELECT * from PRODUCTS")) {
             ResultSet rs = st.executeQuery(SQL_SELECT)) {

            PreparedStatement psInsert = con.prepareStatement(SQL_INSERT);

            //Loop Cursor
            while (rs.next()) {
//                System.out.println("PRODUCTS:" + rs.getString(1)+ " " + rs.getString(2)+
//                        " " + rs.getString(3)+ " " + rs.getString(4)+ " " +
//                        rs.getString(5) + " " + rs.getString(6));

                System.out.println("TEMP_M_STORAGEONHAND => :"+ rs.getString(1)+ " " + rs.getString(2)+
                        " " + rs.getString(3)+ " " + rs.getString(4));

                //Insert ke Stock Current
                psInsert.setString(1, rs.getString(2)); //location = m_locator_id
                psInsert.setString(2, rs.getString(3)); //product = m_product_id
                psInsert.setString(3, rs.getString(1)); //m_attributesetinstance_id = attributesetinstance_id
                psInsert.setFloat(4, rs.getFloat(4));   //units = qty_onhand
                psInsert.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void main(String args[]) {
        InsertStockCurrent run = new InsertStockCurrent();
    }
}

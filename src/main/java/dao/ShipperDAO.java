/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import model.Shipper;
import utils.DBContext;

/**
 *
 * @author ADMIN
 */
public class ShipperDAO {

    private DBContext context;
    public ShipperDAO(){
         context = new DBContext();
    }

    public Shipper getshipperByID(int shipperID) throws SQLException {
        String sql = "Select * from Shipper where shipperID = ?";
        Object[] params = {shipperID};
        ResultSet rs = context.exeQuery(sql, params);
        Shipper shipper = null;
        
        if (rs.next()) {
            shipper = new Shipper();
            shipper.getShipperID();
            shipper.getDeliveryAreas();
            shipper.getTotalDeliveries();
        }
        return shipper;
    }
}

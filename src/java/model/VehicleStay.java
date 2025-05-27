package model;

import java.util.Date;
import java.sql.*;
import java.util.ArrayList;
import web.AppListener;

public class VehicleStay {
    public static final double HOUR_PRICE = 10.0;
    
    private long rowId;
    private String VehicleModel;
    private String VehicleColor;
    private String VehiclePlate;
    private Date beginStay;
    private Date endStay;
    private double price;
    
    public static String getCreateStatement(){
      return "CREATE TABLE IF NOT EXISTS vehicle_stays(\n"
             + " vehicle_model VARCHAR(50) NOT NULL\n"
             + ", vehicle_color VARCHAR(20) NOT NULL\n"
             + ", vehicle_plate VARCHAR(7) NOT NULL\n"
             + ", begin_stay DATETIME NOT NULL\n"
             + ", end_stay DATETIME\n"
             + ", price NUMERIC(10,2)\n"
             + ")";
    }
    
    
    public static VehicleStay getStay(long rowId) throws Exception{
        VehicleStay vs = null;
        Connection conn = AppListener.getConnection();
        String sql = "SELECT rowid, * FROM vehicle_stays WHERE rowId = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setLong(1, rowId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            vs = new VehicleStay (
                    rs.getLong("rowId"), 
                    rs.getString("vehicle_model"), 
                    rs.getString("vehicle_color"), 
                    rs.getString("vehicle_plate"), 
                    rs.getTimestamp("begin_stay")
            );
            
        }        
        rs.close();
        stmt.close();
        conn.close();

        return vs;
    }
    
    
    
    public static ArrayList<VehicleStay> getList() throws Exception{
        ArrayList<VehicleStay> list = new ArrayList<>();
        Connection conn = AppListener.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT rowid, * FROM vehicle_stays WHERE end_stay IS NULL");
        while(rs.next()){
            VehicleStay vs = new VehicleStay(
                    rs.getLong("rowId"), 
                    rs.getString("vehicle_model"), 
                    rs.getString("vehicle_color"), 
                    rs.getString("vehicle_plate"), 
                    rs.getTimestamp("begin_stay")
            );
            list.add(vs);
        }        
        rs.close();
        stmt.close();
        conn.close();

        return list;
    }
    
     public static ArrayList<VehicleStay> getHistoryList() throws Exception{
        ArrayList<VehicleStay> list = new ArrayList<>();
        Connection conn = AppListener.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT rowid, * FROM vehicle_stays WHERE end_stay IS NOT NULL");
        while(rs.next()){
            VehicleStay vs = new VehicleStay(
                    rs.getLong("rowId"), 
                    rs.getString("vehicle_model"), 
                    rs.getString("vehicle_color"), 
                    rs.getString("vehicle_plate"), 
                    rs.getTimestamp("begin_stay"),
                    rs.getTimestamp("end_stay"),
                    rs.getDouble("price")
            );
            list.add(vs);
        }        
        rs.close();
        stmt.close();
        conn.close();

        return list;
    }
   
    public static void insertVehicleStay(String model, String color, String plate) throws Exception { 
        Connection conn = AppListener.getConnection();
        String sql = "INSERT INTO vehicle_stays VALUES(?, ?, ? ,?, NULL, NULL)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        stmt.setString(1, model);
        stmt.setString(2, color);
        stmt.setString(3, plate);
        stmt.setTimestamp(4, new Timestamp(new Date().getTime()));
        stmt.execute();
       
        stmt.close();
        conn.close();
    } 
    
    public static void finishVehicleStay(long rowid, double price) throws Exception{
        Connection conn = AppListener.getConnection();
        String sql = "UPDATE vehicle_stays SET end_stay = ?, price = ? WHERE rowid = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setTimestamp(1, new Timestamp(new Date().getTime()));
        stmt.setDouble(2, price);
        stmt.setLong(3, rowid);  
        stmt.execute();
       
        stmt.close();
        conn.close();
    }
     
     
    
    public VehicleStay(long rowId, String VehicleModel, String VehicleColor, String VehiclePlate, Date beginStay, Date endStay, double price) {
        this.rowId = rowId;
        this.VehicleModel = VehicleModel;
        this.VehicleColor = VehicleColor;
        this.VehiclePlate = VehiclePlate;
        this.beginStay = beginStay;
        this.endStay = endStay;
        this.price = price;
    }

    public VehicleStay(long rowId, String VehicleModel, String VehicleColor, String VehiclePlate, Date beginStay) {
        this.rowId = rowId;
        this.VehicleModel = VehicleModel;
        this.VehicleColor = VehicleColor;
        this.VehiclePlate = VehiclePlate;
        this.beginStay = beginStay;
    }

    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    public String getVehicleModel() {
        return VehicleModel;
    }

    public void setVehicleModel(String VehicleModel) {
        this.VehicleModel = VehicleModel;
    }

    public String getVehicleColor() {
        return VehicleColor;
    }

    public void setVehicleColor(String VehicleColor) {
        this.VehicleColor = VehicleColor;
    }

    public String getVehiclePlate() {
        return VehiclePlate;
    }

    public void setVehiclePlate(String VehiclePlate) {
        this.VehiclePlate = VehiclePlate;
    }

    public Date getBeginStay() {
        return beginStay;
    }

    public void setBeginStay(Date beginStay) {
        this.beginStay = beginStay;
    }

    public Date getEndStay() {
        return endStay;
    }

    public void setEndStay(Date endStay) {
        this.endStay = endStay;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;


import java.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author w10
 */
public class dbConnector {
    
    public Connection connect ;
    
    public dbConnector (){  
    try{
        connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/mybuss", "root", "");
    }catch(SQLException ex){
    System.out.println("Can't connect to the database:" +ex.getMessage());
    }
    }
    
    public Connection getConnection() {
        return connect;
    }
    //Function to retrieve data
        public ResultSet getData(String sql) throws SQLException{
            Statement stmt = connect.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            return rst;
        }
        
        public int insertData(String sql) throws SQLException {
        Statement stmt = connect.createStatement();
        int rowsAffected = stmt.executeUpdate(sql);
        return rowsAffected;
    }
        
        public void updateData(String sql){
            int result;
            try{
                PreparedStatement pst = connect.prepareStatement(sql);
                int rowsUpdated = pst.executeUpdate();
                if(!(rowsUpdated>0)){
                    System.out.println("Data Update Failed!");
                }
            }catch(SQLException ex){
                System.out.println("Connection Error: "+ex);
            }
        }
        
        public int columnCount(String table) {
        int column = 0;
        String var = null;
        
        if(table.equals("tbl_users")){
            var = "u_";
        }else if(table.equals("tbl_bus")){
            var = "b_";
        }else if(table.equals("tbl_routes")){
            var = "r_";
        }
        
        try {
            String query = "SELECT COUNT(*) FROM " + table + " WHERE "+var+"status != 'Archived'";
            ResultSet rs = getData(query);
            if (rs.next()) {
                column = rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return column;
    }
        
        public static boolean checkData(String b, String c, String d){
        dbConnector connector = new dbConnector();
        try{
            String query = "SELECT * FROM "+b+"  WHERE "+c+" = '" +d + "'";
            ResultSet resultSet = connector.getData(query);
            return resultSet.next();
        }catch (SQLException ex) {
            return false;
        }
    }
        
        public void deleteData(int id, String table, String var){
        try{
            PreparedStatement pst = connect.prepareStatement("DELETE FROM "+table+" WHERE "+var+" = ?");
            pst.setInt(1,id);
            int rowsDeleted=pst.executeUpdate();
            if(rowsDeleted >0){
                JOptionPane.showMessageDialog(null, "Deleted Successfully!");
            }else{
                System.out.println("Deletion Failed");
            }
            pst.close();
        }catch(SQLException ex){
                JOptionPane.showMessageDialog(null, "Deleted Unsuccessfully. \nThis data is currently associated with existing transactions.");
        }
    }

    

}
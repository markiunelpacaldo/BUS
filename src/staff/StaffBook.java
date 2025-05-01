/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff;

import admin.*;
import config.PanelPrinter;
import config.Session;
import config.dbConnector;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import login.logindashboard;

/**
 *
 * @author w10
 */
public class StaffBook extends javax.swing.JFrame {
    public StaffBook() {
        initComponents();
        setDefault();
        Timer timer = new Timer(0, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setDateTime();
            }
        });
        timer.start();
    }

    
    public void setDateTime(){
        Session sess = Session.getInstance();
        namedisp.setText(""+sess.getFname() + " "+ sess.getLname());
        Date now = new Date();
        
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String time = timeFormat.format(now);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String date = dateFormat.format(now);
        
        date_disp.setText(date);
        time_disp.setText(time);
       
    }
    
    public void setDefault(){
        dbConnector connect = new dbConnector();
        try {
            bus.addItem("");
            ResultSet rs = connect.getData("SELECT * FROM tbl_bus WHERE b_status != 'Archived'");
            while(rs.next()){
                bus.addItem(rs.getString("b_id"));
            }
            
            routes.addItem("");
            ResultSet rs1 = connect.getData("SELECT * FROM tbl_routes WHERE r_status != 'Archived'");
            while(rs1.next()){
                routes.addItem(rs1.getString("r_id"));
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    
    public void calculateFare(){
        dbConnector connect = new dbConnector();
        if(routes.getSelectedItem().equals("")){
            fare.setText("0.00");
        }else{
            try {
                ResultSet rs = connect.getData("SELECT * FROM tbl_routes WHERE r_id = "+routes.getSelectedItem());
                if(rs.next()){
                    if(passenger.getSelectedItem().equals("Child")){
                        fare.setText(String.valueOf(rs.getDouble("r_fare")*.5));
                    }else if(passenger.getSelectedItem().equals("Senior Citizen")){
                        fare.setText(String.valueOf(rs.getDouble("r_fare")*.75));
                    }else if(passenger.getSelectedItem().equals("PWD")){
                        fare.setText(String.valueOf(rs.getDouble("r_fare")*.75));
                    }else{
                        fare.setText(String.valueOf(rs.getDouble("r_fare")));
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(StaffBook.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    private void logoutUser(String username) {
    dbConnector connector = new dbConnector();
    try (Connection con = connector.getConnection()) {

        String updateQuery = "UPDATE tbl_log SET log_status = 'Inactive', logout_time = NOW() " +
                             "WHERE LOWER(u_username) = LOWER(?) AND log_status = 'Active'";

        try (PreparedStatement stmt = con.prepareStatement(updateQuery)) {
            System.out.println("Logging out user: " + username); // Debug
            stmt.setString(1, username);
            int updatedRows = stmt.executeUpdate();

            if (updatedRows > 0) {
                JOptionPane.showMessageDialog(null, "User " + username + " has logged out successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "No active session found for " + username);
            }
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error logging out: " + ex.getMessage());
    }
}
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        namedisp = new javax.swing.JLabel();
        date_disp = new javax.swing.JLabel();
        time_disp = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        bus = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        passenger = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        routes = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        routedisp = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        fare = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        busdisp = new javax.swing.JLabel();
        passengerdisp = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(null);

        jPanel2.setBackground(new java.awt.Color(255, 0, 0));
        jPanel2.setAutoscrolls(true);
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(216, 217, 218), 1, true));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        namedisp.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        namedisp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        namedisp.setText("Staff");
        jPanel4.add(namedisp, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, -1, -1));

        date_disp.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        date_disp.setText("dd/mm/yyyy");
        jPanel4.add(date_disp, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, -1, -1));

        time_disp.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        time_disp.setText("hh-mm-ss");
        jPanel4.add(time_disp, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, 140, -1));

        jButton1.setBackground(new java.awt.Color(0, 0, 255));
        jButton1.setText("View Profile");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 210, -1));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/staff/zd.png"))); // NOI18N
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 10, 70, 70));

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 230, 120));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel6MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel6MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel6MouseExited(evt);
            }
        });
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/icons8-logout-25 (1).png"))); // NOI18N
        jLabel2.setText(" Logout");
        jPanel6.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 5, -1, 30));

        jPanel2.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 500, 130, 40));

        jPanel7.setBackground(new java.awt.Color(204, 255, 255));
        jPanel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel7MouseClicked(evt);
            }
        });
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/icons8-bus-25.png"))); // NOI18N
        jLabel7.setText(" Dashboard");
        jPanel7.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 5, 120, 30));

        jPanel2.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 270, 40));

        jPanel1.add(jPanel2);
        jPanel2.setBounds(-10, 0, 280, 590);

        jPanel12.setBackground(new java.awt.Color(102, 102, 102));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bus.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                busItemStateChanged(evt);
            }
        });
        bus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                busActionPerformed(evt);
            }
        });
        jPanel12.add(bus, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 380, 30));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setText("Bus Selection");
        jPanel12.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 90, 15));

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setText("Passenger Type");
        jPanel12.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 110, 15));

        passenger.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Adult", "Child", "Senior Citizen", "PWD" }));
        passenger.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                passengerItemStateChanged(evt);
            }
        });
        passenger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passengerActionPerformed(evt);
            }
        });
        jPanel12.add(passenger, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 380, 30));

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel18.setText("Route");
        jPanel12.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 110, -1));

        routes.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                routesItemStateChanged(evt);
            }
        });
        routes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                routesActionPerformed(evt);
            }
        });
        jPanel12.add(routes, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 380, 30));

        jPanel5.setBackground(new java.awt.Color(252, 255, 224));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/icons8-passenger-20_1.png"))); // NOI18N
        jLabel14.setText("BUSWAYZ BUS TICKET");
        jPanel5.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));
        jPanel5.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 210, -1));
        jPanel5.add(routedisp, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, 180, 30));

        jLabel17.setText("Passenger Type:");
        jPanel5.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, 20));

        jLabel21.setText("Route:");
        jPanel5.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, -1, 20));

        jPanel13.setBackground(new java.awt.Color(252, 255, 224));
        jPanel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setText("Fare");
        jPanel13.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, 40));

        fare.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        fare.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/black peso.png"))); // NOI18N
        fare.setText("0.00");
        fare.setFocusTraversalPolicyProvider(true);
        jPanel13.add(fare, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        jPanel5.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 230, 100));

        jLabel19.setText("Bus:");
        jPanel5.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, 20));
        jPanel5.add(busdisp, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 180, 30));

        passengerdisp.setText("Adult");
        jPanel5.add(passengerdisp, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, 180, 30));

        jPanel12.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 20, 230, -1));

        jPanel19.setBackground(new java.awt.Color(119, 176, 170));
        jPanel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        jPanel19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel19MouseClicked(evt);
            }
        });
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel25.setText("Print Ticket");
        jPanel19.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 100, -1));

        jPanel12.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 350, 230, 40));

        jPanel1.add(jPanel12);
        jPanel12.setBounds(270, 120, 680, 460);

        jLabel3.setFont(new java.awt.Font("Segoe Print", 3, 36)); // NOI18N
        jLabel3.setText("PACALDO BUS STATION");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(370, 30, 470, 65);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 950, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jPanel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseClicked
    
            Session sess = Session.getInstance();
    if (sess.getUid() != 0) {
        logoutUser(sess.getUsername());  // Log out the current user // CLEAR
    }

    logindashboard loginFrame = new logindashboard();
    JOptionPane.showMessageDialog(null, "Log-out Success!");
    loginFrame.setVisible(true);
    this.dispose();
    }//GEN-LAST:event_jPanel6MouseClicked

    private void jPanel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel7MouseClicked
        StaffBook open = new StaffBook();
        open.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jPanel7MouseClicked

    private void jPanel6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseEntered
        jPanel6.setBackground(new Color(97, 103, 122));
    }//GEN-LAST:event_jPanel6MouseEntered

    private void jPanel6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseExited
        jPanel6.setBackground(new Color(39, 40, 41));
    }//GEN-LAST:event_jPanel6MouseExited

    private void busActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_busActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_busActionPerformed

    private void passengerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passengerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passengerActionPerformed

    private void routesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_routesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_routesActionPerformed

    private void jPanel19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel19MouseClicked
        PanelPrinter print = new PanelPrinter(jPanel5);
        print.printPanel();
    }//GEN-LAST:event_jPanel19MouseClicked

    private void busItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_busItemStateChanged
        dbConnector connect = new dbConnector();
        if(bus.getSelectedItem().equals("")){
            busdisp.setText("");
        }else{
            try {
                ResultSet rs = connect.getData("SELECT * FROM tbl_bus WHERE b_id = "+bus.getSelectedItem());
                if(rs.next()){
                    busdisp.setText(rs.getString("b_name"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(StaffBook.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_busItemStateChanged

    private void passengerItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_passengerItemStateChanged
        passengerdisp.setText(passenger.getSelectedItem().toString());
        calculateFare();
    }//GEN-LAST:event_passengerItemStateChanged

    private void routesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_routesItemStateChanged
        dbConnector connect = new dbConnector();
        if(routes.getSelectedItem().equals("")){
            routedisp.setText("");
        }else{
            try {
                ResultSet rs = connect.getData("SELECT * FROM tbl_routes WHERE r_id = "+routes.getSelectedItem());
                if(rs.next()){
                    routedisp.setText(rs.getString("r_from")+ " to "+rs.getString("r_destination"));
                }
                calculateFare();
            } catch (SQLException ex) {
                Logger.getLogger(StaffBook.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_routesItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        StaffProfile open = new StaffProfile();
        open.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(StaffBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StaffBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StaffBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StaffBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StaffBook().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> bus;
    private javax.swing.JLabel busdisp;
    private javax.swing.JLabel date_disp;
    private javax.swing.JLabel fare;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel namedisp;
    private javax.swing.JComboBox<String> passenger;
    private javax.swing.JLabel passengerdisp;
    private javax.swing.JLabel routedisp;
    private javax.swing.JComboBox<String> routes;
    private javax.swing.JLabel time_disp;
    // End of variables declaration//GEN-END:variables
}

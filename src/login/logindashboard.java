/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import admin.AdminProfile;
import admin.adminDashboard;
import config.Session;
import config.dbConnector;
import config.passwordHasher;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import javax.swing.JOptionPane;
import staff.StaffBook;

/**
 *
 * @author w10
 */
public class logindashboard extends javax.swing.JFrame {

    public logindashboard() {
        initComponents();
    }
    
    static String status;
    static String type;
    
 public static boolean loginAcc(String username, String password) {
    dbConnector conn = new dbConnector(); // Create the dbConnector instance
    String query = "SELECT * FROM tbl_users WHERE u_username = ?";

    try (Connection connection = conn.getConnection(); // Use conn to get the connection
         PreparedStatement stmt = connection.prepareStatement(query)) {

        stmt.setString(1, username);
        ResultSet resultSet = stmt.executeQuery();

        if (resultSet.next()) {
            String hashedPass = resultSet.getString("u_password");
            
            // Hash the input password to compare with stored hashed password
            String rehashedPass = passwordHasher.hashPassword(password);

            if (hashedPass.equals(rehashedPass)) {
                String status = resultSet.getString("u_status"); // Directly retrieve status as a String
                String type = resultSet.getString("u_type");     // Directly retrieve type as a String

                // Set session details
                Session sess = Session.getInstance();
                sess.setUid(resultSet.getInt("u_id"));
                sess.setFname(resultSet.getString("u_fname"));
                sess.setLname(resultSet.getString("u_lname"));
                sess.setEmail(resultSet.getString("u_email"));
                sess.setUsername(resultSet.getString("u_username"));
                sess.setType(type); // Using the corrected `type`
                sess.setStatus(status); // Using the corrected `status`

                // Log the event
             
                return true;
            }
        }
    } catch (SQLException | NoSuchAlgorithmException ex) {
        ex.printStackTrace(); // Log the error for debugging
    }
    return false; // Return false if login fails
}



public static void setSession(ResultSet resultSet) throws SQLException {
    Session sess = Session.getInstance();
    
    // Assuming 'u_id' is stored as an integer in the database, use getInt() to fetch the ID
    sess.setUid(resultSet.getInt("u_id")); // Use getInt instead of getString
    
    // Set other fields as before
    sess.setFname(resultSet.getString("u_fname"));
    sess.setLname(resultSet.getString("u_lname"));
    sess.setUsername(resultSet.getString("u_username"));
    sess.setEmail(resultSet.getString("u_email"));
    sess.setNum(resultSet.getString("u_num"));
    sess.setPassword(resultSet.getString("u_password"));
    sess.setType(resultSet.getString("u_type"));
    sess.setStatus(resultSet.getString("u_status"));
}



  public String getUserId(String username) {
       
        dbConnector dbc = new dbConnector();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String userId = null;

        try {
         
            String sql = "SELECT u_id FROM tbl_users WHERE u_username = ?";
            pstmt = dbc.connect.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                userId = rs.getString("u_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
           
       
        }
        return userId;
    }
    public void logEvent(int userId, String username, String userType) {
    dbConnector dbc = new dbConnector();
    Connection con = dbc.getConnection();
    PreparedStatement pstmt = null;
    String ut = "Active"; // Assuming the status is "Active" once logged in

    try {
        String sql = "INSERT INTO tbl_log (u_id, u_username, login_time, u_type, log_status) VALUES (?, ?, ?, ?, ?)";
        pstmt = con.prepareStatement(sql);

        pstmt.setInt(1, userId);
        pstmt.setString(2, username);
        pstmt.setTimestamp(3, new Timestamp(new Date().getTime())); // Log the current time
        pstmt.setString(4, userType);
        pstmt.setString(5, ut);

        pstmt.executeUpdate();
        System.out.println("Login log recorded successfully.");
    } catch (SQLException e) {
        System.out.println("Error recording log: " + e.getMessage());
    } finally {
        try {
            if (pstmt != null) pstmt.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.out.println("Error closing resources: " + e.getMessage());
        }
    }
}


     public String getUserTypeFromDatabase(String username) {
    String type = "";
    String query = "SELECT u_type FROM tbl_users WHERE LOWER(u_username) = LOWER(?)";
    
    // Use an instance of dbConnector to get the connection
    dbConnector connector = new dbConnector();  // Create instance of dbConnector
    try (Connection con = connector.getConnection(); 
         PreparedStatement stmt = con.prepareStatement(query)) {
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            type = rs.getString("u_type");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return type;
}
      public String getStatusFromDatabase(String username) {
    String status = "";
    String query = "SELECT log_status FROM tbl_log WHERE LOWER(u_username) = LOWER(?) ORDER BY login_time DESC LIMIT 1";
    
    // Use an instance of dbConnector to get the connection
    dbConnector connector = new dbConnector();  // Create instance of dbConnector
    try (Connection con = connector.getConnection(); 
         PreparedStatement stmt = con.prepareStatement(query)) {
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            status = rs.getString("log_status");
            System.out.println("status: "+status);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return status;
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel5 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        user = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        pass = new javax.swing.JPasswordField();
        jLabel9 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        jLabel5.setText("jLabel5");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 255, 153));

        jPanel7.setBackground(new java.awt.Color(102, 102, 102));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "PACALDO BUS STATION", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe Print", 1, 36))); // NOI18N
        jPanel7.setForeground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(null);

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 10, true));
        jPanel1.setForeground(new java.awt.Color(92, 131, 116));
        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("Password");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(20, 70, 90, 30);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel2.setText("Username");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(20, 20, 100, 15);

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));
        jPanel2.setLayout(null);

        jPanel5.setBackground(new java.awt.Color(0, 153, 102));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(jPanel5);
        jPanel5.setBounds(-10, -10, 540, 270);

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setText("Password:");
        jPanel2.add(jLabel3);
        jLabel3.setBounds(110, 430, 90, 17);

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setText("Username:");
        jPanel2.add(jLabel4);
        jLabel4.setBounds(110, 380, 100, 17);

        jTextField2.setText("jTextField1");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jPanel2.add(jTextField2);
        jTextField2.setBounds(200, 380, 190, 22);

        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 0, 0, 0);

        user.setBackground(new java.awt.Color(238, 238, 238));
        user.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userActionPerformed(evt);
            }
        });
        jPanel1.add(user);
        user.setBounds(20, 40, 380, 30);

        jButton1.setBackground(new java.awt.Color(118, 171, 174));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setText("Login");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(20, 140, 380, 30);
        jPanel1.add(jSeparator2);
        jSeparator2.setBounds(20, 180, 380, 10);

        jLabel6.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Click here to register..");
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel6);
        jLabel6.setBounds(140, 210, 140, 20);
        jPanel1.add(pass);
        pass.setBounds(20, 100, 380, 30);

        jLabel9.setText("Forget Password");
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel9);
        jLabel9.setBounds(160, 190, 100, 16);

        jPanel7.add(jPanel1);
        jPanel1.setBounds(430, 170, 430, 240);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/login/bus-stop.png"))); // NOI18N
        jPanel7.add(jLabel7);
        jLabel7.setBounds(90, 160, 280, 270);

        jLabel8.setFont(new java.awt.Font("Segoe Print", 3, 18)); // NOI18N
        jLabel8.setText("DEVELOP BY MARK PACALDO BSIT-2C");
        jPanel7.add(jLabel8);
        jLabel8.setBounds(40, 510, 380, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 920, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        registerdashboard rgst = new registerdashboard();
        rgst.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      
   String username = user.getText();  // Get username input
    String password = pass.getText();  // Get password input

    dbConnector connector = new dbConnector();
    String status = null;
    String type = null;

    // Check login credentials
    if (loginAcc(username, password)) {
        // Get session info
        Session sess = Session.getInstance();
        int userId = sess.getUid();

        try {
            // Retrieve full user details
            String query = "SELECT * FROM tbl_users WHERE u_username = ?";
            PreparedStatement pstmt = connector.getConnection().prepareStatement(query);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                status = rs.getString("u_status");
                type = rs.getString("u_type");
            } else {
                JOptionPane.showMessageDialog(null, "User not found.");
                return;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            return;
        }

        // Check if account is active
        if (!"Active".equalsIgnoreCase(status)) {
            JOptionPane.showMessageDialog(null, "Inactive Account, Contact the Admin!");
            logEvent(userId, username, "Failed - Inactive Account");
        } else {
            // Admin or Staff redirection
            if ("Admin".equalsIgnoreCase(type)) {
                JOptionPane.showMessageDialog(null, "Login Success! Welcome Admin.");
                AdminProfile adminFrame = new AdminProfile();
                adminFrame.setVisible(true);
                logEvent(userId, username, "Success - Admin Login");
            } else if ("Staff".equalsIgnoreCase(type)) {
                JOptionPane.showMessageDialog(null, "Login Success! Welcome Staff.");
                StaffBook staffFrame = new StaffBook();
                staffFrame.setVisible(true);
                logEvent(userId, username, "Success - Staff Login");
            } else {
                JOptionPane.showMessageDialog(null, "Unknown user type: " + type);
            }
            this.dispose();  // Close current frame
        }
    } else {
        // Login failed
        JOptionPane.showMessageDialog(null, "Invalid Username or Password!");
        logEvent(-1, username, "Failed - Invalid Login");
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void userActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_userActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
           new ForgetPassword().setVisible(true); // Open ForgotPassword form
          dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel9MouseClicked

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
            java.util.logging.Logger.getLogger(logindashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(logindashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(logindashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(logindashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new logindashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JPasswordField pass;
    private javax.swing.JTextField user;
    // End of variables declaration//GEN-END:variables
}

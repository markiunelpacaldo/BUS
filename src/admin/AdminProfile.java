/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import config.Session;
import config.dbConnector;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import login.logindashboard;

/**
 *
 * @author w10
 */
public class AdminProfile extends javax.swing.JFrame {
    public AdminProfile() {
        initComponents();
        
        Timer timer = new Timer(0, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setDateTime();
            }
        });
        timer.start();
         loadUserProfile();
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
        
        profilename.setText(""+sess.getFname() + " "+ sess.getLname());
        profilefname.setText("First Name: "+sess.getFname());
        profilelname.setText("Last Name: "+sess.getLname());
        profileemail.setText("E-mail: "+sess.getEmail());
        profilenum.setText("Contact Number: "+sess.getNum());
        profiletype.setText("Type: "+sess.getType());
        profilestatus.setText("Status: "+sess.getStatus());
        profileusername.setText("Username: "+sess.getUsername());
        profilepass.setText("Password: "+sess.getPassword());
    }
    
    private void loadLogs() {
    dbConnector connector = new dbConnector();
    try (Connection con = connector.getConnection()) {

        // Update 'Pending' log_status to 'Active' for recent logins
        String updateQuery = "UPDATE tbl_log SET log_status = 'Active' WHERE log_status = 'Pending'";
        try (PreparedStatement updateStmt = con.prepareStatement(updateQuery)) {
            updateStmt.executeUpdate();
        }

        // Fetch updated logs including logout time and log_description
        String selectQuery = "SELECT l.log_id, l.u_username, l.login_time, l.logout_time, l.u_type, " +
                             "CASE WHEN u.u_username IS NULL THEN 'Invalid User' ELSE l.log_status END AS log_status, " +
                             "l.log_description " + // Include log_description
                             "FROM tbl_log l LEFT JOIN tbl_users u ON l.u_username = u.u_username " +
                             "ORDER BY l.login_time DESC";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(selectQuery)) {

            DefaultTableModel model = new DefaultTableModel(
                new String[]{"Log ID", "Username", "Login Time", "Logout Time", "User Type", "Status", "Description"}, 0 // Add "Description" column
            );

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("log_id"),
                    rs.getString("u_username"),
                    rs.getTimestamp("login_time"),
                    rs.getTimestamp("logout_time"),
                    rs.getString("u_type"),
                    rs.getString("log_status"),
                    rs.getString("log_description") // Add log_description value
                });
            }

            logstbl.setModel(model);
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error loading logs: " + ex.getMessage());
    }
}

 
 private void logoutUser(String username) {
    dbConnector connector = new dbConnector();

    try (Connection con = connector.getConnection()) {
        String updateQuery = "UPDATE tbl_log SET log_status = 'Inactive', logout_time = NOW() " +
                             "WHERE u_username = ? AND log_status = 'Active' ORDER BY log_id DESC LIMIT 1";

        try (PreparedStatement stmt = con.prepareStatement(updateQuery)) {
            stmt.setString(1, username);
            int updatedRows = stmt.executeUpdate();

            if (updatedRows > 0) {
                System.out.println("Logout recorded in log.");
            } else {
                System.out.println("No active session found for " + username);
            }
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error logging out: " + ex.getMessage());
    }
}

 private void loadUserProfile() {
    dbConnector dbc = new dbConnector();
    Session sess = Session.getInstance();

    String query = "SELECT u_image FROM tbl_users WHERE u_id = ?";

    try (Connection conn = dbc.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setInt(1, sess.getUid());
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            String imagePath = rs.getString("u_image");

            if (imagePath != null && !imagePath.isEmpty()) {
                ImageIcon icon = new ImageIcon(imagePath);
                u_image.setIcon(icon);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Log the error
        JOptionPane.showMessageDialog(this, "Error loading profile image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}   
 
 
        public void logEvent(int userId, String username, String action) 
    {
        dbConnector dbc = new dbConnector();
        Connection con = dbc.getConnection();
        PreparedStatement pstmt = null;
        Timestamp time = new Timestamp(new Date().getTime());

        try {
            String sql = "INSERT INTO tbl_log (u_id, u_username, login_time, log_status, log_description) "
                    + "VALUES ('" + userId + "', '" + username + "', '" + time + "', '" + action + "')";
            pstmt = con.prepareStatement(sql);

            /*            pstmt.setInt(1, userId);
            pstmt.setString(2, username);
            pstmt.setTimestamp(3, new Timestamp(new Date().getTime()));
            pstmt.setString(4, userType);*/
            pstmt.executeUpdate();
            System.out.println("Login log recorded successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error recording log: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error closing resources: " + e.getMessage());
            }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        namedisp = new javax.swing.JLabel();
        date_disp = new javax.swing.JLabel();
        time_disp = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        u_image = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        profilename = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        profilestatus = new javax.swing.JLabel();
        profilefname = new javax.swing.JLabel();
        profilelname = new javax.swing.JLabel();
        profilepass = new javax.swing.JLabel();
        profilenum = new javax.swing.JLabel();
        profiletype = new javax.swing.JLabel();
        profileemail = new javax.swing.JLabel();
        profileusername = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        logstbl = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        searchbar = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(null);

        jPanel2.setBackground(new java.awt.Color(255, 0, 0));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        jPanel6.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, 40));

        jPanel2.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 520, 120, 40));

        jPanel7.setBackground(new java.awt.Color(204, 255, 255));
        jPanel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel7MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel7MouseExited(evt);
            }
        });
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/icons8-bus-25.png"))); // NOI18N
        jLabel7.setText(" Dashboard");
        jPanel7.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 5, 120, 30));

        jPanel2.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 270, 40));

        jPanel8.setBackground(new java.awt.Color(204, 255, 255));
        jPanel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel8MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel8MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel8MouseExited(evt);
            }
        });
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/icons8-bus-25.png"))); // NOI18N
        jLabel8.setText(" Bus");
        jPanel8.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 5, 80, 30));

        jPanel2.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 270, 40));

        jPanel9.setBackground(new java.awt.Color(204, 255, 255));
        jPanel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel9MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel9MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel9MouseExited(evt);
            }
        });
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/icons8-route-30_1.png"))); // NOI18N
        jLabel3.setText("Routes");
        jPanel9.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 80, 40));

        jPanel2.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 270, 40));

        jPanel10.setBackground(new java.awt.Color(204, 255, 255));
        jPanel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel10MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel10MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel10MouseExited(evt);
            }
        });
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/icons8-staff-25.png"))); // NOI18N
        jLabel4.setText(" Staff");
        jPanel10.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 5, 80, 30));

        jPanel2.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 270, 40));

        jPanel4.setBackground(new java.awt.Color(97, 103, 122));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(216, 217, 218), 1, true));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        namedisp.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        namedisp.setForeground(new java.awt.Color(255, 255, 255));
        namedisp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        namedisp.setText("System Administrator");
        jPanel4.add(namedisp, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, -1, -1));

        date_disp.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        date_disp.setForeground(new java.awt.Color(255, 255, 255));
        date_disp.setText("dd/mm/yyyy");
        jPanel4.add(date_disp, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, -1, -1));

        time_disp.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        time_disp.setForeground(new java.awt.Color(255, 255, 255));
        time_disp.setText("hh-mm-ss");
        jPanel4.add(time_disp, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, 150, -1));

        jButton2.setText("View Profile");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 210, -1));

        u_image.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        u_image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/admin/businessman.png"))); // NOI18N
        jPanel4.add(u_image, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 6, 70, 70));

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 230, 120));

        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 0, 290, 580);

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(97, 103, 122));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 190, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 190, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 190, 190));

        profilename.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        profilename.setText("Null");
        jPanel3.add(profilename, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 20, 380, -1));
        jPanel3.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 70, 380, 10));

        profilestatus.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        profilestatus.setText("status");
        jPanel3.add(profilestatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 180, 160, -1));

        profilefname.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        profilefname.setText("Fname");
        jPanel3.add(profilefname, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 80, 160, -1));

        profilelname.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        profilelname.setText("Lname");
        jPanel3.add(profilelname, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 100, 160, -1));

        profilepass.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        profilepass.setText("password");
        jPanel3.add(profilepass, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 100, 190, -1));

        profilenum.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        profilenum.setText("contact");
        jPanel3.add(profilenum, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 140, 160, -1));

        profiletype.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        profiletype.setText("type");
        jPanel3.add(profiletype, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 160, 160, -1));

        profileemail.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        profileemail.setText("email");
        jPanel3.add(profileemail, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 120, 160, -1));

        profileusername.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        profileusername.setText("username");
        jPanel3.add(profileusername, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 80, 190, -1));

        jButton1.setText("Change Password");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 120, 190, -1));

        jButton3.setText("AccountDetails");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });
        jPanel3.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 160, 190, -1));

        jPanel1.add(jPanel3);
        jPanel3.setBounds(300, 30, 620, 210);

        logstbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "log_id", "u_username", "login_time", "u_type", "log_status", "log_description"
            }
        ));
        jScrollPane1.setViewportView(logstbl);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(310, 310, 620, 250);

        jButton6.setText("Refresh");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton6);
        jButton6.setBounds(530, 270, 79, 25);
        jPanel1.add(searchbar);
        searchbar.setBounds(310, 270, 210, 22);

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
    logoutUser(sess.getUsername());
}

JOptionPane.showMessageDialog(null, "Log-out Success!");
new logindashboard().setVisible(true);
this.dispose();

    }//GEN-LAST:event_jPanel6MouseClicked

    private void jPanel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel10MouseClicked
        Staff open = new Staff();
        open.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jPanel10MouseClicked

    private void jPanel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel9MouseClicked
        Routes open = new Routes();
        open.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jPanel9MouseClicked

    private void jPanel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel8MouseClicked
        Busview open = new Busview();
        open.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jPanel8MouseClicked

    private void jPanel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel7MouseClicked
        adminDashboard open = new adminDashboard();
        open.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jPanel7MouseClicked

    private void jPanel8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel8MouseEntered
        jPanel8.setBackground(new Color(97, 103, 122));
    }//GEN-LAST:event_jPanel8MouseEntered

    private void jPanel8MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel8MouseExited
        jPanel8.setBackground(new Color(39, 40, 41));
    }//GEN-LAST:event_jPanel8MouseExited

    private void jPanel9MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel9MouseEntered
        jPanel9.setBackground(new Color(97, 103, 122));
    }//GEN-LAST:event_jPanel9MouseEntered

    private void jPanel9MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel9MouseExited
        jPanel9.setBackground(new Color(39, 40, 41));
    }//GEN-LAST:event_jPanel9MouseExited

    private void jPanel10MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel10MouseEntered
        jPanel10.setBackground(new Color(97, 103, 122));
    }//GEN-LAST:event_jPanel10MouseEntered

    private void jPanel10MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel10MouseExited
        jPanel10.setBackground(new Color(39, 40, 41));
    }//GEN-LAST:event_jPanel10MouseExited

    private void jPanel6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseEntered
        jPanel6.setBackground(new Color(97, 103, 122));
    }//GEN-LAST:event_jPanel6MouseEntered

    private void jPanel6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseExited
        jPanel6.setBackground(new Color(39, 40, 41));
    }//GEN-LAST:event_jPanel6MouseExited

    private void jPanel7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel7MouseEntered
        jPanel7.setBackground(new Color(97, 103, 122));
    }//GEN-LAST:event_jPanel7MouseEntered

    private void jPanel7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel7MouseExited
        jPanel7.setBackground(new Color(39, 40, 41));
    }//GEN-LAST:event_jPanel7MouseExited

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        AdminProfile open = new AdminProfile();
        open.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        changepass cp = new changepass();
        cp.setVisible(true);
        this.dispose();            // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
         AccountAdminDetails ads = new AccountAdminDetails();
        ads.setVisible(true);
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3MouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
       loadUserProfile();
        searchbar.setText("");        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
          loadLogs();        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowActivated

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
            java.util.logging.Logger.getLogger(AdminProfile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminProfile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminProfile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminProfile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
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
                new AdminProfile().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel date_disp;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable logstbl;
    private javax.swing.JLabel namedisp;
    private javax.swing.JLabel profileemail;
    private javax.swing.JLabel profilefname;
    private javax.swing.JLabel profilelname;
    private javax.swing.JLabel profilename;
    private javax.swing.JLabel profilenum;
    private javax.swing.JLabel profilepass;
    private javax.swing.JLabel profilestatus;
    private javax.swing.JLabel profiletype;
    private javax.swing.JLabel profileusername;
    private javax.swing.JTextField searchbar;
    private javax.swing.JLabel time_disp;
    private javax.swing.JLabel u_image;
    // End of variables declaration//GEN-END:variables
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import login.*;
import admin.adminDashboard;
import config.Session;
import config.dbConnector;
import config.passwordHasher;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author w10
 */
public class StaffAdd extends javax.swing.JFrame {

   
    public StaffAdd() {
        initComponents();
    }
    
  
              
    public String destination = ""; 
    File selectedFile;
    public String oldpath;
    public String path;
        

   public boolean duplicateCheck(String username, String email) { 
    dbConnector dbc = new dbConnector();
    
    try {
        String query = "SELECT * FROM tbl_users WHERE u_username = '" + username + "' OR u_email = '" + email + "'";
        ResultSet resultSet = dbc.getData(query);
        
        if (resultSet.next()) {
            String existingEmail = resultSet.getString("u_email");
            if (existingEmail.equals(email)) {
                JOptionPane.showMessageDialog(null, "Email is already used!");
                return true;
            }

            String existingUsername = resultSet.getString("u_username");
            if (existingUsername.equals(username)) {
                JOptionPane.showMessageDialog(null, "Username is already used!");
                return true;
            }
        }
    } catch (SQLException ex) {
        System.out.println("" + ex);
    }
    
    return false; // No duplicate found
}

  public boolean UpdateCheck() {
    dbConnector dbc = new dbConnector();

    try {
        // Assuming uid is a JTextField that holds the user's ID.
        String userId = uid.getText().trim(); // Directly get the user ID from the uid text field.

        String query = "SELECT * FROM tbl_users WHERE (u_username = ? OR u_email = ?) AND u_id != ?";
        try (Connection conn = dbc.getConnection(); 
             PreparedStatement pst = conn.prepareStatement(query)) {
            
            // Use prepared statements to prevent SQL injection
            pst.setString(1, uname.getText().trim());  // User's username
            pst.setString(2, email.getText().trim());  // User's email
            pst.setString(3, userId);  // User's ID (current user)

            ResultSet resultSet = pst.executeQuery();
            
            if (resultSet.next()) {
                // Check for duplicate email
                String existingEmail = resultSet.getString("u_email");
                if (existingEmail.equals(email.getText().trim())) {
                    JOptionPane.showMessageDialog(null, "Email is already used!");
                    email.setText(""); // Clear the email field
                }

                // Check for duplicate username
                String existingUsername = resultSet.getString("u_username");
                if (existingUsername.equals(uname.getText().trim())) {
                    JOptionPane.showMessageDialog(null, "Username is already used!");
                    uname.setText(""); // Clear the username field
                }

                return true;  // Indicate that duplicates were found
            } else {
                return false;  // No duplicates found
            }
        }
    } catch (SQLException ex) {
        // Handle database errors
        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
}


     public static int getHeightFromWidth(String imagePath, int desiredWidth) {
        try {
            // Read the image file
            File imageFile = new File(imagePath);
            BufferedImage image = ImageIO.read(imageFile);
            
            // Get the original width and height of the image
            int originalWidth = image.getWidth();
            int originalHeight = image.getHeight();
            
            // Calculate the new height based on the desired width and the aspect ratio
            int newHeight = (int) ((double) desiredWidth / originalWidth * originalHeight);
            
            return newHeight;
        } catch (IOException ex) {
            System.out.println("No image found!");
        }
        
        return -1;
    }    
    
       public  ImageIcon ResizeImage(String ImagePath, byte[] pic, JLabel label) {
        ImageIcon MyImage = null;
            if(ImagePath !=null){
                MyImage = new ImageIcon(ImagePath);
            }else{
                MyImage = new ImageIcon(pic);
            }

        int newHeight = getHeightFromWidth(ImagePath, label.getWidth());

        Image img = MyImage.getImage();
        Image newImg = img.getScaledInstance(label.getWidth(), newHeight, Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImg);
        return image;
    }
     public int FileExistenceChecker(String path){
        File file = new File(path);
        String fileName = file.getName();
        
        Path filePath = Paths.get("src/usersimages", fileName);
        boolean fileExists = Files.exists(filePath);
        
        if (fileExists) {
            return 1;
        } else {
            return 0;
        }
    
    }
    
        public void imageUpdater(String existingFilePath, String newFilePath){
        File existingFile = new File(existingFilePath);
        if (existingFile.exists()) {
            String parentDirectory = existingFile.getParent();
            File newFile = new File(newFilePath);
            String newFileName = newFile.getName();
            File updatedFile = new File(parentDirectory, newFileName);
            existingFile.delete();
            try {
                Files.copy(newFile.toPath(), updatedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image updated successfully.");
            } catch (IOException e) {
                System.out.println("Error occurred while updating the image: "+e);
            }
        } else {
            try{
                Files.copy(selectedFile.toPath(), new File(destination).toPath(), StandardCopyOption.REPLACE_EXISTING);
            }catch(IOException e){
                System.out.println("Error on update!");
            }
        }
   }

    public static boolean checkuseremail(String var, String txt){
        dbConnector connector = new dbConnector();
        try{
            String query = "SELECT * FROM tbl_users  WHERE "+ var+" = '" + txt + "'";
            ResultSet resultSet = connector.getData(query);
            return resultSet.next();
        }catch (SQLException ex) {
            System.out.println("Error:" +ex.getMessage());
            return false;
        }
    }
    
    public boolean isnotNum (String num){
        if((!num.matches(".*[^0-9].*"))){
            return false;
        }else{
            return true;
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
      
      private String generateUniqueUNum(Connection conn) throws SQLException {
    String prefix = "U";
    String query = "SELECT u_num FROM tbl_users ORDER BY u_num DESC LIMIT 1";

    try (PreparedStatement pst = conn.prepareStatement(query);
         ResultSet rs = pst.executeQuery()) {

        if (rs.next()) {
            String lastUNum = rs.getString("u_num").replace(prefix, "");
            int num = Integer.parseInt(lastUNum);
            num++;
            return String.format(prefix + "%03d", num);  // e.g., U001, U002
        } else {
            return prefix + "001"; // First user
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
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        fname = new javax.swing.JTextField();
        lname = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        uname = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        type = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        pass = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        num = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        sq = new javax.swing.JComboBox<>();
        ans = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        u_image = new javax.swing.JLabel();
        select = new javax.swing.JButton();
        remove = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        stat = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        uid = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setLayout(null);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setText("User Type:");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(50, 340, 80, 17);

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

        jLabel7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel7.setText("Last Name:");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(50, 140, 90, 17);

        jLabel9.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel9.setText("Email Address:");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(20, 210, 110, 30);

        fname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fnameActionPerformed(evt);
            }
        });
        jPanel1.add(fname);
        fname.setBounds(130, 90, 210, 30);

        lname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lnameActionPerformed(evt);
            }
        });
        jPanel1.add(lname);
        lname.setBounds(130, 130, 210, 30);

        jLabel10.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel10.setText("Username:");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(50, 170, 90, 30);

        uname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unameActionPerformed(evt);
            }
        });
        jPanel1.add(uname);
        uname.setBounds(130, 170, 210, 30);

        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });
        jPanel1.add(email);
        email.setBounds(130, 210, 210, 30);

        type.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin", "Staff" }));
        jPanel1.add(type);
        type.setBounds(130, 330, 210, 30);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/icons8-register-20.png"))); // NOI18N
        jButton1.setText("ADD");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(440, 490, 100, 29);

        jLabel12.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel12.setText("STAFF");
        jPanel1.add(jLabel12);
        jLabel12.setBounds(20, 30, 120, 22);
        jPanel1.add(jSeparator1);
        jSeparator1.setBounds(90, 270, 0, 2);

        pass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passActionPerformed(evt);
            }
        });
        jPanel1.add(pass);
        pass.setBounds(130, 290, 210, 30);

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setText("Password:");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(50, 300, 80, 17);

        num.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numActionPerformed(evt);
            }
        });
        jPanel1.add(num);
        num.setBounds(130, 250, 210, 30);

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setText("Contact #:");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(50, 260, 80, 20);

        jLabel13.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel13.setText("First Name:");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(40, 100, 100, 17);
        jPanel1.add(jSeparator2);
        jSeparator2.setBounds(20, 50, 110, 10);

        sq.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "What's the name of your first pet?", "What's the lastname of your Mother?", "What's your favorite food?", "What's your favorite Color?", "What's your birth month?" }));
        jPanel1.add(sq);
        sq.setBounds(40, 380, 238, 22);
        jPanel1.add(ans);
        ans.setBounds(40, 410, 230, 30);

        u_image.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(u_image, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(u_image, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel11);
        jPanel11.setBounds(470, 80, 250, 210);

        select.setBackground(new java.awt.Color(255, 255, 255));
        select.setText("SELECT");
        select.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        select.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectActionPerformed(evt);
            }
        });
        jPanel1.add(select);
        select.setBounds(480, 300, 100, 30);

        remove.setBackground(new java.awt.Color(255, 255, 255));
        remove.setText("REMOVE");
        remove.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeActionPerformed(evt);
            }
        });
        jPanel1.add(remove);
        remove.setBounds(630, 300, 90, 30);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setLayout(null);

        stat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        stat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Active", "Pending" }));
        jPanel9.add(stat);
        stat.setBounds(90, 0, 190, 30);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("User Type");
        jPanel9.add(jLabel8);
        jLabel8.setBounds(0, 0, 90, 30);

        jPanel1.add(jPanel9);
        jPanel9.setBounds(140, 450, 330, 30);

        uid.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        uid.setEnabled(false);
        jPanel1.add(uid);
        uid.setBounds(140, 50, 190, 30);

        jLabel1.setText("UserID");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(70, 60, 38, 16);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 772, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void numActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_numActionPerformed

    private void fnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fnameActionPerformed

    private void lnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lnameActionPerformed

    private void unameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_unameActionPerformed

    private void emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    dbConnector dbc = new dbConnector();
    Session sess = Session.getInstance();  // Logged-in admin

    String fn = fname.getText().trim();
    String ln = lname.getText().trim();
    String username = uname.getText().trim();
    String password = pass.getText().trim();
    String mail = email.getText().trim();
    String status = stat.getSelectedItem().toString().trim();
    String ty = type.getSelectedItem().toString();
    String question = sq.getSelectedItem().toString();
    String answer = ans.getText().trim();

    if (selectedFile == null) {
        JOptionPane.showMessageDialog(null, "Please select an image.");
        return;
    }

    if (!selectedFile.exists() || !selectedFile.isFile()) {
        JOptionPane.showMessageDialog(null, "Invalid image file.");
        return;
    }

    String imageName = username + "_" + selectedFile.getName();
    String destinationDir = "src/usersimages";
    new File(destinationDir).mkdirs(); // Ensure folder exists
    String destinationPath = destinationDir + "/" + imageName;

    if (fn.isEmpty() || ln.isEmpty() || username.isEmpty() || password.isEmpty() ||
        mail.isEmpty() || question.isEmpty() || answer.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Please fill all fields.");
        return;
    } else if (password.length() < 8) {
        JOptionPane.showMessageDialog(null, "Password must be at least 8 characters.");
        return;
    } else if (!mail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
        JOptionPane.showMessageDialog(null, "Enter a valid email address.");
        return;
    } else if (duplicateCheck(username, mail)) {
        return;
    }

    try {
        String hashedPassword = passwordHasher.hashPassword(password);
        String hashedAnswer = passwordHasher.hashPassword(answer);

        // Generate u_num (e.g., U001, U002...)
        String uNum = generateUniqueUNum(dbc.getConnection());

        Files.copy(selectedFile.toPath(), new File(destinationPath).toPath(), StandardCopyOption.REPLACE_EXISTING);

        String insertQuery = "INSERT INTO tbl_users (u_num, u_fname, u_lname, u_username, u_status, u_password, u_email, u_type, u_image, security_question, security_answer) "
                           + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbc.getConnection();
             PreparedStatement pst = conn.prepareStatement(insertQuery)) {

            pst.setString(1, uNum);
            pst.setString(2, fn);
            pst.setString(3, ln);
            pst.setString(4, username);
            pst.setString(5, status);
            pst.setString(6, hashedPassword);
            pst.setString(7, mail);
            pst.setString(8, ty);
            pst.setString(9, imageName);
            pst.setString(10, question);
            pst.setString(11, hashedAnswer);

            int rowsInserted = pst.executeUpdate();

            if (rowsInserted > 0) {
                String logQuery = "INSERT INTO tbl_log (u_id, u_username, u_type, log_status, log_description) VALUES (?, ?, ?, ?, ?)";

                try (PreparedStatement logPst = conn.prepareStatement(logQuery)) {
                    logPst.setInt(1, sess.getUid());
                    logPst.setString(2, sess.getUsername());
                    logPst.setString(3, sess.getType());
                    logPst.setString(4, "Active");
                    logPst.setString(5, "Admin Added a New Account: " + username);
                    logPst.executeUpdate();
                }

                JOptionPane.showMessageDialog(null, "Registered Successfully!");
                new AdminProfile().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Registration Failed!");
            }
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void passActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passActionPerformed

    private void selectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                selectedFile = fileChooser.getSelectedFile();
                destination = "src/usersimages/" + selectedFile.getName();
                path  = selectedFile.getAbsolutePath();

                if(FileExistenceChecker(path) == 1){
                    JOptionPane.showMessageDialog(null, "File Already Exist, Rename or Choose another!");
                    destination = "";
                    path= "";
                }else{
                    u_image.setIcon(ResizeImage(path, null, u_image));
                    select.setEnabled(false);
                    remove.setEnabled(true);
                }
            } catch (Exception ex) {
                System.out.println("File Error!");
            }
        }
    }//GEN-LAST:event_selectActionPerformed

    private void removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeActionPerformed
        remove.setEnabled(false);
        select.setEnabled(true);
        u_image.setIcon(null);
        destination = "";
        path = "";
    }//GEN-LAST:event_removeActionPerformed

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
            java.util.logging.Logger.getLogger(StaffAdd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StaffAdd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StaffAdd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StaffAdd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StaffAdd().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ans;
    private javax.swing.JTextField email;
    private javax.swing.JTextField fname;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField lname;
    private javax.swing.JTextField num;
    private javax.swing.JTextField pass;
    public javax.swing.JButton remove;
    public javax.swing.JButton select;
    private javax.swing.JComboBox<String> sq;
    public javax.swing.JComboBox<String> stat;
    private javax.swing.JComboBox<String> type;
    private javax.swing.JLabel u_image;
    public javax.swing.JTextField uid;
    private javax.swing.JTextField uname;
    // End of variables declaration//GEN-END:variables
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;

/**
 *
 * @author w10
 */
public class StaffEdit extends javax.swing.JFrame {
    public int id;
    public String bar;
    public String currentUser;
    /**
     * Creates new form logindash
     */
    public StaffEdit() {
        initComponents();
    }
    
    public void accessTable(TableModel model, int rowid){
        id = Integer.parseInt(model.getValueAt(rowid, 0).toString());
        currentUser = model.getValueAt(rowid, 3).toString();
        
        fname.setText(model.getValueAt(rowid, 1).toString());
        lname.setText(model.getValueAt(rowid, 2).toString());
        uname.setText(currentUser);
        email.setText(model.getValueAt(rowid, 4).toString());
        num.setText(model.getValueAt(rowid, 5).toString());
        pass.setText(model.getValueAt(rowid, 6).toString());
        
        if((model.getValueAt(rowid, 7).toString()).equals("Admin")){
            type.setSelectedItem("Admin");
        }else{
            type.setSelectedItem("Staff");
        }
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
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        fname = new javax.swing.JTextField();
        lname = new javax.swing.JTextField();
        uname = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        num = new javax.swing.JTextField();
        pass = new javax.swing.JTextField();
        type = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        status = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        uid = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        sq = new javax.swing.JComboBox<>();
        ans = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        stat = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        u_image = new javax.swing.JLabel();
        select = new javax.swing.JButton();
        remove = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setLayout(null);

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
        jPanel1.add(jSeparator1);
        jSeparator1.setBounds(90, 270, 0, 2);
        jPanel1.add(jSeparator2);
        jSeparator2.setBounds(30, 130, 110, 10);

        jLabel12.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel12.setText("STAFF");
        jPanel1.add(jLabel12);
        jLabel12.setBounds(30, 110, 120, 22);

        jLabel13.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel13.setText("First Name:");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(40, 210, 100, 17);

        jLabel7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel7.setText("Last Name:");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(40, 250, 100, 17);

        jLabel10.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel10.setText("Username:");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(40, 270, 90, 50);

        jLabel9.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel9.setText("Email Address:");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(40, 310, 110, 50);

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setText("Contact #:");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(40, 370, 100, 20);

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setText("Password:");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(40, 410, 100, 17);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setText("User Type:");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(40, 450, 100, 17);

        fname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fnameActionPerformed(evt);
            }
        });
        jPanel1.add(fname);
        fname.setBounds(170, 200, 210, 30);

        lname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lnameActionPerformed(evt);
            }
        });
        jPanel1.add(lname);
        lname.setBounds(170, 240, 210, 30);

        uname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unameActionPerformed(evt);
            }
        });
        jPanel1.add(uname);
        uname.setBounds(170, 280, 210, 30);

        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });
        jPanel1.add(email);
        email.setBounds(170, 320, 210, 30);

        num.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numActionPerformed(evt);
            }
        });
        jPanel1.add(num);
        num.setBounds(170, 360, 210, 30);

        pass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passActionPerformed(evt);
            }
        });
        jPanel1.add(pass);
        pass.setBounds(170, 400, 210, 30);

        type.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin", "Staff" }));
        jPanel1.add(type);
        type.setBounds(170, 440, 210, 30);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/icons8-register-20.png"))); // NOI18N
        jButton1.setText("Edit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(530, 630, 100, 29);

        status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Active", "Inactive" }));
        jPanel1.add(status);
        status.setBounds(170, 480, 210, 30);

        jLabel8.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel8.setText("Status:");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(40, 490, 100, 17);

        jLabel1.setFont(new java.awt.Font("Segoe Print", 1, 24)); // NOI18N
        jLabel1.setText("PACALDO STAFF EDIT");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(jLabel1)
                .addContainerGap(64, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4);
        jPanel4.setBounds(150, 10, 400, 100);

        uid.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        uid.setEnabled(false);
        jPanel1.add(uid);
        uid.setBounds(170, 160, 210, 30);

        jLabel11.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel11.setText("UserID");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(40, 160, 80, 31);

        sq.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "What's the name of your first pet?", "What's the lastname of your Mother?", "What's your favorite food?", "What's your favorite Color?", "What's your birth month?" }));
        jPanel1.add(sq);
        sq.setBounds(50, 530, 238, 22);
        jPanel1.add(ans);
        ans.setBounds(50, 560, 230, 30);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setLayout(null);

        stat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        stat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Active", "Pending" }));
        jPanel9.add(stat);
        stat.setBounds(90, 0, 190, 30);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("User Type");
        jPanel9.add(jLabel14);
        jLabel14.setBounds(0, 0, 90, 30);

        jPanel1.add(jPanel9);
        jPanel9.setBounds(290, 550, 330, 30);

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
        jPanel11.setBounds(470, 200, 250, 210);

        select.setBackground(new java.awt.Color(255, 255, 255));
        select.setText("SELECT");
        select.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        select.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectActionPerformed(evt);
            }
        });
        jPanel1.add(select);
        select.setBounds(470, 420, 100, 30);

        remove.setBackground(new java.awt.Color(255, 255, 255));
        remove.setText("REMOVE");
        remove.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeActionPerformed(evt);
            }
        });
        jPanel1.add(remove);
        remove.setBounds(630, 420, 90, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 831, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 698, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

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

    private void numActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_numActionPerformed

    private void passActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passActionPerformed

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

// Assuming selectedFile is set when the user selects an image via the select button
if (selectedFile == null) {
    JOptionPane.showMessageDialog(null, "Please select an image.");
    return;
}

// Check if the selected file is a valid file
if (!selectedFile.exists() || !selectedFile.isFile()) {
    JOptionPane.showMessageDialog(null, "Invalid image file.");
    return;
}

String imageName = username + "_" + selectedFile.getName();
String destinationDir = "src/usersimages";
new File(destinationDir).mkdirs();  // Ensure folder exists

// Ensure destination path is valid
String destinationPath = destinationDir + "/" + imageName;


// Validation
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
    return; // Already shown warning inside duplicateCheck
}

try {
    // Hash passwords and answers
    String hashedPassword = passwordHasher.hashPassword(password);
    String hashedAnswer = passwordHasher.hashPassword(answer);

    // Save image to folder
    Files.copy(selectedFile.toPath(), new File(destinationPath).toPath(), StandardCopyOption.REPLACE_EXISTING);

    // Insert user into DB
    String insertQuery = "INSERT INTO tbl_users (u_fname, u_lname, u_username, u_status, u_password, u_email, u_type, u_image, security_question, security_answer) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = dbc.getConnection();
         PreparedStatement pst = conn.prepareStatement(insertQuery)) {

        pst.setString(1, fn);
        pst.setString(2, ln);
        pst.setString(3, username);
        pst.setString(4, status);
        pst.setString(5, hashedPassword);
        pst.setString(6, mail);
        pst.setString(7, ty);
        pst.setString(8, imageName);
        pst.setString(9, question);
        pst.setString(10, hashedAnswer);

        int rowsInserted = pst.executeUpdate();

        if (rowsInserted > 0) {
            // Insert log entry
            String logQuery = "INSERT INTO tbl_log (u_id, u_username, u_type, log_status, log_description) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement logPst = conn.prepareStatement(logQuery)) {
                logPst.setInt(1, sess.getUid());  // User ID of the admin performing the action
                logPst.setString(2, sess.getUsername()); // Admin username
                logPst.setString(3, sess.getType()); // Assuming you have this in session
                logPst.setString(4, "Active"); // You can adjust this as needed
                logPst.setString(5, "Admin Added or Edited a User Account: " + username); // Description of the action
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
            java.util.logging.Logger.getLogger(StaffEdit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StaffEdit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StaffEdit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StaffEdit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new StaffEdit().setVisible(true);
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
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
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
    private javax.swing.JPanel jPanel4;
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
    private javax.swing.JComboBox<String> status;
    private javax.swing.JComboBox<String> type;
    private javax.swing.JLabel u_image;
    public javax.swing.JTextField uid;
    private javax.swing.JTextField uname;
    // End of variables declaration//GEN-END:variables
}

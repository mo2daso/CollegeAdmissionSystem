package collegeadmissionsystem;
import static com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Admin extends College {
    String username;
    String password;
    String AssignedCollege;
    int id; 
    int rNumber;
     ArrayList<String> studentList = new ArrayList<>();
    public Admin(String username, String AssignedCollege, int id) {
        this.username = username;
        this.AssignedCollege = AssignedCollege;
        this.id = id;
    }

    public Admin(int id, int rNumber) {
        this.id = rNumber;
        this.id = id;
    }

    Admin() {
    
    }
    @Override
    boolean ViewDetails() {
     try {
        // Create the SQL queries
        String tableName = "stdInfo";
        String query = "SELECT * FROM " + tableName;
        String tableName2 = "stdEduInfo";
        String query2 = "SELECT * FROM " + tableName2;
        // Create a DBConnector object
        DBConnector db = new DBConnector();
        // Execute the queries and get the result sets
        ResultSet rs = db.RunSelect(query);
        ResultSet rs2 = db.RunSelect(query2);
        // Clear the existing data in the ArrayList
        studentList.clear();
        // Loop through the result sets and populate the ArrayList
        while (rs.next() && rs2.next()) {
            // Retrieve the data from the result sets
            String id = rs.getString("ID");
            String studentName = rs.getString("name");
            String cnicNo = rs.getString("CNICNo");
            String referredCollege = rs2.getString("ReferredCollege");
            String assignCollege = rs.getString("AssignedCollege");
            String gender = rs.getString("Gender");

            // Create a formatted string with the retrieved data
            String studentData = "" + id +
                    ",  " + studentName +
                    ",   " + cnicNo +
                    ",  " + referredCollege +
                    ",  " + gender +
                    ",  " +assignCollege;
            // Add the student data to the ArrayList
            studentList.add(studentData);            
        }

        // Close the result sets
        rs.close();
        rs2.close();

        return true; // Data retrieval and ArrayList population successful

    } catch (SQLException e) {
        System.out.println(e.getMessage());
        return false; // Data retrieval or ArrayList population failed
    }
    }
    
    ArrayList<String> getStudentList() {
        ViewDetails();
        return studentList;
}
    void displaydata()
    {
        
            ViewDetails();
            for (String studentData : studentList) {
            System.out.println("hello");
            System.out.println(studentData);
            
    }
    }

public boolean AdminLogin(String username, String password) {
    boolean loggedIn = false;

    try {
        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        Connection conn = DriverManager.getConnection("jdbc:ucanaccess://ClgAdmission1.accdb");
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Admin WHERE Username = ? AND Password = ?");
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            loggedIn = true;
            // You can perform additional actions here if needed
        }

        rs.close();
        ps.close();
        conn.close();
    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
    }

    return loggedIn;
}
        @Override
    String CheckStatus() {
   try {
       
       ArrayList<String> collegeNames1 = getCollegeNames();                    
        // Create the SQL query
        String updateQuery = "UPDATE stdInfo SET AssignedCollege = ? WHERE ID = ?";
        String insertQuery = "INSERT INTO CollegeTable (Students, RollNo) VALUES (?, ?)";

        // Create a DBConnector object
        DBConnector db = new DBConnector();
        Connection conn = DriverManager.getConnection("jdbc:ucanaccess://ClgAdmission1.accdb");
        PreparedStatement updatePs = conn.prepareStatement(updateQuery);
        PreparedStatement insertPs = null;

        // Set the parameters for the update statement
        updatePs.setString(1, AssignedCollege);
        updatePs.setInt(2, id);

        // Execute the update
        int rowsAffected = updatePs.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("AssignedCollege updated successfully for student ID: " + id);

            // Determine the table name based on the assigned college
            String collegeTableName = "";
//          for (String assignedCollege : collegeNames1) {
//                if (AssignedCollege.equals(assignedCollege)) {
//                    collegeTableName = assignedCollege;
//                    System.out.println("AssignedCollege found in ArrayList: " + assignedCollege);
//                    break;
//                }
//                else {
//                System.out.println("Invalid college specified.");
//                return null;
//            }
//}
            if (AssignedCollege.equals("Dehli")) {
                collegeTableName = "Dehli";
            } else if (AssignedCollege.equals("Formen")) {
                collegeTableName = "Formen";
            } else if (AssignedCollege.equals("Gulshan")) {
                collegeTableName = "Gulshan";
            } else {
                System.out.println("Invalid college specified.");
                return null;
            }

            // Retrieve student's name from the stdInfo table using the ID
            String selectQuery = "SELECT name , Gender , FatherName FROM stdInfo WHERE ID = ?";
            PreparedStatement selectPs = conn.prepareStatement(selectQuery);
            selectPs.setInt(1, id);
            ResultSet resultSet = selectPs.executeQuery();

            if (resultSet.next()) {
                String studentName = resultSet.getString("name");
                String Gender = resultSet.getString("Gender");
                String fName = resultSet.getString("FatherName");
                
                // Create a new PreparedStatement for the insert statement
                String insertStatement = "INSERT INTO " + collegeTableName + " (Students, RollNo,Gender,FatherName) VALUES (?,?,?,?)";
                insertPs = conn.prepareStatement(insertStatement);
                // Set the parameters for the insert statement
                insertPs.setString(1, studentName);
                insertPs.setInt(2, id);
                insertPs.setString(3, Gender);
                insertPs.setString(4, fName);
                // Execute the insert statement
                int rowsInserted = insertPs.executeUpdate();
                if (rowsInserted > 0) 
                {
                    System.out.println("Enrolled student ID: " + id + " (" + studentName + ") in " + AssignedCollege + " successfully.");
                }
                else
                {
                    System.out.println("Failed to enroll student ID: " + id + " (" + studentName + ") in " + AssignedCollege + ".");
                }
            }
            else {                
                    System.out.println("Student not found for ID: " + id);
                 }
            // Close the result set and select statement
            resultSet.close();
            selectPs.close();
        } else {
            System.out.println("Failed to update AssignedCollege for student ID: " + id);
        }

        // Close the prepared statements and connection
        updatePs.close();
        if (insertPs != null) {
            insertPs.close();
        }
        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return null;
    }

}


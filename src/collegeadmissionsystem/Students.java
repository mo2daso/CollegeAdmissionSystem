package collegeadmissionsystem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Students extends College
{
       //Personal Details
    private String name;
    private int cnicNo;
    private String fName;
    private String mName;
    private String emailAdd;
    private String phoneNo;
    private String gender;

    //FOR SEARCH 
    private int id;
    private int rNumber;

    //FOR EDUCATIONAL DETAILS
    private String passYear;
    private String rollNo;
    private String totalMarks;
    private String obtMarks;
    private String schoolName;
    private String boardName;
    private String districtName;
 
    public Students(String passYear,String rollNo, String totalMarks, String obtMarks, String schoolName, String boardName, String districtName)
    {
        this.passYear = passYear;
        this.rollNo=rollNo;
        this.totalMarks = totalMarks;
        this.obtMarks = obtMarks;
        this.schoolName = schoolName;
        this.boardName = boardName;
        this.districtName = districtName;
    }
    
    public Students(int id, int rNumber) {
            this.id = id;
            this.rNumber = rNumber;

        }
    
    public Students (String name, int id)
    {
        this.name = name;
        this.id = id;
    }
    
    public Students(String name , int cnicNo , String fName , String mName , String emailAdd,String phoneNo,String gender)
    {
        this.name = name;
        this.cnicNo=cnicNo;
        this.fName=fName;
        this.mName=mName;
        this.emailAdd=emailAdd;
        this.phoneNo=phoneNo;
        this.gender=gender;
    }
    @Override
    boolean ViewDetails() {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            Connection conn = DriverManager.getConnection("jdbc:ucanaccess://ClgAdmission1.accdb");
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM stdInfo WHERE Id = ?");
            PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM stdEduInfo WHERE RollNumber = ?");
            ps.setInt(1, id);
            ps1.setInt(1, rNumber);

            ResultSet rs = ps.executeQuery();
            ResultSet rs1 = ps1.executeQuery();
            if (rs.next() && rs1.next()) {
                String searchId = rs.getString("Id");
                String searchRollNo = rs1.getString("RollNumber");

                if (searchId.equals(String.valueOf(id)) && searchRollNo.equals(String.valueOf(rNumber))) {
                    String name1 = rs.getString("name");
                    String cNic = rs.getString("CNICNo");
                    String fName1 = rs.getString("FatherName");
                    String mName1 = rs.getString("MotherName");
                    String email = rs.getString("EmailAddress");
                    int pNo = rs.getInt("PhoneNumber");
                    String gender1 = rs.getString("Gender");

                    System.out.println("Name: " + name1);
                    System.out.println("CNIC No: " + cNic);
                    System.out.println("Father Name: " + fName1);
                    System.out.println("Mother Name: " + mName1);
                    System.out.println("Email Address: " + email);
                    System.out.println("Phone Number: " + pNo);
                    System.out.println("Gender: " + gender1);

                    int rollNo = rs1.getInt("RollNumber");
                    int pYear = rs1.getInt("PassYear");
                    int c2 = rs1.getInt("TotalMarks");
                    int c3 = rs1.getInt("ObtMarks");
                    String sName = rs1.getString("SchoolName");
                    String bName = rs1.getString("BoardName");
                    String dName = rs1.getString("DistrictName");

                    System.out.println("Roll Number: " + rollNo);
                    System.out.println("Pass Year: " + pYear);
                    float percent = (c3 * 100) / c2;
                    System.out.println("Percentage: " + percent);
                    String grade;
                    DisplayDetails form = new DisplayDetails();
                    // Retrieve college names and percentages from the College class
                    ArrayList<String> collegeNames = getCollegeNames();
                    ArrayList<String> collegePercentages = getCollegePercentage();

                    // Display the names and percentages of colleges
                    System.out.println("Suggested colleges:");
                    if (collegeNames.size() >= 3 && collegePercentages.size() >= 3) {
                        for (int i = 0; i < 3; i++) {
                    
                            String collegeName = collegeNames.get(i);
                            String collegePercentage = collegePercentages.get(i);
                            System.out.println(collegeName + " - " + collegePercentage);
                        
                        }
                    } 
                    
                    else {
                        System.out.println("Insufficient data to suggest colleges.");
                    }
                    if (percent >= 80) {
                        grade = "A+";
                        String college1 = collegeNames.size() >= 1 ? collegeNames.get(0) : "None";
                        String college2 = collegeNames.size() >= 2 ? collegeNames.get(1) : "None";
                        String college3 = collegeNames.size() >= 3 ? collegeNames.get(2) : "None";
                        form.setcollege(college1, college2, college3);
                    } else if (percent >= 75) {
                        grade = "A";
                        String college2 = collegeNames.size() >= 2 ? collegeNames.get(1) : "None";
                        String college3 = collegeNames.size() >= 3 ? collegeNames.get(2) : "None";
                        form.setcollege(college2, college3, "None");
                    } else if (percent >= 70) {
                        grade = "B";
                        String college3 = collegeNames.size() >= 3 ? collegeNames.get(2) : "None";
                        form.setcollege(college3, "None", "None");
                    } else if (percent >= 60) {
                        grade = "C";
                        form.setcollege("None", "None", "None");
                    } else if (percent >= 50) {
                        grade = "D";
                        form.setcollege("None", "None", "None");
                    } else {
                        grade = "Fail";
                        form.setcollege("None", "None", "None");
                    }
                    System.out.println("Grade: " + grade);
                    System.out.println("School Name: " + sName);
                    System.out.println("Board Name: " + bName);
                    System.out.println("District Name: " + dName);
                    form.setFields(name1, cNic, fName1, mName1, email, pNo, gender1, rollNo, pYear, percent, grade, sName, bName, dName);
                    form.setVisible(true);
                    
                    
                    return true;
                } else {
                    System.out.println("No Data Found!");
                    return false;
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }


    @Override
    String CheckStatus() {
       int id2 = id;
    String username2 = name; 
    // Execute the SQL query
    String query = "SELECT `AssignedCollege` FROM stdInfo WHERE Id = '" + id + "' AND name = '" + username2 + "';";
    // Assuming you are using JDBC to interact with the database
    try (
        Connection connection = DriverManager.getConnection("jdbc:ucanaccess://ClgAdmission1.accdb");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query)
    ) {
        if (resultSet.next()) {
            String assignedCollege = resultSet.getString("AssignedCollege");
            return assignedCollege;
        } else {
            return "Entry not found";
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return "An error occurred";
    }
    }


    public boolean saveDataToDatabase() {
        try {
            
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            Connection conn = DriverManager.getConnection("jdbc:ucanaccess://ClgAdmission1.accdb");
            String query = "INSERT INTO stdEduInfo (RollNumber,PassYear, TotalMarks, ObtMarks, SchoolName, BoardName, DistrictName) VALUES (?,?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, rollNo);
            ps.setString(2, passYear);
            ps.setString(3, totalMarks);
            ps.setString(4, obtMarks);
            ps.setString(5, schoolName);
            ps.setString(6, boardName);
            ps.setString(7, districtName);
                    
            int rowsAffected = ps.executeUpdate();
            conn.commit();
            
            ps.close();
            conn.close();
            
            return rowsAffected > 0;
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveDataToDatabase2() {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            Connection conn = DriverManager.getConnection("jdbc:ucanaccess://ClgAdmission1.accdb");
            String query = "INSERT INTO stdInfo (name, CNICNo, FatherName, MotherName, EmailAddress, PhoneNumber, Gender) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, name);
            ps.setInt(2, cnicNo);
            ps.setString(3, fName);
            ps.setString(4, mName);
            ps.setString(5, emailAdd);
            ps.setString(6, phoneNo);
            ps.setString(7, gender);

            int rowsAffected = ps.executeUpdate();
            conn.commit();

            ps.close();
            conn.close();

            return rowsAffected > 0;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    // Getters for the private fields
    
    public String getRollNo() {
        return rollNo;
    }

    public String getPassYear() {
        return passYear;
    }

    public String getTotalMarks() {
        return totalMarks;
    }

    public String getObtMarks() {
        return obtMarks;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getBoardName() {
        return boardName;
    }

    public String getDistrictName() {
        return districtName;

    }
    
    public String getCollegeName(int totMarks, int obtMarks, int rollNo) throws ClassNotFoundException, SQLException {
    float percent = (obtMarks * 100) / totMarks;
    String grade;
        System.out.println(percent);
    String referredCollege = null;

    Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
    Connection conn = DriverManager.getConnection("jdbc:ucanaccess://ClgAdmission1.accdb");
    PreparedStatement ps = conn.prepareStatement("SELECT * FROM stdEduInfo WHERE RollNumber = ?");
    ps.setInt(1, rollNo);
    ResultSet rs = ps.executeQuery();

    if (rs.next()) {
        String searchRoll = rs.getString("RollNumber");
        ArrayList<String> collegeNames = getCollegeNames();
        ArrayList<String> collegePercentages = getCollegePercentage();

        if (collegeNames.size() >= 3 && collegePercentages.size() >= 3) {
            for (int i = 0; i < 3; i++) {
                String collegeName = collegeNames.get(i);
                String collegePercentage = collegePercentages.get(i);
                System.out.println(collegeName + " - " + collegePercentage);
            }
        } else {
            System.out.println("Insufficient data to suggest colleges.");
        }

        ArrayList<String> referredColleges = new ArrayList<>();

        if (percent >= 80) {
            grade = "A+";
            if (collegeNames.size() >= 1) {
                referredColleges.add(collegeNames.get(0));
            }
            if (collegeNames.size() >= 2) {
                referredColleges.add(collegeNames.get(1));
            }
            if (collegeNames.size() >= 3) {
                referredColleges.add(collegeNames.get(2));
            }
        } else if (percent >= 75) {
            grade = "A";
            if (collegeNames.size() >= 2) {
                referredColleges.add(collegeNames.get(1));
            }
            if (collegeNames.size() >= 3) {
                referredColleges.add(collegeNames.get(2));
            }
        } else if (percent >= 70) {
            grade = "B";
            if (collegeNames.size() >= 3) {
                referredColleges.add(collegeNames.get(2));
            }
        } else if (percent >= 60) {
            grade = "C";
        } else if (percent >= 50) {
            grade = "D";
        } else {
            grade = "Fail";
        }

        // Convert the referredColleges list to a comma-separated string
        referredCollege = String.join(",", referredColleges);
    }

    // Update the ReferredCollege column in the database
    PreparedStatement updatePs = conn.prepareStatement("UPDATE stdEduInfo SET ReferredCollege = ? WHERE RollNumber = ?");
    updatePs.setString(1, referredCollege);
    updatePs.setInt(2, rollNo);
    updatePs.executeUpdate();
    PreparedStatement updatePs2 = conn.prepareStatement("UPDATE stdEduInfo SET Percentage = ? WHERE RollNumber = ?");
    updatePs2.setFloat(1, percent);
    updatePs2.setInt(2, rollNo);
    updatePs2.executeUpdate();
    return referredCollege;
    
}

}
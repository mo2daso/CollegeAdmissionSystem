package collegeadmissionsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PersonDetails {
    private String name;
    private String cnicNo;
    private String fName;
    private String mName;
    private String emailAdd;
    private String phoneNo;
    private String gender;

    public PersonDetails(String name, String cnicNo, String fName, String mName, String emailAdd, String phoneNo, String gender) {
        this.name = name;
        this.cnicNo = cnicNo;
        this.fName = fName;
        this.mName = mName;
        this.emailAdd = emailAdd;
        this.phoneNo = phoneNo;
        this.gender = gender;
    }

    public boolean saveDataToDatabase() {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            Connection conn = DriverManager.getConnection("jdbc:ucanaccess://ClgAdmission1.accdb");
            String query = "INSERT INTO stdInfo (name, CNICNo, FatherName, MotherName, EmailAddress, PhoneNumber, Gender) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, cnicNo);
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
}

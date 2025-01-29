package collegeadmissionsystem;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class  College {
    private int id;
//    ArrayList<String> seats;
    ArrayList<String>percentage;
    ArrayList<String> name;
 public College() {
//        seats = new ArrayList<>();
        percentage = new ArrayList<>();
        name = new ArrayList<>();
    }    
   abstract boolean ViewDetails ();
   abstract String CheckStatus();
   private void Displaycolleges() {
    DBConnector dbConnector = new DBConnector();
    String query = "SELECT CollegeName,ReqPercentage FROM College";
    ResultSet resultSet = dbConnector.RunSelect(query);
    try {
        while (resultSet.next()) {
            String collegeName = resultSet.getString("CollegeName");
            String collegePercentage = resultSet.getString("ReqPercentage");
            percentage.add(collegePercentage);
            name.add(collegeName);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } 
}
    public ArrayList<String> getCollegeNames() {
    Displaycolleges();
        return name;
}

    public ArrayList<String> getCollegePercentage() {
    Displaycolleges();
        return percentage;
}
}

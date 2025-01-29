package collegeadmissionsystem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnector {
    //dbLoc = location of database
    private static final String dbLoc = "jdbc:ucanaccess://ClgAdmission1.accdb";
    private Connection conn;
    private PreparedStatement prepState;
    private ResultSet rSet;
    
    public DBConnector(){
        try{
            conn= DriverManager.getConnection(dbLoc);
            System.out.println("Connection Established");
        }
        catch(SQLException e){
            System.out.println(e);
            System.out.println("Connection Not Established");            
        }
    }
    //ResultSet for select query
    public ResultSet RunSelect(String query){
        try {
            prepState=conn.prepareStatement(query);
            rSet=prepState.executeQuery();//method for select statements
        }
        catch(SQLException e){
            //System.out.println("Record Not Found!");
            System.out.println(e);
        }
        return rSet;
    }
    
    public void RunDML(String query){
        try {
            prepState=conn.prepareStatement(query);
            prepState.executeUpdate();
        }
        catch(SQLException e){
            //System.out.println("Record Not Found!");
            System.out.println(e);
        }
        
    }
    
}

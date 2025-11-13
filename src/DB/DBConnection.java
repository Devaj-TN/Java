package DB;
import java.sql.*;

public class DBConnection {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/attendance_DB", 
                "root", 
                "root"
            );
            System.out.println("Database Connected");
        } catch(Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
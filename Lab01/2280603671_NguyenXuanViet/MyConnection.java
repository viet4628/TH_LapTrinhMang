import java.sql.*;
import javax.swing.*;

public class MyConnection {
    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/quanlytaikhoan?user=root&password=";
            Connection conn = DriverManager.getConnection(url);
            return conn;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection error: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
    }
}
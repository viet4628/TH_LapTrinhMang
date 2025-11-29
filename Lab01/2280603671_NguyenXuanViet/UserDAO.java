import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private DBAccess dbAccess;
    
    public UserDAO() {
        dbAccess = new DBAccess();
    }
    
    // Đăng ký người dùng mới
    public boolean register(User user) {
        try {
            String sql = "INSERT INTO users (username, password, role) VALUES ('" 
                        + user.getUsername() + "', '" 
                        + user.getPassword() + "', '" 
                        + user.getRole() + "')";
            System.out.println("Executing SQL: " + sql);
            int result = dbAccess.Update(sql);
            System.out.println("Result: " + result);
            return result > 0;
        } catch (Exception e) {
            System.err.println("Register error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Đăng nhập
    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username='" + username 
                    + "' AND password='" + password + "'";
        try {
            ResultSet rs = dbAccess.Query(sql);
            if (rs != null && rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                );
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Kiểm tra username đã tồn tại
    public boolean isUsernameExists(String username) {
        String sql = "SELECT * FROM users WHERE username='" + username + "'";
        try {
            ResultSet rs = dbAccess.Query(sql);
            if (rs != null && rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Lấy tất cả người dùng
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try {
            ResultSet rs = dbAccess.Query(sql);
            while (rs != null && rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                );
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
    
    // Cập nhật người dùng
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET username='" + user.getUsername() 
                    + "', password='" + user.getPassword() 
                    + "', role='" + user.getRole() 
                    + "' WHERE id=" + user.getId();
        int result = dbAccess.Update(sql);
        return result > 0;
    }
    
    // Xóa người dùng
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id=" + id;
        int result = dbAccess.Update(sql);
        return result > 0;
    }
    
    // Đặt lại mật khẩu (Forgot Password)
    public boolean resetPassword(String username, String hashedPassword) {
        try {
            String sql = "UPDATE users SET password='" + hashedPassword 
                        + "' WHERE username='" + username + "'";
            System.out.println("Resetting password for: " + username);
            int result = dbAccess.Update(sql);
            System.out.println("Result: " + result);
            return result > 0;
        } catch (Exception e) {
            System.err.println("Reset password error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

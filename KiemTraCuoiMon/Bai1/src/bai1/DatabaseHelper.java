/*
 * Class DatabaseHelper - Quản lý kết nối và thao tác với CSDL MySQL
 */
package bai1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class DatabaseHelper {
    // Cấu hình kết nối MySQL
    private static final String DB_NAME = "ql_nguoi_dung";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/" + DB_NAME;
    private static final String DB_URL_NO_DB = "jdbc:mysql://localhost:3306/";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // Mật khẩu mặc định là rỗng
    private Connection connection;

    public DatabaseHelper() {
        try {
            // Tải driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Tạo database nếu chưa tồn tại
            taoDatabase();
            
            // Kết nối đến database
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Tạo bảng nếu chưa tồn tại
            taoTableNguoiDung();
            
            System.out.println("Kết nối MySQL thành công!");
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy driver MySQL: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối CSDL: " + e.getMessage());
        }
    }

    // Tạo database nếu chưa tồn tại
    private void taoDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL_NO_DB, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            System.out.println("Database đã sẵn sàng!");
        } catch (SQLException e) {
            System.err.println("Lỗi tạo database: " + e.getMessage());
        }
    }

    // Tạo bảng người dùng nếu chưa tồn tại
    private void taoTableNguoiDung() {
        String sql = "CREATE TABLE IF NOT EXISTS nguoi_dung (" +
                     "id INT PRIMARY KEY AUTO_INCREMENT," +
                     "ten_dang_nhap VARCHAR(100) UNIQUE NOT NULL," +
                     "mat_khau VARCHAR(255) NOT NULL," +
                     "duong_dan_thu_muc VARCHAR(500)," +
                     "quyen_truy_xuat VARCHAR(50))";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Lỗi tạo bảng: " + e.getMessage());
        }
    }

    // Đăng ký người dùng mới
    public boolean dangKy(NguoiDung nguoiDung) {
        String sql = "INSERT INTO nguoi_dung (ten_dang_nhap, mat_khau, duong_dan_thu_muc, quyen_truy_xuat) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nguoiDung.getTenDangNhap());
            pstmt.setString(2, nguoiDung.getMatKhau());
            pstmt.setString(3, nguoiDung.getDuongDanThuMuc());
            pstmt.setString(4, nguoiDung.getQuyenTruyXuat());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi đăng ký: " + e.getMessage());
            return false;
        }
    }

    // Đăng nhập - kiểm tra tên đăng nhập và mật khẩu
    public NguoiDung dangNhap(String tenDangNhap, String matKhau) {
        String sql = "SELECT * FROM nguoi_dung WHERE ten_dang_nhap = ? AND mat_khau = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, tenDangNhap);
            pstmt.setString(2, matKhau);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new NguoiDung(
                    rs.getInt("id"),
                    rs.getString("ten_dang_nhap"),
                    rs.getString("mat_khau"),
                    rs.getString("duong_dan_thu_muc"),
                    rs.getString("quyen_truy_xuat")
                );
            }
        } catch (SQLException e) {
            System.err.println("Lỗi đăng nhập: " + e.getMessage());
        }
        return null;
    }

    // Kiểm tra tên đăng nhập đã tồn tại chưa
    public boolean kiemTraTonTai(String tenDangNhap) {
        String sql = "SELECT COUNT(*) FROM nguoi_dung WHERE ten_dang_nhap = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, tenDangNhap);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra: " + e.getMessage());
        }
        return false;
    }

    // Lấy danh sách tất cả người dùng
    public List<NguoiDung> layDanhSachNguoiDung() {
        List<NguoiDung> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM nguoi_dung";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                NguoiDung nd = new NguoiDung(
                    rs.getInt("id"),
                    rs.getString("ten_dang_nhap"),
                    rs.getString("mat_khau"),
                    rs.getString("duong_dan_thu_muc"),
                    rs.getString("quyen_truy_xuat")
                );
                danhSach.add(nd);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách: " + e.getMessage());
        }
        return danhSach;
    }

    // Xóa người dùng theo ID
    public boolean xoaNguoiDung(int id) {
        String sql = "DELETE FROM nguoi_dung WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa: " + e.getMessage());
            return false;
        }
    }

    // Cập nhật thông tin người dùng
    public boolean capNhatNguoiDung(NguoiDung nguoiDung) {
        String sql = "UPDATE nguoi_dung SET ten_dang_nhap = ?, mat_khau = ?, duong_dan_thu_muc = ?, quyen_truy_xuat = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nguoiDung.getTenDangNhap());
            pstmt.setString(2, nguoiDung.getMatKhau());
            pstmt.setString(3, nguoiDung.getDuongDanThuMuc());
            pstmt.setString(4, nguoiDung.getQuyenTruyXuat());
            pstmt.setInt(5, nguoiDung.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật: " + e.getMessage());
            return false;
        }
    }

    // Đóng kết nối
    public void dongKetNoi() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Lỗi đóng kết nối: " + e.getMessage());
        }
    }
}

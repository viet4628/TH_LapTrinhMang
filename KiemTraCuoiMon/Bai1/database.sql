-- Tạo Database quản lý người dùng
-- Tạo database
CREATE DATABASE IF NOT EXISTS ql_nguoi_dung;

-- Sử dụng database
USE ql_nguoi_dung;

-- Tạo bảng người dùng
CREATE TABLE IF NOT EXISTS nguoi_dung (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ten_dang_nhap VARCHAR(100) UNIQUE NOT NULL,
    mat_khau VARCHAR(255) NOT NULL,
    duong_dan_thu_muc VARCHAR(500),
    quyen_truy_xuat VARCHAR(50)
);

-- Thêm một số dữ liệu mẫu (tùy chọn)
INSERT INTO nguoi_dung (ten_dang_nhap, mat_khau, duong_dan_thu_muc, quyen_truy_xuat) VALUES
('admin', '123456', 'C:/Users/Admin', 'Cả hai'),
('user1', 'password1', 'C:/Users/User1', 'Đọc'),
('user2', 'password2', 'C:/Users/User2', 'Viết');

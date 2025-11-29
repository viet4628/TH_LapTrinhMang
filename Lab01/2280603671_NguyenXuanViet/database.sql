-- Tạo database
CREATE DATABASE IF NOT EXISTS quanlytaikhoan;

-- Sử dụng database
USE quanlytaikhoan;

-- Tạo bảng users
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'read',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Thêm dữ liệu mẫu
INSERT INTO users (username, password, role) VALUES
('admin', 'admin', 'full'),
('user1', '123', 'write'),
('user2', '123', 'read');

-- Hiển thị dữ liệu
SELECT * FROM users;

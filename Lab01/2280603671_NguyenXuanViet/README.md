# Hệ thống Quản lý Người dùng - Java Swing

## Mô tả
Ứng dụng Java Swing quản lý người dùng với kết nối MySQL, hỗ trợ các chức năng:
- Đăng ký tài khoản mới
- Đăng nhập hệ thống
- Quản lý người dùng (CRUD)
- Phân quyền: Read, Write, Full

## Cấu trúc Project
```
├── MainForm.java          # Form chính - Menu điều hướng
├── LoginForm.java         # Form đăng nhập
├── RegisterForm.java      # Form đăng ký
├── UserManagementForm.java # Form quản lý người dùng
├── User.java              # Model người dùng
├── UserDAO.java           # Data Access Object
├── DBAccess.java          # Lớp truy cập database
├── MyConnection.java      # Kết nối MySQL
└── database.sql           # Script tạo database
```

## Yêu cầu
- JDK 8 trở lên
- MySQL Server
- MySQL Connector/J (JDBC Driver)

## Cài đặt

### 1. Tạo Database
Chạy file `database.sql` trong MySQL:
```bash
mysql -u root -p < database.sql
```

Hoặc mở MySQL Workbench/phpMyAdmin và chạy các lệnh trong file.

### 2. Cấu hình kết nối
Mở file `MyConnection.java` và chỉnh sửa thông tin kết nối:
```java
String url = "jdbc:mysql://localhost:3306/quanlytaikhoan?user=root&password=";
```
Thay đổi `password=` thành mật khẩu MySQL của bạn.

### 3. Thêm MySQL Connector
Download MySQL Connector/J từ: https://dev.mysql.com/downloads/connector/j/

Thêm file `.jar` vào classpath:
```bash
javac -cp .;mysql-connector-java-x.x.xx.jar *.java
java -cp .;mysql-connector-java-x.x.xx.jar LoginForm
```

## Chạy ứng dụng

### Biên dịch:
```bash
javac *.java
```

### Chạy:
```bash
java MainForm
```

Hoặc chạy trực tiếp các form riêng lẻ:
```bash
java LoginForm
java RegisterForm
```

## Tài khoản mặc định
- **Admin**: 
  - Username: `admin`
  - Password: `admin`
  - Quyền: `full` (toàn quyền)

- **User1**: 
  - Username: `user1`
  - Password: `123`
  - Quyền: `write` (xem và thêm)

- **User2**: 
  - Username: `user2`
  - Password: `123`
  - Quyền: `read` (chỉ xem)

## Phân quyền

### Read (Chỉ đọc)
- Xem danh sách người dùng
- Không thể thêm, sửa, xóa

### Write (Đọc/Ghi)
- Xem danh sách người dùng
- Đăng ký người dùng mới
- Không thể xóa người dùng

### Full (Toàn quyền)
- Xem danh sách người dùng
- Đăng ký người dùng mới
- Xóa người dùng (trừ tài khoản đang đăng nhập)

## Chức năng

### MainForm (Form chính)
- Giao diện menu chính đẹp mắt với 4 tùy chọn:
  - **Đăng nhập**: Mở form đăng nhập
  - **Đăng ký tài khoản mới**: Mở form đăng ký
  - **Quản lý hệ thống (Admin)**: Yêu cầu mật khẩu admin để truy cập
  - **Thoát**: Đóng ứng dụng
- Hiệu ứng hover trên các nút
- Điều hướng giữa các form
- Quay lại menu khi đóng các form con

### LoginForm
- Đăng nhập với username/password
- Chuyển đến UserManagementForm sau khi đăng nhập thành công

### RegisterForm
- Đăng ký tài khoản mới
- Kiểm tra username trùng lặp
- Chọn quyền cho tài khoản

### UserManagementForm
- Hiển thị danh sách người dùng trong bảng
- Nút "Đăng ký": Mở RegisterForm
- Nút "Đăng nhập": Quay lại LoginForm
- Nút "Thoát": Xóa người dùng đã chọn (chỉ với quyền Full)
- Nút "Refresh": Làm mới danh sách
- Nút "Đăng xuất": Đăng xuất và quay lại LoginForm
- Radio buttons hiển thị quyền của user đã chọn

## Lưu ý
- Mật khẩu được lưu dạng plain text (nên mã hóa trong thực tế)
- Không thể xóa tài khoản đang đăng nhập
- Username phải ít nhất 3 ký tự
- Password phải ít nhất 3 ký tự

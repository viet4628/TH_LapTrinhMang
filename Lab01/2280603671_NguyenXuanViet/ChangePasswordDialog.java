import java.awt.*;
import javax.swing.*;

public class ChangePasswordDialog extends JDialog {
    private JPasswordField txtOldPassword;
    private JPasswordField txtNewPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnSave, btnCancel;
    private UserDAO userDAO;
    private User currentUser;
    
    public ChangePasswordDialog(JFrame parent, User currentUser, UserDAO userDAO) {
        super(parent, "Đổi mật khẩu", true);
        this.currentUser = currentUser;
        this.userDAO = userDAO;
        initComponents();
    }
    
    private void initComponents() {
        setSize(450, 400);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        // Panel chính
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Mật khẩu cũ
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Mật khẩu cũ:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtOldPassword = new JPasswordField(20);
        mainPanel.add(txtOldPassword, gbc);
        
        // Mật khẩu mới
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Mật khẩu mới:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtNewPassword = new JPasswordField(20);
        mainPanel.add(txtNewPassword, gbc);
        
        // Xác nhận mật khẩu mới
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Xác nhận mật khẩu:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtConfirmPassword = new JPasswordField(20);
        mainPanel.add(txtConfirmPassword, gbc);
        
        // Yêu cầu mật khẩu
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JLabel lblRequirements = new JLabel(PasswordValidator.getPasswordRequirements());
        lblRequirements.setFont(new Font("Arial", Font.PLAIN, 10));
        lblRequirements.setForeground(new Color(100, 100, 100));
        mainPanel.add(lblRequirements, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Panel nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnSave = new JButton("Đổi mật khẩu");
        btnCancel = new JButton("Hủy");
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Xử lý sự kiện
        btnSave.addActionListener(e -> changePassword());
        btnCancel.addActionListener(e -> dispose());
    }
    
    private void changePassword() {
        String oldPassword = new String(txtOldPassword.getPassword());
        String newPassword = new String(txtNewPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        
        // Kiểm tra các trường không rỗng
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập đầy đủ thông tin!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Kiểm tra mật khẩu cũ
        String hashedOldPassword = PasswordValidator.hashPassword(oldPassword);
        if (!hashedOldPassword.equals(currentUser.getPassword())) {
            JOptionPane.showMessageDialog(this,
                "Mật khẩu cũ không đúng!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Kiểm tra mật khẩu mới khác mật khẩu cũ
        if (oldPassword.equals(newPassword)) {
            JOptionPane.showMessageDialog(this,
                "Mật khẩu mới phải khác mật khẩu cũ!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Kiểm tra mật khẩu mới và xác nhận khớp
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                "Mật khẩu mới và xác nhận không khớp!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Kiểm tra mật khẩu mạnh
        String passwordError = PasswordValidator.validatePassword(newPassword);
        if (passwordError != null) {
            JOptionPane.showMessageDialog(this,
                passwordError,
                "Mật khẩu không hợp lệ",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Hiển thị CAPTCHA
        if (!Captcha.showCaptchaDialog(this, "Xác nhận đổi mật khẩu")) {
            return;
        }
        
        // Mã hóa và cập nhật mật khẩu
        String hashedNewPassword = PasswordValidator.hashPassword(newPassword);
        currentUser.setPassword(hashedNewPassword);
        
        if (userDAO.updateUser(currentUser)) {
            JOptionPane.showMessageDialog(this,
                "Đổi mật khẩu thành công!",
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Đổi mật khẩu thất bại!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}

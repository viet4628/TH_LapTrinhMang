import java.awt.*;
import javax.swing.*;

public class ForgotPasswordForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtNewPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnReset, btnBack;
    private UserDAO userDAO;
    private MainForm mainForm;
    
    public ForgotPasswordForm(MainForm mainForm) {
        this.mainForm = mainForm;
        userDAO = new UserDAO();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Quên mật khẩu");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        // Panel tiêu đề
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(231, 76, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel("QUÊN MẬT KHẨU");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Panel chính
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtUsername = new JTextField(20);
        mainPanel.add(txtUsername, gbc);
        
        // Mật khẩu mới
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Mật khẩu mới:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtNewPassword = new JPasswordField(20);
        mainPanel.add(txtNewPassword, gbc);
        
        // Xác nhận mật khẩu
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
        btnReset = new JButton("Đặt lại mật khẩu");
        btnReset.setFont(new Font("Arial", Font.BOLD, 14));
        btnReset.setBackground(new Color(231, 76, 60));
        btnReset.setForeground(Color.WHITE);
        btnReset.setFocusPainted(false);
        
        btnBack = new JButton("Quay lại");
        btnBack.setFont(new Font("Arial", Font.BOLD, 14));
        
        buttonPanel.add(btnReset);
        buttonPanel.add(btnBack);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Xử lý sự kiện
        btnReset.addActionListener(e -> resetPassword());
        btnBack.addActionListener(e -> {
            dispose();
            mainForm.setVisible(true);
        });
    }
    
    private void resetPassword() {
        String username = txtUsername.getText().trim();
        String newPassword = new String(txtNewPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        
        // Kiểm tra các trường không rỗng
        if (username.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập đầy đủ thông tin!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Kiểm tra username có tồn tại không
        if (!userDAO.isUsernameExists(username)) {
            JOptionPane.showMessageDialog(this,
                "Username không tồn tại!",
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
        
        // Hiển thị CAPTCHA để xác thực
        if (!Captcha.showCaptchaDialog(this, "Xác nhận bạn không phải robot")) {
            JOptionPane.showMessageDialog(this,
                "Không thể đặt lại mật khẩu. Vui lòng thử lại!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Mã hóa mật khẩu mới
        String hashedPassword = PasswordValidator.hashPassword(newPassword);
        
        // Cập nhật mật khẩu trong database
        if (userDAO.resetPassword(username, hashedPassword)) {
            JOptionPane.showMessageDialog(this,
                "Đặt lại mật khẩu thành công!\nBạn có thể đăng nhập với mật khẩu mới.",
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Quay lại trang chính
            dispose();
            mainForm.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                "Đặt lại mật khẩu thất bại! Vui lòng thử lại.",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainForm().setVisible(true);
        });
    }
}

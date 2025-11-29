import java.awt.*;
import javax.swing.*;

public class AddUserDialog extends JDialog {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cboRole;
    private JButton btnSave, btnCancel;
    private UserDAO userDAO;
    
    public AddUserDialog(JFrame parent, UserDAO userDAO) {
        super(parent, "Thêm người dùng mới", true);
        this.userDAO = userDAO;
        initComponents();
    }
    
    private void initComponents() {
        setSize(450, 380);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
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
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtPassword = new JPasswordField(20);
        mainPanel.add(txtPassword, gbc);
        
        // Yêu cầu mật khẩu
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JLabel lblRequirements = new JLabel(PasswordValidator.getPasswordRequirements());
        lblRequirements.setFont(new Font("Arial", Font.PLAIN, 9));
        lblRequirements.setForeground(new Color(100, 100, 100));
        mainPanel.add(lblRequirements, gbc);
        
        // Role
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Quyền:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        String[] roles = {"read", "write", "full"};
        cboRole = new JComboBox<>(roles);
        mainPanel.add(cboRole, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Panel nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Xử lý sự kiện
        btnSave.addActionListener(e -> saveUser());
        btnCancel.addActionListener(e -> dispose());
    }
    
    private void saveUser() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String role = (String) cboRole.getSelectedItem();
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập đầy đủ thông tin!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Kiểm tra username hợp lệ
        String usernameError = PasswordValidator.validateUsername(username);
        if (usernameError != null) {
            JOptionPane.showMessageDialog(this, 
                usernameError, 
                "Username không hợp lệ", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Kiểm tra mật khẩu mạnh
        String passwordError = PasswordValidator.validatePassword(password);
        if (passwordError != null) {
            JOptionPane.showMessageDialog(this, 
                passwordError, 
                "Mật khẩu không hợp lệ", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (userDAO.isUsernameExists(username)) {
            JOptionPane.showMessageDialog(this, 
                "Username đã tồn tại!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Mã hóa mật khẩu
        String hashedPassword = PasswordValidator.hashPassword(password);
        User user = new User(username, hashedPassword, role);
        if (userDAO.register(user)) {
            JOptionPane.showMessageDialog(this, 
                "Thêm người dùng thành công!", 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Thêm người dùng thất bại!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnRegister, btnExit;
    private UserDAO userDAO;
    private MainForm mainForm;
    
    public RegisterForm(MainForm mainForm) {
        this.mainForm = mainForm;
        userDAO = new UserDAO();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Đăng ký");
        setSize(450, 320);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
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
        
        // Yêu cầu username
        gbc.gridx = 1;
        gbc.gridy = 1;
        JLabel lblUsernameReq = new JLabel("<html><i>VD: user123, admin1</i></html>");
        lblUsernameReq.setFont(new Font("Arial", Font.PLAIN, 9));
        lblUsernameReq.setForeground(new Color(120, 120, 120));
        mainPanel.add(lblUsernameReq, gbc);
        
        // Password
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtPassword = new JPasswordField(20);
        mainPanel.add(txtPassword, gbc);
        
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
        btnRegister = new JButton("Đăng ký");
        btnExit = new JButton("Quay lại");
        
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnExit);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Xử lý sự kiện
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });
        
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                mainForm.setVisible(true);
            }
        });
    }
    
    private void register() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String role = "read"; // Mặc định quyền read cho user mới
        
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
        
        // Kiểm tra username đã tồn tại
        if (userDAO.isUsernameExists(username)) {
            JOptionPane.showMessageDialog(this, 
                "Username đã tồn tại!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Mã hóa mật khẩu
        String hashedPassword = PasswordValidator.hashPassword(password);
        
        // Đăng ký
        User user = new User(username, hashedPassword, role);
        try {
            if (userDAO.register(user)) {
                JOptionPane.showMessageDialog(this, 
                    "Đăng ký thành công!", 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Đăng ký thất bại! Vui lòng kiểm tra kết nối database và xem console để biết chi tiết lỗi.", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void clearForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtUsername.requestFocus();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainForm().setVisible(true);
        });
    }
}

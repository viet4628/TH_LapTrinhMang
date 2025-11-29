import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnExit;
    private UserDAO userDAO;
    private MainForm mainForm;
    
    public LoginForm(MainForm mainForm) {
        this.mainForm = mainForm;
        userDAO = new UserDAO();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Đăng nhập");
        setSize(400, 200);
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
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtPassword = new JPasswordField(20);
        mainPanel.add(txtPassword, gbc);
        
        // Link quên mật khẩu
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblForgotPassword = new JLabel("<html><a href=''>Quên mật khẩu?</a></html>");
        lblForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblForgotPassword.setForeground(new Color(52, 152, 219));
        lblForgotPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openForgotPasswordForm();
            }
        });
        mainPanel.add(lblForgotPassword, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Panel nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnLogin = new JButton("Đăng nhập");
        btnExit = new JButton("Quay lại");
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnExit);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Xử lý sự kiện
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                mainForm.setVisible(true);
            }
        });
        
        // Enter để đăng nhập
        txtPassword.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        });
    }
    
    private void openForgotPasswordForm() {
        this.dispose();
        ForgotPasswordForm forgotForm = new ForgotPasswordForm(mainForm);
        forgotForm.setVisible(true);
    }
    
    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập đầy đủ thông tin!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Mã hóa mật khẩu trước khi kiểm tra
        String hashedPassword = PasswordValidator.hashPassword(password);
        User user = userDAO.login(username, hashedPassword);
        if (user != null) {
            JOptionPane.showMessageDialog(this, 
                "Đăng nhập thành công!\nQuyền: " + user.getRole(), 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Mở form quản lý
            this.dispose();
            UserManagementForm managementForm = new UserManagementForm(user, mainForm);
            managementForm.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Sai tên đăng nhập hoặc mật khẩu!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtUsername.requestFocus();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainForm().setVisible(true);
        });
    }
}

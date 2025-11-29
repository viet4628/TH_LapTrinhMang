import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainForm extends JFrame {
    private JButton btnLogin, btnRegister, btnExit;
    private JLabel lblTitle, lblWelcome;
    
    public MainForm() {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Hệ thống Quản lý Người dùng");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        // Panel tiêu đề
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 20, 20));
        headerPanel.setBackground(new Color(52, 152, 219));
        
        lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ NGƯỜI DÙNG");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        lblWelcome = new JLabel("Chào mừng bạn đến với hệ thống");
        lblWelcome.setFont(new Font("Arial", Font.PLAIN, 14));
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(lblTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(lblWelcome);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Panel chính - các nút chức năng
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        
        // Nút Đăng nhập
        btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setPreferredSize(new Dimension(300, 50));
        btnLogin.setBackground(new Color(46, 204, 113));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 0;
        mainPanel.add(btnLogin, gbc);
        
        // Nút Đăng ký
        btnRegister = new JButton("Đăng ký tài khoản mới");
        btnRegister.setFont(new Font("Arial", Font.BOLD, 16));
        btnRegister.setPreferredSize(new Dimension(300, 50));
        btnRegister.setBackground(new Color(52, 152, 219));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 1;
        mainPanel.add(btnRegister, gbc);
        
        // Nút Thoát
        btnExit = new JButton("Thoát");
        btnExit.setFont(new Font("Arial", Font.BOLD, 16));
        btnExit.setPreferredSize(new Dimension(300, 50));
        btnExit.setBackground(new Color(231, 76, 60));
        btnExit.setForeground(Color.WHITE);
        btnExit.setFocusPainted(false);
        btnExit.setBorderPainted(false);
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 2;
        mainPanel.add(btnExit, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Panel footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel lblFooter = new JLabel("© 2024 - Hệ thống quản lý người dùng");
        lblFooter.setFont(new Font("Arial", Font.ITALIC, 11));
        lblFooter.setForeground(Color.GRAY);
        footerPanel.add(lblFooter);
        add(footerPanel, BorderLayout.SOUTH);
        
        // Xử lý sự kiện
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openLoginForm();
            }
        });
        
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openRegisterForm();
            }
        });
        
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitApplication();
            }
        });
        
        // Hiệu ứng hover cho các nút
        addHoverEffect(btnLogin, new Color(46, 204, 113), new Color(39, 174, 96));
        addHoverEffect(btnRegister, new Color(52, 152, 219), new Color(41, 128, 185));
        addHoverEffect(btnExit, new Color(231, 76, 60), new Color(192, 57, 43));
    }
    
    private void addHoverEffect(JButton button, Color normalColor, Color hoverColor) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
            }
        });
    }
    
    private void openLoginForm() {
        LoginForm loginForm = new LoginForm(this);
        loginForm.setVisible(true);
        this.setVisible(false);
    }
    
    private void openRegisterForm() {
        RegisterForm registerForm = new RegisterForm(this);
        registerForm.setVisible(true);
        this.setVisible(false);
    }
    
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn thoát ứng dụng?",
            "Xác nhận thoát",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        // Thiết lập Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }
}

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UserManagementForm extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh, btnChangePassword, btnLogout;
    private JRadioButton rbRead, rbWrite, rbFull;
    private ButtonGroup roleGroup;
    private UserDAO userDAO;
    private User currentUser;
    private MainForm mainForm;
    
    public UserManagementForm(User currentUser, MainForm mainForm) {
        this.currentUser = currentUser;
        this.mainForm = mainForm;
        userDAO = new UserDAO();
        initComponents();
        loadData();
        setPermissions();
    }
    
    private void initComponents() {
        setTitle("Hệ thống quản lý người dùng - User: " + currentUser.getUsername());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        // Panel trên - Quyền
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("Quyền"));
        
        rbRead = new JRadioButton("Read");
        rbWrite = new JRadioButton("Write");
        rbFull = new JRadioButton("Full");
        
        roleGroup = new ButtonGroup();
        roleGroup.add(rbRead);
        roleGroup.add(rbWrite);
        roleGroup.add(rbFull);
        
        topPanel.add(rbRead);
        topPanel.add(rbWrite);
        topPanel.add(rbFull);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Panel giữa - Bảng
        String[] columnNames = {"ID", "Username", "Password", "Role"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách người dùng"));
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel dưới - Nút chức năng
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnAdd = new JButton("Thêm user");
        btnEdit = new JButton("Sửa user");
        btnDelete = new JButton("Xóa user");
        btnRefresh = new JButton("Làm mới");
        btnChangePassword = new JButton("Đổi mật khẩu");
        btnLogout = new JButton("Đăng xuất");
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnChangePassword);
        buttonPanel.add(btnLogout);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Xử lý sự kiện
        btnAdd.addActionListener(e -> addUser());
        btnEdit.addActionListener(e -> editUser());
        btnDelete.addActionListener(e -> deleteUser());
        btnRefresh.addActionListener(e -> loadData());
        btnChangePassword.addActionListener(e -> changePassword());
        btnLogout.addActionListener(e -> logout());
        
        // Double click vào bảng để chọn quyền
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        String role = (String) tableModel.getValueAt(row, 3);
                        selectRole(role);
                    }
                }
            }
        });
    }
    
    private void setPermissions() {
        String role = currentUser.getRole().toLowerCase();
        
        if (role.equals("read")) {
            // Chỉ có quyền xem
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            rbRead.setEnabled(false);
            rbWrite.setEnabled(false);
            rbFull.setEnabled(false);
        } else if (role.equals("write")) {
            // Có quyền xem, không thể quản lý user
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            rbRead.setEnabled(false);
            rbWrite.setEnabled(false);
            rbFull.setEnabled(false);
        } else if (role.equals("full")) {
            // Chỉ admin mới có quyền quản lý user
            btnAdd.setEnabled(true);
            btnEdit.setEnabled(true);
            btnDelete.setEnabled(true);
            rbRead.setEnabled(true);
            rbWrite.setEnabled(true);
            rbFull.setEnabled(true);
        }
        
        // Tất cả user đều có thể đổi mật khẩu của chính mình
        btnChangePassword.setEnabled(true);
    }
    
    private void selectRole(String role) {
        switch (role.toLowerCase()) {
            case "read":
                rbRead.setSelected(true);
                break;
            case "write":
                rbWrite.setSelected(true);
                break;
            case "full":
                rbFull.setSelected(true);
                break;
        }
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<User> users = userDAO.getAllUsers();
        for (User user : users) {
            Object[] row = {
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole()
            };
            tableModel.addRow(row);
        }
    }
    
    private void addUser() {
        AddUserDialog dialog = new AddUserDialog(this, userDAO);
        dialog.setVisible(true);
        loadData();
    }
    
    private void editUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn người dùng cần sửa!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        String password = (String) tableModel.getValueAt(selectedRow, 2);
        String role = (String) tableModel.getValueAt(selectedRow, 3);
        
        User user = new User(id, username, password, role);
        EditUserDialog dialog = new EditUserDialog(this, userDAO, user);
        dialog.setVisible(true);
        loadData();
    }
    
    private void deleteUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn người dùng cần xóa!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        
        // Không cho xóa chính mình
        if (username.equals(currentUser.getUsername())) {
            JOptionPane.showMessageDialog(this,
                "Không thể xóa tài khoản đang đăng nhập!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn xóa người dùng '" + username + "'?",
            "Xác nhận",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (userDAO.deleteUser(id)) {
                JOptionPane.showMessageDialog(this,
                    "Xóa thành công!",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Xóa thất bại!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void changePassword() {
        ChangePasswordDialog dialog = new ChangePasswordDialog(this, currentUser, userDAO);
        dialog.setVisible(true);
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn đăng xuất?",
            "Xác nhận",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            mainForm.setVisible(true);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainForm().setVisible(true);
        });
    }
}

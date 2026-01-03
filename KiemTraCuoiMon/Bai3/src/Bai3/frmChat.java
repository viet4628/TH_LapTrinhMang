/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bai3;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import javax.swing.JOptionPane;

/**
 * Ứng dụng Chat Multicast - Cho phép nhiều máy chat với nhau cùng lúc
 * Sử dụng địa chỉ Multicast để broadcast tin nhắn đến tất cả thành viên
 * 
 * @author Administrator
 */
public class frmChat extends javax.swing.JFrame {

    private MulticastSocket socket;
    private InetAddress group;
    private Thread receiveThread;
    private boolean isRunning = false;
    private int port = 1234;
    private String nick = "";
    
    /**
     * Creates new form frmChat
     */
    public frmChat() {
        initComponents();
        setLocationRelativeTo(null);
        
        // Disable các nút khi chưa kết nối
        btnsend.setEnabled(false);
        txtnoidung.setEnabled(false);
        btndong.setEnabled(false);
    }
    
    /**
     * Kết nối vào nhóm Multicast
     */
    private void connect() {
        try {
            String groupAddress = txtgroup.getText().trim();
            port = Integer.parseInt(txtport.getText().trim());
            nick = txtnick.getText().trim();
            
            // Kiểm tra dữ liệu
            if (groupAddress.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập địa chỉ Multicast!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (nick.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập Nick!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (port < 1 || port > 65535) {
                JOptionPane.showMessageDialog(this, "Port phải từ 1 đến 65535!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Tạo MulticastSocket
            socket = new MulticastSocket(port);
            
            // Lấy địa chỉ nhóm Multicast
            group = InetAddress.getByName(groupAddress);
            
            // Tham gia vào nhóm Multicast
            socket.joinGroup(group);
            
            isRunning = true;
            
            // Cập nhật giao diện
            btnketnoi.setEnabled(false);
            txtgroup.setEnabled(false);
            txtport.setEnabled(false);
            txtnick.setEnabled(false);
            btnsend.setEnabled(true);
            txtnoidung.setEnabled(true);
            btndong.setEnabled(true);
            
            appendMessage("[Hệ thống] Đã kết nối vào nhóm " + groupAddress + ":" + port + "\n");
            appendMessage("[Hệ thống] Nick của bạn: " + nick + "\n");
            appendMessage("-------------------------------------------\n");
            
            // Gửi thông báo tham gia
            sendMessage(nick + " đã tham gia phòng chat!");
            
            // Khởi động thread nhận tin nhắn
            startReceiveThread();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Port không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Ngắt kết nối khỏi nhóm Multicast
     */
    private void disconnect() {
        try {
            if (isRunning) {
                // Gửi thông báo rời phòng
                sendMessage(nick + " đã rời phòng chat!");
                
                isRunning = false;
                
                // Rời khỏi nhóm Multicast
                if (socket != null && group != null) {
                    socket.leaveGroup(group);
                    socket.close();
                }
                
                // Cập nhật giao diện
                btnketnoi.setEnabled(true);
                txtgroup.setEnabled(true);
                txtport.setEnabled(true);
                txtnick.setEnabled(true);
                btnsend.setEnabled(false);
                txtnoidung.setEnabled(false);
                btndong.setEnabled(false);
                
                appendMessage("[Hệ thống] Đã ngắt kết nối.\n");
                appendMessage("-------------------------------------------\n");
            }
        } catch (Exception e) {
            appendMessage("[Lỗi] " + e.getMessage() + "\n");
        }
    }
    
    /**
     * Khởi động thread nhận tin nhắn từ nhóm Multicast
     */
    private void startReceiveThread() {
        receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] buffer = new byte[1024];
                
                while (isRunning) {
                    try {
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);
                        
                        String message = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
                        
                        // Hiển thị tin nhắn
                        appendMessage(message + "\n");
                        
                    } catch (Exception e) {
                        if (isRunning) {
                            appendMessage("[Lỗi] " + e.getMessage() + "\n");
                        }
                    }
                }
            }
        });
        
        receiveThread.setDaemon(true);
        receiveThread.start();
    }
    
    /**
     * Gửi tin nhắn đến nhóm Multicast
     */
    private void sendMessage(String content) {
        try {
            if (socket != null && group != null && isRunning) {
                String message = "[" + nick + "]: " + content;
                byte[] data = message.getBytes("UTF-8");
                
                DatagramPacket packet = new DatagramPacket(data, data.length, group, port);
                socket.send(packet);
            }
        } catch (Exception e) {
            appendMessage("[Lỗi gửi] " + e.getMessage() + "\n");
        }
    }
    
    /**
     * Thêm tin nhắn vào vùng hiển thị chat (thread-safe)
     */
    private void appendMessage(final String message) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                txtchat.append(message);
                // Tự động scroll xuống cuối
                txtchat.setCaretPosition(txtchat.getDocument().getLength());
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblGroup = new javax.swing.JLabel();
        txtgroup = new javax.swing.JTextField();
        lblPort = new javax.swing.JLabel();
        txtport = new javax.swing.JTextField();
        lblNick = new javax.swing.JLabel();
        txtnick = new javax.swing.JTextField();
        btnketnoi = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtchat = new javax.swing.JTextArea();
        btndong = new javax.swing.JButton();
        txtnoidung = new javax.swing.JTextField();
        btnsend = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chương trình chat dùng Multicast");

        lblGroup.setText("Nhạp địa chỉ Multicast");

        txtgroup.setText("239.0.0.36");

        lblPort.setText("Port");

        txtport.setText("1234");

        lblNick.setText("Nick");

        btnketnoi.setText("Kết nối");
        btnketnoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnketnoiActionPerformed(evt);
            }
        });

        txtchat.setEditable(false);
        txtchat.setColumns(20);
        txtchat.setRows(5);
        jScrollPane1.setViewportView(txtchat);

        btndong.setText("Đóng");
        btndong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndongActionPerformed(evt);
            }
        });

        btnsend.setText("Send");
        btnsend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblGroup)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtgroup, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblPort)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtport, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblNick)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtnick, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnketnoi, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btndong, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtnoidung)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnsend, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblGroup)
                            .addComponent(txtgroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPort)
                            .addComponent(txtport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNick)
                            .addComponent(txtnick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnketnoi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btndong)
                            .addComponent(txtnoidung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnsend)))
                    .addComponent(jScrollPane1))
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnketnoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnketnoiActionPerformed
        connect();
    }//GEN-LAST:event_btnketnoiActionPerformed

    private void btndongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndongActionPerformed
        disconnect();
    }//GEN-LAST:event_btndongActionPerformed

    private void btnsendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsendActionPerformed
        String content = txtnoidung.getText().trim();
        if (!content.isEmpty()) {
            sendMessage(content);
            txtnoidung.setText("");
        }
    }//GEN-LAST:event_btnsendActionPerformed

    private void txtnoidungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnoidungActionPerformed
        // Gửi tin nhắn khi nhấn Enter
        String content = txtnoidung.getText().trim();
        if (!content.isEmpty()) {
            sendMessage(content);
            txtnoidung.setText("");
        }
    }//GEN-LAST:event_txtnoidungActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // Ngắt kết nối khi đóng cửa sổ
        if (isRunning) {
            disconnect();
        }
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmChat().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btndong;
    private javax.swing.JButton btnketnoi;
    private javax.swing.JButton btnsend;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblGroup;
    private javax.swing.JLabel lblNick;
    private javax.swing.JLabel lblPort;
    private javax.swing.JTextArea txtchat;
    private javax.swing.JTextField txtgroup;
    private javax.swing.JTextField txtnick;
    private javax.swing.JTextField txtnoidung;
    private javax.swing.JTextField txtport;
    // End of variables declaration//GEN-END:variables
}

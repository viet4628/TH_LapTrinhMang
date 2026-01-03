package tcpchat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class frmChat extends javax.swing.JFrame {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Thread listenThread;
    private String myNickname;

    public frmChat() {
        initComponents();
        this.setLocationRelativeTo(null);
    }

    private void connectToServer() {
        try {
            String host = txtHost.getText().trim();
            int port = Integer.parseInt(txtPort.getText().trim());
            myNickname = txtNickname.getText().trim();
            String room = txtRoom.getText().trim();

            if (myNickname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập họ tên!");
                return;
            }
            
            if (room.isEmpty()) {
                room = "Phòng chung";
                txtRoom.setText(room);
            }

            socket = new Socket(host, port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            // Gửi lệnh gia nhập: JOIN|nickname|room
            out.writeUTF("JOIN|" + myNickname + "|" + room);
            out.flush();

            JOptionPane.showMessageDialog(this, "Đã kết nối thành công!");
            btnConnect.setEnabled(false);
            btnDisconnect.setEnabled(true);
            txtHost.setEditable(false);
            txtPort.setEditable(false);
            txtNickname.setEditable(false);
            txtRoom.setEditable(false);

            startListening();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối: " + e.getMessage());
        }
    }

    private void startListening() {
        listenThread = new Thread(() -> {
            try {
                while (true) {
                    String msg = in.readUTF();
                    // Nếu tin nhắn bắt đầu bằng nickname của mình và dấu ':', hiển thị bên phải
                    if (msg.startsWith(myNickname + ":")) {
                        appendMessage(msg, true);
                    } else if (msg.startsWith("---")) { 
                        // Tin nhắn hệ thống căn giữa
                        appendMessage(msg, false, true);
                    } else {
                        // Tin nhắn người khác hiển thị bên trái
                        appendMessage(msg, false);
                    }
                }
            } catch (IOException e) {
                appendMessage("Mất kết nối với Server.", false, true);
                SwingUtilities.invokeLater(() -> {
                    resetConnectionState();
                });
            }
        });
        listenThread.start();
    }

    private void disconnectFromServer() {
        try {
            if (out != null) {
                out.writeUTF("QUIT");
                out.flush();
            }
        } catch (IOException e) {
            // Ignore
        }
        
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            // Ignore
        }
        
        if (listenThread != null) {
            listenThread.interrupt();
        }
        
        resetConnectionState();
        appendMessage("--- Đã ngắt kết nối ---", false, true);
    }

    private void resetConnectionState() {
        btnConnect.setEnabled(true);
        btnDisconnect.setEnabled(false);
        txtHost.setEditable(true);
        txtPort.setEditable(true);
        txtNickname.setEditable(true);
        txtRoom.setEditable(true);
    }

    private void appendMessage(String msg, boolean isMe) {
        appendMessage(msg, isMe, false);
    }

    private void appendMessage(String msg, boolean isMe, boolean isSystem) {
        SwingUtilities.invokeLater(() -> {
            try {
                StyledDocument doc = txtChatPane.getStyledDocument();
                SimpleAttributeSet attrs = new SimpleAttributeSet();
                
                if (isSystem) {
                    StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_CENTER);
                    StyleConstants.setItalic(attrs, true);
                    StyleConstants.setForeground(attrs, Color.GRAY);
                } else if (isMe) {
                    StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_RIGHT);
                    StyleConstants.setForeground(attrs, new Color(0, 102, 204));
                    StyleConstants.setBold(attrs, true);
                } else {
                    StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_LEFT);
                    StyleConstants.setForeground(attrs, Color.BLACK);
                }

                int length = doc.getLength();
                doc.insertString(length, msg + "\n", attrs);
                doc.setParagraphAttributes(length, msg.length() + 1, attrs, false);
                
                txtChatPane.setCaretPosition(doc.getLength());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });
    }

    private void sendMessage() {
        String msg = txtMessage.getText().trim();
        if (msg.isEmpty())
            return;

        try {
            if (out != null) {
                out.writeUTF(msg);
                out.flush();
                txtMessage.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng kết nối trước!");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gửi tin thất bại: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtHost = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtPort = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtNickname = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtRoom = new javax.swing.JTextField();
        btnConnect = new javax.swing.JButton();
        btnDisconnect = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtChatPane = new javax.swing.JTextPane();
        txtMessage = new javax.swing.JTextField();
        btnSend = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TCP Chat Room");

        jLabel1.setText("Host:");
        txtHost.setText("localhost");

        jLabel2.setText("Port:");
        txtPort.setText("8888");

        jLabel3.setText("Ho ten:");

        jLabel4.setText("Phong:");
        txtRoom.setText("Phong chung");

        btnConnect.setText("Ket noi");
        btnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectActionPerformed(evt);
            }
        });

        btnDisconnect.setText("Ngat ket noi");
        btnDisconnect.setEnabled(false);
        btnDisconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisconnectActionPerformed(evt);
            }
        });

        txtChatPane.setEditable(false);
        txtChatPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Noi dung chat"));
        jScrollPane1.setViewportView(txtChatPane);

        txtMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMessageActionPerformed(evt);
            }
        });

        btnSend.setText("Gui");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtMessage)
                        .addGap(10, 10, 10)
                        .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtHost, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNickname, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(20, 20, 20)
                        .addComponent(btnConnect, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnDisconnect, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtNickname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(txtRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnConnect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDisconnect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectActionPerformed
        connectToServer();
    }//GEN-LAST:event_btnConnectActionPerformed

    private void btnDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisconnectActionPerformed
        disconnectFromServer();
    }//GEN-LAST:event_btnDisconnectActionPerformed

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        sendMessage();
    }//GEN-LAST:event_btnSendActionPerformed

    private void txtMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMessageActionPerformed
        sendMessage();
    }//GEN-LAST:event_txtMessageActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new frmChat().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConnect;
    private javax.swing.JButton btnDisconnect;
    private javax.swing.JButton btnSend;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane txtChatPane;
    private javax.swing.JTextField txtHost;
    private javax.swing.JTextField txtMessage;
    private javax.swing.JTextField txtNickname;
    private javax.swing.JTextField txtPort;
    private javax.swing.JTextField txtRoom;
    // End of variables declaration//GEN-END:variables
}



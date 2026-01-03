package tcpchat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

public class ClientHandler extends Thread {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String nickname;
    private String room;
    private Vector<ClientHandler> clients;

    public ClientHandler(Socket socket, Vector<ClientHandler> clients) {
        try {
            this.socket = socket;
            this.clients = clients;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            // Đọc thông tin gia nhập: JOIN|nickname|room
            String joinMsg = in.readUTF();
            if (joinMsg.startsWith("JOIN|")) {
                String[] parts = joinMsg.split("\\|");
                this.nickname = parts[1];
                this.room = parts[2];
                broadcast("--- " + nickname + " đã tham gia phòng " + room + " ---");
            }

            while (true) {
                String msg = in.readUTF();
                if (msg == null || msg.equals("exit"))
                    break;

                broadcast(nickname + ": " + msg);
            }
        } catch (IOException e) {
            System.out.println(nickname + " ngắt kết nối.");
        } finally {
            clients.remove(this);
            if (nickname != null) {
                broadcast("--- " + nickname + " đã thoát phòng chat ---");
            }
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    private void broadcast(String msg) {
        for (ClientHandler client : clients) {
            // Chỉ gửi cho người cùng phòng
            if (client.room != null && client.room.equals(this.room)) {
                try {
                    client.out.writeUTF(msg);
                    client.out.flush();
                } catch (IOException e) {
                }
            }
        }
        System.out.println("[" + room + "] " + msg);
    }
}

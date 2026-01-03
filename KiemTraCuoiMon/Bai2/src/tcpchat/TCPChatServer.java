package tcpchat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class TCPChatServer {
    private static Vector<ClientHandler> clients = new Vector<>();

    public static void main(String[] args) {
        int port = 8888;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("TCP Chat Server đang chạy tại cổng " + port + "...");
            System.out.println("Sẵn sàng cho các máy trong mạng LAN/Wifi kết nối.");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client mới kết nối: " + socket.getInetAddress());

                ClientHandler handler = new ClientHandler(socket, clients);
                clients.add(handler);
                handler.start();
            }
        } catch (IOException e) {
            System.err.println("Lỗi Server: " + e.getMessage());
        }
    }
}

package server;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ServerSender extends Thread {
    private DatagramSocket socket;
    private byte[] bytes = new byte[16384];
    private SocketAddress clientAddress;
    private int CLIENT_PORT;

    public ServerSender(DatagramSocket datagramSocket, InetSocketAddress clientAddress, int clientPort){
        this.clientAddress = clientAddress;
        this.socket = datagramSocket;
        CLIENT_PORT = clientPort;
    }

    @Override
    public void run(){
//        while (!isInterrupted()) {
//
//        }
    }

    public void send(String serverAnswer, InetAddress address, int port){
        try {
            bytes = serverAnswer.getBytes(StandardCharsets.UTF_8);
            DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, address, port);
            socket.send(datagramPacket);
            System.out.println(datagramPacket);
            System.out.println(clientAddress);
            System.out.println("----\nСообщение отправлено.\n----");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

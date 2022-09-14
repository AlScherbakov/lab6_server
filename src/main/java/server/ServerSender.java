package server;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ServerSender extends Thread {
    private final DatagramSocket socket;

    public ServerSender(DatagramSocket datagramSocket){
        this.socket = datagramSocket;
    }

    @Override
    public void run(){}

    public void send(String serverAnswer, InetAddress address, int port){
        try {
            byte[] bytes = serverAnswer.getBytes(StandardCharsets.UTF_8);
            DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, address, port);
            socket.send(datagramPacket);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

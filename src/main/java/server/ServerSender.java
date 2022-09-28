package server;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ServerSender extends Thread {
    private final DatagramSocket socket;
    private final String serverAnswer;
    private final InetAddress address;
    private final int port;

    public ServerSender(DatagramSocket datagramSocket, String serverAnswer, InetAddress address, int port){
        this.socket = datagramSocket;
        this.serverAnswer = serverAnswer;
        this.address = address;
        this.port = port;
    }

    @Override
    public void run(){
        try {
            byte[] bytes = serverAnswer.getBytes(StandardCharsets.UTF_8);
            DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, address, port);
            socket.send(datagramPacket);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            this.interrupt();
        }
    }
}

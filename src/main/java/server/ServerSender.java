package server;

import command.Receiver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

public class ServerSender extends Thread {
    private DatagramSocket socket;
    private DatagramChannel channel;
    private byte[] bytes = new byte[16384];
    private SocketAddress clientAddress;
    private int CLIENT_PORT;

//    public ServerSender(DatagramChannel datagramChannel, InetSocketAddress inetSocketAddress, int clientPort){
    public ServerSender(DatagramSocket datagramSocket, InetSocketAddress clientAddress, int clientPort){
        this.clientAddress = clientAddress;
//        this.channel = datagramChannel;
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
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
//            objectOutputStream.writeChars(serverAnswer);
//            objectOutputStream.flush();
//            ByteBuffer sendBuffer = ByteBuffer.allocate(16384);
//            sendBuffer.put(byteArrayOutputStream.toByteArray());
//            objectOutputStream.flush();
//            byteArrayOutputStream.flush();
//            sendBuffer.flip();
            bytes = serverAnswer.getBytes(StandardCharsets.UTF_8);
            DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, address, port);
            socket.send(datagramPacket);
            System.out.println(datagramPacket);
            System.out.println(clientAddress);
//            channel.send(sendBuffer, clientAddress);
            System.out.println("----\nСообщение отправлено.\n----");

//            objectOutputStream.close();
//            byteArrayOutputStream.close();
//            sendBuffer.clear();
//
//            bytes = byteArrayOutputStream.toByteArray();
//            byteArrayOutputStream.flush();
//            DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, clientAddress, CLIENT_PORT);
//            channel.send(datagramPacket);
//            System.out.println("----\nПакет отправлен.\n----");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

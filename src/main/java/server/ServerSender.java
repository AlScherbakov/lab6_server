package server;

import command.Receiver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerSender extends Thread {
    private DatagramSocket socket;
    private byte[] bytes = new byte[16384];
    private InetAddress clientAddress;

    public ServerSender(DatagramSocket datagramSocket, InetAddress inetAddress){
        this.clientAddress = inetAddress;
        this.socket = datagramSocket;
    }

    @Override
    public void run(){
//        while (!isInterrupted()) {
//
//        }
    }

    public void send(Receiver serverAnswer, int port){
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(serverAnswer);
            objectOutputStream.flush();
            bytes = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.flush();
            DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, clientAddress, port);
            socket.send(datagramPacket);
            System.out.println("----\nПакет отправлен.\n----");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

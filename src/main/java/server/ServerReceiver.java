package server;

import command.Command;
import command.CommandEnum;
import messages.AddElementMessage;
import messages.Message;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.stream.Stream;

public class ServerReceiver extends Thread{
    private DatagramSocket socket;
    private DatagramChannel channel;

    private Server server;
    private Object clientMessage;

//    public ServerReceiver(DatagramChannel datagramChannel, Server server){
    public ServerReceiver(DatagramSocket datagramSocket, Server server){
        this.socket = datagramSocket;
//        this.channel = datagramChannel;
        this.server = server;
    }

    private static String extractMessage(ByteBuffer buffer) {
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return new String(bytes);
    }

    @Override
    public void run(){
        while (!isInterrupted()){
            try {
                byte[] bytes = new byte[16384];

                DatagramPacket packet
                        = new DatagramPacket(bytes, bytes.length);
                socket.receive(packet);
//                InetAddress address = packet.getAddress();
//                int port = packet.getPort();
//                packet = new DatagramPacket(bytes, bytes.length, address, port);
//                String received
//                        = new String(packet.getData(), 0, packet.getLength());
//                System.out.println(received);
//                if (received.equals("end")) {
//                    running = false;
//                    continue;
//                }
//                socket.send(packet);

//                ByteBuffer receiveBuffer = ByteBuffer.allocate(16384);

//                DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
//                channel.receive(receiveBuffer);
//                socket.receive(datagramPacket);
//                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(receiveBuffer.array());
//                ObjectInputStream objectInputStream = new ObjectInputStream();
//                System.out.println(receiveBuffer);

//                clientMessage = objectInputStream.readObject();
//                System.out.println("clientMessage:" + clientMessage);
//                byteArrayInputStream.close();
//                objectInputStream.close();
//                if(clientMessage == null) return;
//                System.out.println("clientMessage:" + clientMessage);
//                String response = new CommandExecutor((Message) clientMessage).execute();
//                server.sender.send(response);
//
//                ByteBuffer receiveBuffer = ByteBuffer.allocate(16384);
//                channel.receive(receiveBuffer);
//                socket.receive(packet);
//                byte[] bytes = receiveBuffer.array();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                clientMessage = objectInputStream.readObject();
                byteArrayInputStream.close();
                objectInputStream.close();
                if(clientMessage == null) return;
                System.out.println("clientMessage:" + clientMessage);
                String response = new CommandExecutor((Message) clientMessage).execute();
                server.sender.send(response, packet.getAddress(), packet.getPort());

//                String received = extractMessage(receiveBuffer);
            } catch (IOException | ClassNotFoundException e){
                System.err.println(e.getMessage());
                run();
            }
        }
    }
}

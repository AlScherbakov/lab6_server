package server;

import command.Command;
import command.CommandEnum;
import command.Receiver;
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
    private Receiver state;

    public ServerReceiver(DatagramSocket datagramSocket, Server server, Receiver state){
        this.socket = datagramSocket;
        this.state = state;
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
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                clientMessage = objectInputStream.readObject();
                byteArrayInputStream.close();
                objectInputStream.close();
                if(clientMessage == null) return;
                String response = new CommandExecutor((Message) clientMessage, state).execute();
                server.sender.send(response, packet.getAddress(), packet.getPort());
            } catch (IOException | ClassNotFoundException e){
                System.err.println(e.getMessage());
                run();
            }
        }
    }
}

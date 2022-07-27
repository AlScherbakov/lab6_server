package server;

import command.Command;
import command.CommandEnum;
import messages.AddElementMessage;
import messages.Message;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerReceiver extends Thread{
    private DatagramSocket socket;
    private Server server;
    private Message clientMessage;

    public ServerReceiver(DatagramSocket datagramSocket, Server server){
        this.socket = datagramSocket;
        this.server = server;
    }

    @Override
    public void run(){
        while (!isInterrupted()){
            try {
                byte[] bytes = new byte[16384];
                DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
                socket.receive(datagramPacket);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                clientMessage = (Message) objectInputStream.readObject();
                System.out.println(clientMessage);
                CommandEnum commandName = clientMessage.getCommandName();
                System.out.println(commandName);
                if(commandName == CommandEnum.ADD){
                    System.out.println(((AddElementMessage) clientMessage).element);
                }
//                System.out.println(clientAnswer.getAnswer());
//                new Analyzator(reader,clientAnswer,datagramPacket.getPort());
//                server.setPorts(datagramPacket.getPort());
                byteArrayInputStream.close();
                objectInputStream.close();

            }catch (IOException | ClassNotFoundException e){
                System.err.println(e.getMessage());
            }
        }
    }
}

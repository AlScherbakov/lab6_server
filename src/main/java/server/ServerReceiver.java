package server;

import command.Receiver;
import messages.Message;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerReceiver extends Thread {
    private final ExecutorService pool;
    private DatagramSocket socket;
    private Server server;
    private Object clientMessage;
    private Receiver state;


    public ServerReceiver(DatagramSocket datagramSocket, Server server, Receiver state, int poolSize) {
        this.socket = datagramSocket;
        this.state = state;
        this.server = server;
        pool = Executors.newFixedThreadPool(poolSize);
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
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
                if (clientMessage == null) return;
                String response = new CommandExecutor((Message) clientMessage, state).execute();
                pool.execute(new ServerSender(socket, response, packet.getAddress(), packet.getPort()));
            } catch (IOException | ClassNotFoundException e) {
                System.err.println(e.getMessage());
                run();
            }
        }
    }
}

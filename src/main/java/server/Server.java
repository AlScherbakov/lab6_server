package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import command.CommandEnum;
import command.Receiver;
import util.DataInputSource;
import util.StudyGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.AccessDeniedException;
import java.util.*;

/**
 * Server class from Command pattern. Main process host. May be used for creation of several application instances
 */
public class Server {
    private final int PORT = 3333;
    private final Scanner scan;
    public ServerSender sender;
    private DatagramChannel datagramChannel;
    private DatagramSocket datagramSocket;
    private InetSocketAddress clientAddress;

    public Server(Scanner scan){
        this.scan = scan;
        try {
            datagramSocket = new DatagramSocket(PORT);
//            datagramChannel = DatagramChannel.open();
//            datagramChannel.configureBlocking(false);
            clientAddress = new InetSocketAddress("localhost", PORT);
//            datagramSocket.connect(clientAddress);
//            datagramChannel.connect(clientAddress);
//            sender = new ServerSender(datagramChannel, clientAddress, PORT);
            sender = new ServerSender(datagramSocket, clientAddress, PORT);
        } catch ( IOException e) {
            System.err.println(e.getMessage());
        }
    };
    boolean active = true;

// buffered reader, custom tag

    /**
     * run method
     * @throws IOException
     */
    public void run() throws IOException {
        String outputFilepath = System.getenv("lab5_data_filepath");
        Set<StudyGroup> groups = new TreeSet<>();
        Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy HH:mm:ss").create();
        String collectionInitializationDate = "";
        try {
            if (outputFilepath != null){
                File outputFile = new File(outputFilepath);
                if (!outputFile.exists()){
                    throw new FileNotFoundException("Файл " + outputFilepath + " не найден. Проверьте переменную окружения 'lab5_data_filepath'");
                } else if (!outputFile.canWrite()){
                    throw new AccessDeniedException("Нет права на запись в файл " + outputFilepath);
                } else if (!outputFile.canRead()){
                    throw new AccessDeniedException("Нет права чтение файла " + outputFilepath);
                } else {
                    FileReader dataFileReader = new FileReader(outputFilepath);
                    StudyGroup[] data = gson.fromJson(dataFileReader, StudyGroup[].class);
                    StudyGroup[] clearData = Arrays.stream(data).filter(StudyGroup::isValid).toArray(StudyGroup[]::new);
                    if(data.length != clearData.length){
                        System.err.println("Некоторые данные некорректны");
                    }
                    Collections.addAll(groups, clearData);
                    collectionInitializationDate = new Date(new File(outputFilepath).lastModified()).toString();
                }
            } else {
                throw new RuntimeException("Переменная окружения 'lab5_data_filepath' не задана");
            }
        } catch (FileNotFoundException | AccessDeniedException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("----\nСтарт работы.\n----");
        this.sender.start();
//        ServerReceiver receiver = new ServerReceiver(datagramChannel,this);
        ServerReceiver receiver = new ServerReceiver(datagramSocket,this);
        receiver.setDaemon(true);
        receiver.start();
        while (true){

        }
//        List<CommandEnum> history = new ArrayList<>();
//        System.out.println("Введите команду (help - помощь)");
//        DataInputSource inputSource = new DataInputSource(scan);
//        Receiver programState = new Receiver(groups, outputFilepath, history, true, inputSource, collectionInitializationDate);
//            while (programState.getWorking() && active){
//            String command = programState.getSource().get();
//            if (command.isEmpty()) {
//                programState.removeFirstReader();
//                continue;
//            };
//            Invoker invoker = new Invoker(programState);
//            programState = invoker.executeCommand(command);
//        }
    }

    /**
     * stop method
     */
    public void stop(){
        active = false;
    }
}

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
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.util.*;

/**
 * Server class from Command pattern. Main process host. May be used for creation of several application instances
 */
public class Server {
    private int PORT = -1;
    private final Scanner scan;
    public ServerSender sender;
    private DatagramSocket datagramSocket;
    private InetSocketAddress clientAddress;

    public Server(Scanner scan){
        this.scan = scan;
        setPort();
        System.out.println(PORT);
        System.out.println(datagramSocket);
        System.out.println(clientAddress);
    };
    boolean active = true;

    /**
     * run method
     * @throws IOException
     */

    public void setPort() {
        System.out.println("----\nУкажите порт для приёма подключений\n----");
        while (PORT == -1) {
            try {
                String numb = scan.nextLine();
                if (numb.matches("[0-9]+")) {
                    int portCandidate = Integer.parseInt(numb);
                    if (portCandidate < 65535 && portCandidate >= 0) {
                        PORT = portCandidate;
                        datagramSocket = new DatagramSocket(PORT);
                        clientAddress = new InetSocketAddress("localhost", PORT);
                        sender = new ServerSender(datagramSocket, clientAddress, PORT);
                    } else {
                        System.out.println("----\nНедопустимый номер порта, введите снова\n----");
                    }
                } else {
                    System.out.println("----\nНедопустимый номер порта, введите снова\n----");
                }
            } catch (SocketException e){
                System.err.println("Неизвестный хост\n" + e.getMessage());
            }
        }
    }

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
        List<CommandEnum> history = new ArrayList<>();
        DataInputSource inputSource = new DataInputSource(scan);
        Receiver programState = new Receiver(groups, outputFilepath, history, true, inputSource, collectionInitializationDate);
        ServerReceiver receiver = new ServerReceiver(datagramSocket,this, programState);
        receiver.setDaemon(true);
        receiver.start();
        while (true){

        }
    }

    /**
     * stop method
     */
    public void stop(){
        active = false;
    }
}

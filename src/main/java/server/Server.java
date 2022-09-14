package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import command.CommandEnum;
import command.Receiver;
import command.SaveCommand;
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
    private Receiver programState;

    public Server(Scanner scan){
        this.scan = scan;
        setPort();
    };
    boolean active = true;

    /**
     * run method
     * @throws IOException
     */

    public void setPort() {
        System.out.println("Укажите порт для приёма подключений:");
        while (PORT == -1) {
            try {
                String numb = scan.nextLine();
                if (numb.matches("[0-9]+")) {
                    int portCandidate = Integer.parseInt(numb);
                    if (portCandidate < 65535 && portCandidate >= 0) {
                        PORT = portCandidate;
                        datagramSocket = new DatagramSocket(PORT);
                        sender = new ServerSender(datagramSocket);
                    } else {
                        System.out.println("Недопустимый номер порта, попробуйте ещё раз:");
                    }
                } else {
                    System.out.println("Недопустимый номер порта, попробуйте ещё раз:");
                }
            } catch (SocketException e){
                System.err.println("Ошибка доступа к сокету");
            }
        }
    }

    public void run() {
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
        System.out.println("Старт работы");
        this.sender.start();
        List<CommandEnum> history = new ArrayList<>();
        DataInputSource inputSource = new DataInputSource(scan);
        programState = new Receiver(groups, outputFilepath, history, true, inputSource, collectionInitializationDate);
        ServerReceiver receiver = new ServerReceiver(datagramSocket,this, programState);
        receiver.setDaemon(true);
        receiver.start();
        while (programState.getWorking() && active){
            try{
                String rawCommand = programState.getSource().get();
                if(rawCommand.isEmpty()) continue;
                rawCommand = rawCommand.trim();
                if(rawCommand.matches("save")){
                    System.out.println(new SaveCommand(programState).execute());
                } else if (rawCommand.matches("exit")){
                    System.out.println(new SaveCommand(programState).execute());
                    System.exit(0);
                }
            } catch (IOException e){
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * stop method
     */
    public void stop(){
        System.out.println(new SaveCommand(programState).execute());
        active = false;
    }
}

package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import command.CommandEnum;
import command.Receiver;
import command.SaveCommand;
import org.apache.commons.lang3.exception.ExceptionUtils;
import util.DataInputSource;
import util.LocalDateDeserializer;
import util.LocalDateSerializer;
import util.StudyGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.file.AccessDeniedException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

/**
 * Server class from Command pattern. main.Main process host. May be used for creation of several application instances
 */
public class Server {
    private final Scanner scan;
    boolean active = true;
    private int PORT = -1;
    private DatagramSocket datagramSocket;
    private Receiver programState;
    private Connection connection;

    public Server(Scanner scan) {
        this.scan = scan;
        setPort();
        setDBConnection();
    }

    private void setDBConnection() {
        try {
            this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/alexandr", "postgres", "");
        } catch (SQLException e) {
            System.err.println("Ошибка при подключении к БД");
        }
    }

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
                    } else {
                        System.out.println("Недопустимый номер порта, попробуйте ещё раз:");
                    }
                } else {
                    System.out.println("Недопустимый номер порта, попробуйте ещё раз:");
                }
            } catch (SocketException e) {
                System.err.println("Ошибка доступа к сокету");
            }
        }
    }

    private String getInitializationDate (){
        String initializationDate = "-";
        try{
            ResultSet res = connection.createStatement().executeQuery("SELECT creationDate FROM groups ORDER BY id LIMIT 1");
            if(res.next()){
                initializationDate = res.getString(1);
            }
        } catch (SQLException e) {
            System.err.println(ExceptionUtils.getStackTrace(e));
            System.err.println("Ошибка при получении даты инициализации коллекции");
        }
        return initializationDate;
    }

    /**
     * run method
     */
    public void run() {
        Set<StudyGroup> groups = new TreeSet<>();
        String collectionInitializationDate = getInitializationDate();
        System.out.println("Старт работы");
        List<CommandEnum> history = new ArrayList<>();
        DataInputSource inputSource = new DataInputSource(scan);
        programState = new Receiver(groups, history, true, inputSource, collectionInitializationDate, connection);
        programState.updateCollectionFromDB();
        ServerReceiver receiver = new ServerReceiver(datagramSocket, this, programState, 20);
        receiver.setDaemon(true);
        receiver.start();
        while (programState.getWorking() && active) {
            try {
                String rawCommand = programState.getSource().get();
                if (rawCommand.isEmpty()) continue;
                rawCommand = rawCommand.trim();
                if (rawCommand.matches("save")) {
                    System.out.println(new SaveCommand(programState).execute());
                } else if (rawCommand.matches("exit")) {
                    System.out.println(new SaveCommand(programState).execute());
                    System.exit(0);
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * stop method
     */
    public void stop() {
        if (programState != null) System.out.println(new SaveCommand(programState).execute());
        active = false;
    }
}

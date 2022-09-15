package main;

import org.apache.commons.lang3.exception.ExceptionUtils;
import server.Server;

import java.util.*;

public class Main {
    static final Scanner scan = new Scanner(System.in);
    public static void main(String[] args){
        Server server = new Server(scan);
        try {
            server.run();
        } catch (Exception e) {
            server.stop();
            System.err.println(ExceptionUtils.getStackTrace(e));
        }
    }
}

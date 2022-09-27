package command;

import com.google.gson.*;
import util.LocalDateDeserializer;
import util.LocalDateSerializer;
import util.StudyGroup;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Set;

/**
 * Save command writes data into output file
 */

public class SaveCommand extends Command{
    private static final long serialVersionUID = 14L;
    private final Receiver state;
    public SaveCommand(Receiver state){
        this.state = state;
        this.name = CommandEnum.SAVE;
    }

    public String execute() {
//        try {
//            return "";
//        } catch () {
//            return "Возникла ошибка при выполнении команды save";
//        }
        return "SAVED";
    }

    public static String describe() {
        return "save : сохранить коллекцию в файл";
    }
}

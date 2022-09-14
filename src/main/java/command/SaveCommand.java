package command;

import com.google.gson.*;
import util.StudyGroup;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * Save command writes data into output file
 */

public class SaveCommand extends Command{
    private static final long serialVersionUID = 14L;
    private final String outputFilepath;
    private final Set<StudyGroup> collection;
    private final Receiver state;
    public SaveCommand(Receiver state){
        this.state = state;
        outputFilepath = state.getOutputFilepath();
        collection = state.getCollection();
        this.name = CommandEnum.SAVE;
    }

    public String execute() {
        try {
            Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy HH:mm:ss").create();
            File outputFile = new File(outputFilepath);
            OutputStream os = new FileOutputStream(outputFile);
            BufferedOutputStream br = new BufferedOutputStream(os, 16384);
            br.write(gson.toJson(collection).getBytes(StandardCharsets.UTF_8));
            br.close();
            os.close();
            boolean isSuccessfullySaved = new File(outputFilepath).exists();
            if(isSuccessfullySaved) return "Коллекция сохранена в файл";
            return "Возникла ошибка при выполнении команды save. Проверьте путь и права доступа к файлу";
        } catch (IOException e) {
            return "Возникла ошибка при выполнении команды save. Проверьте путь и права доступа к файлу";
        }
    }

    public static String describe() {
        return "save : сохранить коллекцию в файл";
    }
}

package command;

import util.StudyGroup;

import java.util.Set;

/**
 * Info command returns an information about current collection state: type, initialization date, number of elements
 */

public class InfoCommand extends Command{
    private static final long serialVersionUID = 8L;
    Set<StudyGroup> collection;
    String creationDate;
    public InfoCommand(Set<StudyGroup> t, String d){
        collection = t;
        creationDate = d;
        this.name = CommandEnum.INFO;
    }
    public String execute(){
        return String.format("Тип коллекции - %s. Дата инициализации - %s. Количество элементов - %d\n", collection.getClass(), creationDate, collection.size());
    }
    public static String describe() {
        return "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }
}

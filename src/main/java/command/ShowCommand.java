package command;

import util.StudyGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Show command returns an info string about each element in collection
 */

public class ShowCommand extends Command {
    private static final long serialVersionUID = 15L;
    public final CommandEnum name = CommandEnum.SHOW;

    private final Set<StudyGroup> collection;
    public ShowCommand(Set<StudyGroup> c){
        collection = c;
    };

    public String execute(){
        if (collection.size() < 1){
            return "Коллекция пуста";
        } else {
            List<String> groups = new ArrayList<>();
            collection.forEach((StudyGroup g) -> groups.add(g.toString()));
            return String.join("\n", groups);
        }
    }
    public static String describe(){
        return "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}

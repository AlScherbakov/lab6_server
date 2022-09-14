package command;

import util.StudyGroup;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Max group by admin command returns the greatest collection elemets, comparing my groupAdmin field
 */

public class MaxGroupByAdminCommand extends Command{
    private static final long serialVersionUID = 9L;
    private final Set<StudyGroup> collection;
    public MaxGroupByAdminCommand(Receiver state){
        collection = state.getCollection();
        this.name = CommandEnum.MAX_BY_GROUP_ADMIN;
    }
    public String execute(){
        TreeSet<StudyGroup> g = new TreeSet<>(Comparator.comparing(StudyGroup::getAdmin));
        g.addAll(collection);
        return g.last().toString();
    }
    public static String describe(){
        return "max_by_group_admin : вывести любой объект из коллекции, значение поля groupAdmin которого является максимальным";
    }
}

package command;

import util.DataCollector;
import util.DataInputSource;
import util.StudyGroup;

import java.util.Set;
import java.util.TreeSet;

/**
 * Remove greater command filters collection and leaves only elements which are lower than given one
 */

public class RemoveGreaterCommand extends Command{
    private static final long serialVersionUID = 12L;
    TreeSet<StudyGroup> collection;
    StudyGroup group;

    public RemoveGreaterCommand(TreeSet<StudyGroup> collection, StudyGroup group){
        this.collection = collection;
        this.group = group;
        this.name = CommandEnum.REMOVE_GREATER;
    }

    public Set<StudyGroup> execute(){
        Set<StudyGroup> groupsToRemove = collection.tailSet(group);
        collection.removeAll(groupsToRemove);
        return collection;
    }
    public static String describe() {
        return "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный";
    }
}

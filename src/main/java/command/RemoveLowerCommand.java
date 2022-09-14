package command;

import util.DataCollector;
import util.DataInputSource;
import util.StudyGroup;

import java.util.Set;
import java.util.TreeSet;

/**
 * Remove lower command filters collection and leaves only elements which are greater than given one
 */

public class RemoveLowerCommand extends Command{
    private static final long serialVersionUID = 13L;
    TreeSet<StudyGroup> collection;
    StudyGroup group;

    public RemoveLowerCommand(TreeSet<StudyGroup> collection, StudyGroup group){
        this.collection = collection;
        this.group = group;
        this.name = CommandEnum.REMOVE_LOWER;
    }

    public Set<StudyGroup> execute(){
        Set<StudyGroup> groupsToRemove = collection.headSet(group);
        collection.removeAll(groupsToRemove);
        return collection;
    }
    public static String describe() {
        return "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный";
    }
}

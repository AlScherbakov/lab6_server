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
    DataInputSource source;

    public RemoveGreaterCommand(TreeSet<StudyGroup> c, DataInputSource s){
        collection = c;
        source = s;
        this.name = CommandEnum.REMOVE_GREATER;
    }

//    @Override
//    public Set<StudyGroup> execute(){
//        System.out.println("Введите новый элемент для сравнения:");
//        StudyGroup aGroup = new DataCollector(source).requestStudyGroup();
//        Set<StudyGroup> groupsToRemove = collection.tailSet(aGroup);
//        collection.removeAll(groupsToRemove);
//        return collection;
//    }
    public static String describe() {
        return "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный";
    }
}

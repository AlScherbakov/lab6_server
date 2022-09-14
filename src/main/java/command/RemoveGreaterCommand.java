package command;

import messages.RemoveGreaterMessage;
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
    private final TreeSet<StudyGroup> collection;
    private final StudyGroup group;
    private final Receiver state;

    public RemoveGreaterCommand(Receiver state, RemoveGreaterMessage message){
        this.state = state;
        this.group = message.getElement();
        this.collection = (TreeSet<StudyGroup>) state.getCollection();
        this.name = CommandEnum.REMOVE_GREATER;
    }

    public String execute(){
        try {
            Set<StudyGroup> groupsToRemove = collection.tailSet(group);
            collection.removeAll(groupsToRemove);
            state.setCollection(collection);
            return "Все элементы, превышающие заданный, удалены из коллекции";
        } catch (ClassCastException e) {
            return "Возникла ошибка при выполнении команды remove_greater";
        }
    }
    public static String describe() {
        return "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный";
    }
}

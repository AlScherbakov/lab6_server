package command;

import messages.RemoveLowerMessage;
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
    private final TreeSet<StudyGroup> collection;
    private final StudyGroup group;
    private final Receiver state;

    public RemoveLowerCommand(Receiver state, RemoveLowerMessage message){
        this.state = state;
        this.collection = (TreeSet<StudyGroup>) state.getCollection();
        this.group = message.getElement();
        this.name = CommandEnum.REMOVE_LOWER;
    }

    public String execute(){
        try {
            Set<StudyGroup> groupsToRemove = collection.headSet(group);
            collection.removeAll(groupsToRemove);
            state.setCollection(collection);
            return "Все элементы, меньшие, чем заданный, удалены из коллекции";
        } catch (ClassCastException e) {
            return "Возникла ошибка при выполнении команды remove_lower";
        }
    }

    public static String describe() {
        return "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный";
    }
}

package command;

import messages.RemoveGreaterMessage;
import util.DataCollector;
import util.DataInputSource;
import util.StudyGroup;

import java.sql.SQLException;
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
    private final RemoveGreaterMessage message;

    public RemoveGreaterCommand(Receiver state, RemoveGreaterMessage message){
        this.state = state;
        this.group = message.getElement();
        this.collection = (TreeSet<StudyGroup>) state.getCollection();
        this.message = message;
        this.name = CommandEnum.REMOVE_GREATER;
    }

    public String execute(){
        try {
            Set<StudyGroup> groupsToRemove = collection.tailSet(group);
            Set<StudyGroup> removedGroups = new TreeSet<>();
            try {
                groupsToRemove.forEach(x -> {
                    if(x.getAuthor() == message.getUser().getId()){
                        try {
                            state.removeGroupFromDB(x.getId());
                            removedGroups.add(x);
                        } catch (SQLException e) {
                            throw new RuntimeException("Возникла ошибка при удалении элемента из коллекции");
                        }
                    }
                });
                collection.removeAll(removedGroups);
                state.setCollection(collection);
            } catch (RuntimeException e){
                System.err.println(e.getMessage());
                return e.getMessage();
            }
            return "Все элементы, превышающие заданный и принадлежащие Вам, удалены из коллекции";
        } catch (ClassCastException e) {
            return "Возникла ошибка при выполнении команды remove_greater";
        }
    }
    public static String describe() {
        return "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный";
    }
}

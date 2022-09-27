package command;

import messages.RemoveLowerMessage;
import util.StudyGroup;

import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

/**
 * Remove lower command filters collection and leaves only elements which are greater than given one
 */

public class RemoveLowerCommand extends Command {
    private static final long serialVersionUID = 13L;
    private final TreeSet<StudyGroup> collection;
    private final StudyGroup group;
    private final Receiver state;
    private final RemoveLowerMessage message;

    public RemoveLowerCommand(Receiver state, RemoveLowerMessage message) {
        this.state = state;
        this.collection = (TreeSet<StudyGroup>) state.getCollection();
        this.group = message.getElement();
        this.message = message;
        this.name = CommandEnum.REMOVE_LOWER;
    }

    public static String describe() {
        return "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный";
    }

    public String execute() {
        Set<StudyGroup> groupsToRemove = collection.headSet(group);
        Set<StudyGroup> removedGroups = new TreeSet<>();
        try {
            groupsToRemove.forEach(x -> {
                if (x.getAuthor() == message.getUser().getId()) {
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
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            return e.getMessage();
        }
        return "Все элементы, меньшие, чем заданный и принадлежащие Вам, удалены из коллекции";
    }
}

package command;

import messages.ClearMessage;
import util.StudyGroup;

import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Clear command returns empty TreeSet
 */

public class ClearCommand extends Command{
    private static final long serialVersionUID = 2L;
    private final Receiver state;
    private final ClearMessage message;

    public ClearCommand(Receiver state, ClearMessage message){
        this.state = state;
        this.message = message;
        this.name = CommandEnum.CLEAR;
    }

    public String execute (){
        try{
            Set<StudyGroup> groupsToRemove = new TreeSet<>();
            state.getCollection().stream().filter(x -> x.getAuthor() == message.getUser().getId()).forEach(x -> {
                try {
                    state.removeGroupFromDB(x.getId());
                } catch (SQLException e) {
                    throw new RuntimeException("Ошибка очистки коллекции");
                }
                groupsToRemove.add(x);
            });
            TreeSet<StudyGroup> collectionCandidate = (TreeSet<StudyGroup>) state.getCollection();
            collectionCandidate.removeAll(groupsToRemove);
            state.setCollection(collectionCandidate);
        } catch (RuntimeException e){
            System.err.println(e.getMessage());
            return e.getMessage();
        }
        return String.format("Из коллекции удалены все элементы, принадлежащие Вам (#%s)", message.getUser().getId());
    }

    public static String describe() {
        return "clear : очистить коллекцию";
    }
}

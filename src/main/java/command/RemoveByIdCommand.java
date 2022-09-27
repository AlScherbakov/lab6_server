package command;

import messages.Message;
import messages.RemoveByIdMessage;
import util.StudyGroup;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Set;

/**
 * Remove by id command returns collection without an element with given id
 */

public class RemoveByIdCommand extends Command {
    private static final long serialVersionUID = 11L;
    private final long id;
    private final Set<StudyGroup> collection;
    private final Receiver state;
    private final RemoveByIdMessage message;
    public RemoveByIdCommand(Receiver state, RemoveByIdMessage message){
        this.state = state;
        id = message.getId();
        collection = state.getCollection();
        this.message = message;
        this.name = CommandEnum.REMOVE_BY_ID;
    }
    public String execute() {
        StudyGroup removeCandidate = collection.stream().filter(x -> x.getId() == id).findAny().orElse(null);
        if (removeCandidate == null){
            return String.format("Не существует элемента с id #%d", id);
        } else if (removeCandidate.getAuthor() != message.getUser().getId()){
            return String.format("Вы не являетесь автором элемента #%d", id);
        } else {
            try {
                state.removeGroupFromDB(id);
                collection.removeIf(g -> g.getId() == id);
                state.setCollection(collection);
            } catch (SQLException e){
                System.err.println("Возникла ошибка при удалении элемента из коллекции");
                return "Возникла ошибка при удалении элемента из коллекции";
            }
        }
        return String.format("Элемент #%d удалён из коллекции (show - список элементов)", id);
    }
    public static String describe() {
        return "remove_by_id (int)id : удалить элемент из коллекции по его id";
    }
}

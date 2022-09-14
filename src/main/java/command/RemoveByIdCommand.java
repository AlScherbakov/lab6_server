package command;

import messages.Message;
import messages.RemoveByIdMessage;
import util.StudyGroup;

import java.util.Objects;
import java.util.Set;

/**
 * Remove by id command returns collection without an element with given id
 */

public class RemoveByIdCommand extends Command {
    private static final long serialVersionUID = 11L;
    private final int id;
    private final Set<StudyGroup> collection;
    private final Receiver state;
    public RemoveByIdCommand(Receiver state, RemoveByIdMessage message){
        this.state = state;
        id = message.getId();
        collection = state.getCollection();
        this.name = CommandEnum.REMOVE_BY_ID;
    }
    public String execute(){
        if(!state.hasElementWithId(id)){
            return String.format("Не существует элемента с id #%d", id);
        }
        collection.removeIf(g -> Objects.equals(g.getId(), id));
        state.setCollection(collection);
        return String.format("Элемент #%d удалён из коллекции (show - список элементов)", id);
    }
    public static String describe() {
        return "remove_by_id (int)id : удалить элемент из коллекции по его id";
    }
}

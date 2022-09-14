package command;

import messages.UpdateElementMessage;
import util.DataCollector;
import util.DataInputSource;
import util.StudyGroup;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * Update element command returns new collection with updated element by given id
 */

public class UpdateElementCommand extends Command{
    private static final long serialVersionUID = 16L;
    private final int id;
    private final Set<StudyGroup> collection;
    private final StudyGroup updatedGroup;
    private final Receiver state;
    public UpdateElementCommand(Receiver state, UpdateElementMessage message){
        this.state = state;
        id = message.getId();
        this.updatedGroup = message.getElement();
        this.collection = state.getCollection();
        this.name = CommandEnum.UPDATE;
    }
    public String execute(){
        if (!state.hasElementWithId(id)) {
            return String.format("Не существует элемента с id #%d", id);
        }
        TreeSet<StudyGroup> g = new TreeSet<>(collection);
        g.removeIf(x -> !Objects.equals(x.getId(), id));
        StudyGroup aGroup = g.first();
        assert aGroup != null;
        collection.remove(aGroup);
        collection.add(updatedGroup);
        state.setCollection(collection);
        return String.format("Элемент #%d обновлён (show - список элементов)", id);
    }
    public static String describe(){
        return "update (int)id {element} : обновить значение элемента коллекции, id которого равен заданному";
    }
}

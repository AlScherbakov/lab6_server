package command;

import messages.UpdateElementMessage;
import util.StudyGroup;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * Update element command returns new collection with updated element by given id
 */

public class UpdateElementCommand extends Command {
    private static final long serialVersionUID = 16L;
    private final long id;
    private final Set<StudyGroup> collection;
    private final StudyGroup updatedGroup;
    private final Receiver state;
    private final UpdateElementMessage message;

    public UpdateElementCommand(Receiver state, UpdateElementMessage message) {
        this.state = state;
        id = message.getId();
        this.updatedGroup = message.getElement();
        this.collection = state.getCollection();
        this.message = message;
        this.name = CommandEnum.UPDATE;
    }

    public static String describe() {
        return "update (int)id {element} : обновить значение элемента коллекции, id которого равен заданному";
    }

    public String execute() {
        StudyGroup groupToUpdate = collection.stream().filter(x -> x.getId() == id).findAny().orElse(null);

        if (groupToUpdate == null) {
            return String.format("Не существует элемента с id #%d", id);
        } else if (groupToUpdate.getAuthor() != message.getUser().getId()) {
            return String.format("Элемент с id #%d не принадлежит Вам - недоступен для модификации", id);
        } else {
            TreeSet<StudyGroup> g = new TreeSet<>(collection);
            g.removeIf(x -> !Objects.equals(x.getId(), id));
            StudyGroup aGroup = g.first();
            assert aGroup != null;
            int rowsChangedCount = state.updateElementInDB(updatedGroup);
            if (rowsChangedCount == 0) {
                return "Обновление не внесло изменений";
            } else {
                collection.remove(aGroup);
                collection.add(updatedGroup);
                state.setCollection(collection);
                return String.format("Элемент #%d обновлён (show - список элементов)", id);
            }
        }

    }
}
